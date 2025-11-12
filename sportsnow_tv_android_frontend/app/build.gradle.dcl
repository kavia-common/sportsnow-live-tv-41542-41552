androidApplication {
    namespace = "org.sportsnow.tv"

    dependencies {
        // repositories block is not allowed in module DCL; repositories are defined in settings.gradle.dcl

        // Remove Compose dependencies to avoid inline compiler issues in CI (Compose not allowed by project rules)
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.core:core-ktx:1.13.1")
        // Navigation (non-Compose not used currently, placeholder if needed)
        implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
        implementation("androidx.navigation:navigation-ui-ktx:2.8.3")

        // TV dependencies: Using Compose-based UI; Leanback not required.
        // If needed later, add stable Leanback 1.1.0 when available in repos.

        // Lifecycle/Coroutines
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

        // Media3 playback
        implementation("androidx.media3:media3-exoplayer:1.4.1")
        implementation("androidx.media3:media3-ui:1.4.1")

        // Firebase (guarded in code if no google-services.json)
        implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
        implementation("com.google.firebase:firebase-firestore-ktx:25.1.1")
        implementation("com.google.firebase:firebase-analytics-ktx:22.1.2")
    }
}
