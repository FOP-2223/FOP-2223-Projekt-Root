dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure")) // TODO: runtimeOnly
    implementation(libs.flatlaf) // TODO: Remove
}
