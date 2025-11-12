androidApplication {
    namespace = "org.sportsnow.tv"

    dependencies {
        // repositories block is not allowed in module DCL; repositories are defined in settings.gradle.dcl

        // Compose BOM and core (versions managed by BOM where supported)
        implementation(platform("androidx.compose:compose-bom:2024.10.01"))
        implementation("androidx.activity:activity-compose:1.9.3")
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.foundation:foundation")
        // Units like dp/sp
        implementation("androidx.compose.ui:ui-unit")
        implementation("androidx.compose.material3:material3:1.3.0")

        // Navigation
        implementation("androidx.navigation:navigation-compose:2.8.3")
        // Ensure navigation argument helpers are available (pulled with navigation-compose but being explicit is harmless)
        implementation("androidx.navigation:navigation-runtime-ktx:2.8.3")

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
