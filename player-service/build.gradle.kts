plugins {
    alias(libs.plugins.android.library)
    id("plugin.android.module")
}

android {
    namespace = "by.niaprauski.playerservice"
}

dependencies {

    implementation(project(":utils"))

    implementation(libs.media3.exoplayer)
    implementation(libs.media3.dash)
    implementation(libs.media3.ui)
    implementation(libs.media3.mediasession)

    implementation(libs.material)
}