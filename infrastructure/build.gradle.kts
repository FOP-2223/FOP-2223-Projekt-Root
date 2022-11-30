@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins{
    alias(libs.plugins.javafx)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.annotations)
    implementation(libs.flatlaf)
}
