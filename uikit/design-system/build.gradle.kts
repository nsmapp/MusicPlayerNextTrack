plugins {
    alias(libs.plugins.android.library)
    id("plugin.compose.module")

}

android {
    namespace = "by.niaprauski.designsystem"
}

dependencies {
    api(project(":utils"))

    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.runtime.android)
    api(libs.androidx.activity.compose)
    api(libs.material)
    api(libs.androidx.foundation.layout.android)
    api(libs.androidx.material3.android)
    api(libs.androidx.ui.tooling.preview.android)
    api(libs.androidx.ui.icons)
    debugImplementation(libs.androidx.ui.tooling)
}