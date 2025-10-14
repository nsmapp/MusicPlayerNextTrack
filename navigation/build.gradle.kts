plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    id("plugin.compose.module")
}

android {
    namespace = "by.niaprauski.navigation"
}

dependencies {

    implementation(project(":features:player"))
    implementation(project(":features:library"))
    implementation(project(":features:settings"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.android.navigation)
    implementation(libs.androidx.navigation.fragment.ktx)
}