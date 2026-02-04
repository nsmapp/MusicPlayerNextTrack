plugins {
    id("plugin.feature.module")
}

android {
    namespace = "by.niaprauski.library"
}

dependencies {
    implementation(project(":player-service"))
    implementation(libs.media3.mediasession)

    implementation(libs.paging3.runtime)
    implementation(libs.paging3.compose)
}