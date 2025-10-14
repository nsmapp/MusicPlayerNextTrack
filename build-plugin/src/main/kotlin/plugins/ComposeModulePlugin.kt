package plugins

import ext.getLibs
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeModulePlugin: Plugin<Project> {


    override fun apply(target: Project) {

        with(target){
            with(pluginManager){
                apply(getLibs().plugins.compose.compiler.get().pluginId)
                apply("plugin.android.module")
            }

        }
    }
}