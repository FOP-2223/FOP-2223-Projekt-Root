dependencies {
    implementation(libs.annotations)
    testImplementation(libs.junit.core)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
