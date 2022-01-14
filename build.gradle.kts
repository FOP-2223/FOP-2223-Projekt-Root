plugins {
    java
}

tasks {
    create<Jar>("graderJar") {
        group = "build"
        afterEvaluate {
            archiveFileName.set("FOP-2022-Projekt-${project.version}.jar")
            from(project(":grader").sourceSets.main.get().allSource)
            from(project(":solution").sourceSets.main.get().allSource)
        }
    }
}

allprojects {
    apply(plugin = "java")
    version = "0.1.0-SNAPSHOT"
    repositories {
        mavenCentral()
    }
    java {
        withSourcesJar()
    }
    tasks {
        jar {
            archiveFileName.set("${rootProject.name}-${project.name}.jar")
        }
        named<Jar>("sourcesJar") {
            archiveFileName.set("${rootProject.name}-${project.name}-sources.jar")
        }
    }
}
