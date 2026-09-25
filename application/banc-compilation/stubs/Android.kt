// Stubs des API Android utilisées par l'application, avec leurs vraies signatures, pour compiler hors SDK.
@file:Suppress("unused", "UNUSED_PARAMETER")
package android.content

import android.content.res.AssetManager
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.io.OutputStream

abstract class Context {
    open val filesDir: File get() = TODO()
    open val assets: AssetManager get() = TODO()
    open val contentResolver: ContentResolver get() = TODO()
    open val applicationContext: Context get() = TODO()
    open fun getSharedPreferences(name: String, mode: Int): SharedPreferences = TODO()
    companion object { const val MODE_PRIVATE = 0 }
}
interface SharedPreferences {
    fun getLong(key: String, defValue: Long): Long
    fun getInt(key: String, defValue: Int): Int
    fun edit(): Editor
    interface Editor {
        fun putLong(key: String, value: Long): Editor
        fun putInt(key: String, value: Int): Editor
        fun apply()
    }
}
abstract class ContentResolver {
    fun openInputStream(uri: Uri): InputStream? = null
    fun openOutputStream(uri: Uri): OutputStream? = null
}
