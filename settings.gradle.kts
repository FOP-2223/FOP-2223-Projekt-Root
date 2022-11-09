dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "FOP-2223-Projekt-Root"
include("application")
include("domain")
include("infrastructure")
