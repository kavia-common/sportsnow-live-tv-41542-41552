# SportsNow TV Android App

This Android TV application displays live match scores, match details, and plays short video highlights. It is optimized for TV remote navigation using Jetpack Compose. The app reads live data from Firebase Firestore when configured; otherwise it falls back to sample in-memory data for easy preview.

## Prerequisites

- Android Studio Jellyfish or later
- JDK 17
- Android SDK with API level 34
- A Firebase project (only required for real-time Firestore data)
- An Android TV device or emulator (Leanback enabled)

## Project Structure

- App ID: `org.sportsnow.tv`
- Primary module: `:app`
- Key classes:
  - `App.kt`: Initializes Firebase if available.
  - `Repository.kt`: Provides reactive streams of matches with fallback sample data.
  - `FirestoreService.kt`: Observes Firestore collections/documents when Firebase is configured.

## Firebase Setup

Firebase is optional for preview. If `google-services.json` is not present, the app logs a warning and uses sample data via `Repository`. When you add Firebase, live Firestore data will appear automatically without code changes.

### 1) Enable Firebase in the app

1. Create a Firebase project in the Firebase Console.
2. Add an Android app with the package name: `org.sportsnow.tv`.
3. Download the generated `google-services.json`.
4. Place the file at:
   - `sportsnow-live-tv-41542-41552/sportsnow_tv_android_frontend/app/google-services.json`

Note: `app/google-services.README` in the repo reiterates this placement and the policy to not commit the file.

The app calls `FirebaseApp.initializeApp(this)` inside `App.kt` and guards failures so the app still runs in CI or local environments without the JSON.

### 2) Required Firebase products

- Firestore Database
- (Optional) Firebase Analytics

In `:app/build.gradle.dcl` the following dependencies are declared via Firebase BOM:
- `com.google.firebase:firebase-firestore-ktx`
- `com.google.firebase:firebase-analytics-ktx`

No explicit Google Services Gradle plugin is required for this guarded initialization flow.

## Environment Variables

The app does not require runtime environment variables for normal operation. However, you may set standard Android build variables via your shell if needed (e.g., `ORG_GRADLE_PROJECT_*`). There are no custom `.env` variables consumed by the app at runtime.

Summary:
- None required by default
- No `.env` file used in this container

## Firestore Data Model

The app reads from the `matches` collection. When Firebase is configured, `FirestoreService` listens for changes and maps documents into the domain model.

Collection: `matches`
- Document ID: Match identifier (string), e.g., `"match_1"`

Document fields:
- `homeTeam` (string): Home team name.
- `awayTeam` (string): Away team name.
- `homeScore` (number): Home score (int).
- `awayScore` (number): Away score (int).
- `status` (string): Match status such as `"LIVE"`, `"UPCOMING"`, `"FT"`, `"HT"`.
- `startTime` (number): Epoch millis.
- `highlights` (array of objects): Optional list of video highlight items.
  - Each highlight object:
    - `title` (string): Display title for the clip.
    - `url` (string): Media URL (MP4 or stream).
    - `thumbnail` (string, optional): Image URL for thumbnail.
    - `durationSec` (number, optional): Clip length in seconds.

Example document:
```json
{
  "homeTeam": "Falcons",
  "awayTeam": "Tigers",
  "homeScore": 2,
  "awayScore": 1,
  "status": "LIVE",
  "startTime": 1731326400000,
  "highlights": [
    {
      "title": "Goal 1",
      "url": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
      "thumbnail": "https://example.com/thumb1.jpg",
      "durationSec": 30
    },
    {
      "title": "Goal 2",
      "url": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    }
  ]
}
```

Behavior with missing fields:
- If `homeTeam` or `awayTeam` is missing, the document is ignored.
- Absent numeric values default to 0; status defaults to `"UPCOMING"`.
- `highlights` can be omitted or empty.

## CI Notes

Current CI build failure:
- Task: `:app:checkDebugAarMetadata`
- Cause: Resolution failure for `androidx.leanback:leanback:1.2.0-alpha05`
- Quick remediation (choose one):
  1) Downgrade to stable Leanback in `app/build.gradle.dcl`:
     - `implementation("androidx.leanback:leanback:1.1.0")`
     - `implementation("androidx.leanback:leanback-preference:1.1.0")`
  2) Ensure Google Maven repo is configured before mavenCentral in `settings.gradle.dcl`:
     - Add `dependencyResolutionManagement { repositories { google(); mavenCentral() } }`

After change:
```shell
./gradlew :app:assembleDebug
```

## Running the App

You can run the app with or without Firebase. Without Firebase, the UI shows sample data.

### Build

From the container root:
```shell
cd sportsnow-live-tv-41542-41552/sportsnow_tv_android_frontend
./gradlew build
```

### Install and launch on a connected device or emulator

```shell
./gradlew :app:installDebug
```

Then on the Android TV launcher, open “SportsNow TV”.

### Preview without Firebase

No additional steps are required. The repository includes fallback sample data in `Repository.kt`, so you can immediately see matches and play sample highlights.

### Preview with Firebase data

1. Ensure `app/google-services.json` is in place (see Firebase Setup).
2. Create your Firestore `matches` documents following the schema above.
3. Run:
   ```shell
   ./gradlew :app:installDebug
   ```
4. Launch the app and verify live data populates the Home and Details screens.

## Notes on Networking and Media

- The app uses `android.permission.INTERNET` and allows cleartext traffic for development. Update network security configuration as needed for production.
- Media playback uses AndroidX Media3 ExoPlayer. Provide HTTPS media URLs where possible.

## Troubleshooting

Quick fix for current CI build failure (Leanback resolution):
- Edit `app/build.gradle.dcl` and change:
  - `implementation("androidx.leanback:leanback:1.2.0-alpha05")`
  - to `implementation("androidx.leanback:leanback:1.1.0")`
- Optionally align preference:
  - `implementation("androidx.leanback:leanback-preference:1.1.0-rc01")`
  - to `implementation("androidx.leanback:leanback-preference:1.1.0")`
- Ensure Google Maven is present and ordered before mavenCentral in settings.

Then re-run:
```shell
./gradlew :app:assembleDebug
```

- Firebase not initialized:
  - Logcat shows a warning:
    - `Firebase not initialized (likely missing google-services.json)`
  - Ensure the `google-services.json` path is correct and the package name matches `org.sportsnow.tv`.

- No matches displayed:
  - Without Firebase, sample data should appear.
  - With Firebase, verify Firestore rules allow read access and your documents match the schema.

- Video not playing:
  - Verify the highlight `url` is reachable from the device/emulator and uses a supported format.

- Build fails resolving Leanback dependency:
  - Error resembles:
    - `Could not find androidx.leanback:leanback:1.2.0-alpha05`
  - Fix options (choose one):
    1) Ensure Google Maven repository is present at the project level (settings.gradle / repositories):
       - `google()` should be listed before `mavenCentral()`.
    2) Use a stable Leanback version known to be available:
       - Replace `androidx.leanback:leanback:1.2.0-alpha05` with `androidx.leanback:leanback:1.1.0`
       - Keep `androidx.leanback:leanback-preference:1.1.0-rc01` or align it to `1.1.0` if necessary.
  - After adjusting, re-run:
    - `./gradlew :app:dependencies --configuration debugRuntimeClasspath` to verify resolution.

## References

- App initialization: `app/src/main/kotlin/org/sportsnow/tv/App.kt`
- Firestore wrapper: `app/src/main/kotlin/org/sportsnow/tv/data/FirestoreService.kt`
- Repository and sample data: `app/src/main/kotlin/org/sportsnow/tv/data/Repository.kt`
- Gradle dependencies: `app/build.gradle.dcl`
- Manifest and TV launcher setup: `app/src/main/AndroidManifest.xml`

### Appendix: Fixing Leanback resolution via Gradle snippets

If you continue to see resolution failures for `androidx.leanback:leanback:1.2.0-alpha05`, apply one of the following approaches:

Option A — Prefer stable Leanback 1.1.0:
```gradle
// app/build.gradle.dcl (dependencies block)
implementation("androidx.leanback:leanback:1.1.0")
implementation("androidx.leanback:leanback-preference:1.1.0")
```

Option B — Ensure repositories contain Google Maven and have correct order:
```kotlin
// settings.gradle.dcl
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}
```

After changes, verify dependency resolution:
```shell
./gradlew :app:dependencies --configuration debugRuntimeClasspath
./gradlew :app:assembleDebug
```
