package plugins

import ext.getLibs
import ext.implementation
import ext.ksp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeatureModulePlugin: Plugin<Project> {

    override fun apply(target: Project) {

        with(target){
            with(pluginManager){
                apply(getLibs().plugins.android.library.get().pluginId)
                apply("plugin.compose.module")
                apply(getLibs().plugins.jetbrains.kotlin.android.get().pluginId)
                apply(getLibs().plugins.android.hilt.get().pluginId)
                apply(getLibs().plugins.ksp.gradle.plugin.get().pluginId)
            }

            dependencies{
                implementation(project(":domain"))
                implementation(project(":uikit:design-system"))
                implementation(project(":translations"))

                implementation(getLibs().androidx.viewmodel)
                implementation(getLibs().android.dagger.hilt)
                ksp(getLibs().ksp.hilt.compiler)
                implementation(getLibs().hilt.navigation)
                implementation(getLibs().kotlinx.coroutines.android)

            }
        }

    }
}