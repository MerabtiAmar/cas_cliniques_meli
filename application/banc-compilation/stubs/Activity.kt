@file:Suppress("unused", "UNUSED_PARAMETER")
package androidx.activity
open class ComponentActivity { val application: android.app.Application get() = TODO(); protected open fun onCreate(savedInstanceState: android.os.Bundle?) {} }
fun ComponentActivity.enableEdgeToEdge() {}
