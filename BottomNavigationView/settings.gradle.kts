pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local") }
//        maven("https://jitpack.io" )
    }
}

rootProject.name = "BottomNavigationView"
include(":app")
 