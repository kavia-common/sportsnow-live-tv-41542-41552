package org.sportsnow.tv

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

/**
 * Application entry for SportsNow TV.
 * Initializes Firebase if available.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
            Log.i("SportsNowTV", "Firebase initialized")
        } catch (t: Throwable) {
            // Guard against missing google-services.json in CI
            Log.w("SportsNowTV", "Firebase not initialized (likely missing google-services.json): ${t.message}")
        }
    }
}
