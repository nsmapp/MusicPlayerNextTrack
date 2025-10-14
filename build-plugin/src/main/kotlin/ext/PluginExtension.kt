package ext

import com.android.build.api.dsl.AndroidResources
import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DefaultConfig
import com.android.build.api.dsl.Installation
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ProductFlavor
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.the

fun Project.getLibs(): LibrariesForLibs = the<LibrariesForLibs>()

internal fun DependencyHandlerScope.implementation(
    dependency: Provider<MinimalExternalModuleDependency>
) {
    add("implementation", dependency)
}


internal fun DependencyHandlerScope.implementation(
    dependency: ProjectDependency
) {
    add("implementation", dependency)
}

internal fun DependencyHandlerScope.implementation(
    dependency: ConfigurableFileCollection
) {
    add("implementation", dependency)
}

internal fun DependencyHandlerScope.ksp(
    dependency: Provider<MinimalExternalModuleDependency>
) {
    add("ksp", dependency)
}

internal fun Project.androidOptions(
    block: CommonExtension<out BuildFeatures, out BuildType, out DefaultConfig, out ProductFlavor, out AndroidResources, out Installation>.() -> Unit
) {
    androidExtension.apply {
        block()
    }
}

private val Project.androidExtension: CommonExtension<out BuildFeatures, out BuildType, out DefaultConfig, out ProductFlavor, out AndroidResources, out Installation>
    get() = extensions.findByType(BaseAppModuleExtension::class)
        ?: requireNotNull(extensions.findByType(LibraryExtension::class)) {
            "\"Project.androidExtension\" value may be called only from android application" +
                    " or android library gradle script"
        }