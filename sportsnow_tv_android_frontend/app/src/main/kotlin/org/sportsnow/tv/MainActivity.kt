package org.sportsnow.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.sportsnow.tv.navigation.SportsNavGraph
import org.sportsnow.tv.ui.theme.SportsNowTheme

/**
 * PUBLIC_INTERFACE
 * MainActivity is the Compose host for the Android TV application.
 * It sets the app theme and navigation graph for Home, Details and Player screens.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportsNowTheme {
                SportsNavGraph()
            }
        }
    }
}
