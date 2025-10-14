pluginManagement{
    repositories{
        google()
        gradlePluginPortal()
        mavenCentral()

    }
}

dependencyResolutionManagement{
    repositories{
        mavenCentral()
        google()
    }

    versionCatalogs{
        create("libs"){
            from(files("../gradle/libs.versions.toml"))
        }
    }
}