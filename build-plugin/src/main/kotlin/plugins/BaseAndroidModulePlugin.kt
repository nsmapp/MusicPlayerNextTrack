package plugins

import com.android.build.api.dsl.LibraryExtension
import ext.getLibs
import ext.implementation
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class BaseAndroidModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {
            with(pluginManager) {
                apply(getLibs().plugins.jetbrains.kotlin.android.get().pluginId)
            }

            extensions.configure<LibraryExtension> {

                compileSdk = getLibs().versions.compileSDk.get().toInt()

                defaultConfig {
                    minSdk = getLibs().versions.minSDK.get().toInt()

                    consumerProguardFiles("consumer-rules.pro")
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }

                    debug {
                        isMinifyEnabled = false
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                tasks.withType<KotlinCompile>().configureEach {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }

                dependencies {
                    implementation(getLibs().androidx.core.ktx)
                    implementation(getLibs().androidx.appcompat)

                }

            }
        }
    }
}
