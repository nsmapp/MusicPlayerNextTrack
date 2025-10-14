plugins {
    alias(libs.plugins.android.library)
    id("plugin.android.module")
    alias(libs.plugins.ksp.gradle.plugin)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "by.niaprauski.data"
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.android.room.runtime)
    implementation(libs.android.room.ktx)
    ksp(libs.android.ksp.room.compiler)

    implementation(libs.androidx.datastore)

    implementation(libs.android.dagger.hilt)
    ksp(libs.ksp.hilt.compiler)

}