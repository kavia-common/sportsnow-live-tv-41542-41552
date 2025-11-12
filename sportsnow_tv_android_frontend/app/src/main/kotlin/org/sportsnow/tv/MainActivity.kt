package org.sportsnow.tv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * PUBLIC_INTERFACE
 * MainActivity is a simple non-Compose Activity placeholder to allow CI build without Compose.
 * It renders a basic TextView. The TV-specific UI will be implemented without Compose.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        val textView = TextView(this).apply {
            text = "SportsNow TV"
            textSize = 24f
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        root.addView(textView)
        setContentView(root)
    }
}
