package by.niaprauski.playerservice.utils

import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.nio.ByteBuffer
import kotlin.math.sqrt

@UnstableApi
class SoundProcessor(
    val chank: Int,
    val waveFlow: MutableStateFlow<FloatArray>
): AudioProcessor {

    private val max16BitSoundValue = 32767.0f
    private var outputBuffer: ByteBuffer = AudioProcessor.EMPTY_BUFFER

    override fun configure(inputAudioFormat: AudioProcessor.AudioFormat): AudioProcessor.AudioFormat {
        return inputAudioFormat
    }

    override fun isActive(): Boolean = true

    override fun queueInput(inputBuffer: ByteBuffer) {
        if (!inputBuffer.hasRemaining()) return

        outputBuffer = inputBuffer

        val readOnlyBuffer = inputBuffer.asReadOnlyBuffer()
        val shortBuffer = readOnlyBuffer.asShortBuffer()
        val limit = shortBuffer.limit()

        val waveArray = FloatArray(chank)
        val pointOnChank = limit / chank

        if (pointOnChank > 0) {
            for (chankPart in 0 until chank) {
                var sumOfSquares = 0.0
                for (point in 0 until pointOnChank) {
                    val sample = shortBuffer.get(chankPart * pointOnChank + point).toDouble()
                    sumOfSquares += sample * sample
                }
                val rms = sqrt(sumOfSquares / pointOnChank)
                val linearRms = (rms / max16BitSoundValue).toFloat()
                val powerRms = linearRms * linearRms
                waveArray[chankPart] = powerRms.coerceIn(0f, 1f)
            }
        }
        waveFlow.update { waveArray }
    }

    override fun queueEndOfStream() {
        waveFlow.update { FloatArray(chank) }
    }

    override fun getOutput(): ByteBuffer {
        val buffer = outputBuffer
        outputBuffer = AudioProcessor.EMPTY_BUFFER
        return buffer
    }

    override fun isEnded(): Boolean = false

    override fun flush() {}
    override fun reset() {}
}