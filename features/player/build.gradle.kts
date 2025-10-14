plugins {
    id("plugin.feature.module")
}

android {
    namespace = "by.niaprauski.player"
}

dependencies {

    implementation(project(":player-service"))
    implementation(libs.media3.mediasession)
}