dependencies {
    implementation(libs.annotations)
    testImplementation(libs.junit.core)
}

tasks {
    test {
        val runDir = File("../build/run")
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        useJUnitPlatform()
    }
}
