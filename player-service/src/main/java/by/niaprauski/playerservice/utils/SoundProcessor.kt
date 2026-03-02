package by.niaprauski.playerservice.utils

import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.audio.AudioProcessor.EMPTY_BUFFER
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.nio.ByteOrder

@UnstableApi
class SoundProcessor(
    private val scope: CoroutineScope,
    private val waveForm: MutableStateFlow<FloatArray>,
) : AudioProcessor {

    private val waveChannel = Channel<ByteBuffer>(Channel.CONFLATED)

    private var isVisuallyEnabled = false
    private var chank: Int = 64

    private var inputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
    private var outputBuffer: ByteBuffer = EMPTY_BUFFER
    private var buffer: ByteBuffer = EMPTY_BUFFER
    private var inputEnded = false

    init {
        scope.launch {
            for (buffer in waveChannel) {
                val array = processWave(buffer)
                waveForm.update { array }
            }
        }
    }

    override fun configure(inputAudioFormat: AudioProcessor.AudioFormat): AudioProcessor.AudioFormat {
        this.inputAudioFormat = inputAudioFormat
        return inputAudioFormat
    }

    override fun isActive(): Boolean {
        return inputAudioFormat != AudioProcessor.AudioFormat.NOT_SET
    }

    override fun queueInput(inputBuffer: ByteBuffer) {
        if (!inputBuffer.hasRemaining()) return

        val count = inputBuffer.remaining()

        if (buffer.capacity() < count) buffer = ByteBuffer.allocateDirect(count)
            .order(ByteOrder.nativeOrder())
        else buffer.clear()

        if (isVisuallyEnabled) waveChannel.trySend(buffer)

        buffer.put(inputBuffer)
        buffer.flip()
        outputBuffer = buffer
    }

    private suspend fun processWave(buffer: ByteBuffer): FloatArray =
        withContext(Dispatchers.Default) {
            buffer.order(ByteOrder.nativeOrder())

            val limit = buffer.limit()
            val totalSamples = buffer.remaining() / 2
            val targetPoints = chank

            val actualPoints = if (totalSamples < targetPoints) totalSamples else targetPoints
            val waveArray = FloatArray(targetPoints)
            if (totalSamples == 0) return@withContext waveArray

            val step = totalSamples / actualPoints

            for (i in 0 until actualPoints) {
                val index = i * step
                val position = index * 2

                if (index * 2 < limit) {
                    val sample = buffer.getShort(position).toFloat() / Short.MAX_VALUE
                    waveArray[i] = sample
                }
            }
            waveArray
        }

    override fun queueEndOfStream() {
        inputEnded = true
    }

    override fun getOutput(): ByteBuffer {
        val output = outputBuffer
        outputBuffer = EMPTY_BUFFER
        return output
    }

    override fun isEnded(): Boolean {
        return inputEnded && outputBuffer === EMPTY_BUFFER
    }

    override fun flush() {
        outputBuffer = EMPTY_BUFFER
        inputEnded = false
    }

    override fun reset() {
        flush()
        buffer = EMPTY_BUFFER
        inputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
    }

    fun setIsVisuallyEnabled(enabled: Boolean) {
        isVisuallyEnabled = enabled
    }

    fun setChank(chank: Int) {
        this.chank = chank
    }
}