pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "librarys"

include(":app")
include(":core-data-store")
include(":core-dao")
include(":core-view-model")
include(":core-databinding-ui")
//include(":core-compose-ui")
include(":core-common")
