@file:Suppress("unused", "UNUSED_PARAMETER")
package android.widget
import android.content.Context
class Toast { fun show() {}; companion object { const val LENGTH_LONG = 1; fun makeText(context: Context, text: CharSequence, duration: Int): Toast = Toast() } }
