import ext.getLibs

plugins {
    id("plugin.app.module")
}

android {
    namespace = "by.niaprauski.nt"
}

dependencies {
    implementation(getLibs().androidx.ui)
}