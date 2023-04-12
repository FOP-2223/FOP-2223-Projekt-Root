@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    java
    application
    alias(libs.plugins.style)
    alias(libs.plugins.jagr.gradle)
    alias(libs.plugins.javafx)
}

version = file("version").readLines().first()

jagr {
    assignmentId.set("projekt")
    submissions {
        val main by creating {
            from(project(":"))
            from(project(":application"))
            from(project(":domain"))
            from(project(":infrastructure"))
            studentId.set("ab12cdef")
            firstName.set("sol_first")
            lastName.set("sol_last")
        }
    }
    graders {
        val graderPublic by creating {
            graderName.set("FOP-2223-Projekt-Public")
            rubricProviderName.set("projekt.Projekt_RubricProvider")
            configureDependencies {
                implementation(libs.algoutils.tutor)
                implementation(libs.mockito.inline)
            }
        }
        val graderPrivate by creating {
            parent(graderPublic)
            graderName.set("FOP-2223-Projekt-Private")
            rubricProviderName.set("projekt.Projekt_RubricProvider_Private")
            disableTimeouts()
        }
    }
}

allprojects {
    apply(plugin = "java")
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

dependencies {
    implementation(libs.annotations)
    implementation(libs.algoutils.student)
    implementation(libs.flatlaf)
    testImplementation(libs.junit.core)
    testImplementation(project(":application"))
    testImplementation(project(":infrastructure"))
    testImplementation(project(":domain"))
    implementation(project(":application"))
    runtimeOnly(project(":infrastructure"))
}

application {
    mainClass.set("projekt.Main")
}

tasks {
    val runDir = File("build/run")
    withType<JavaExec> {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
    }
    test {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        useJUnitPlatform()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
