@file:Suppress("unused", "UNUSED_PARAMETER")
package androidx.room
import kotlin.reflect.KClass
annotation class Dao
annotation class Database(val entities: Array<KClass<*>>, val version: Int, val exportSchema: Boolean = true)
annotation class Entity(val tableName: String = "", val primaryKeys: Array<String> = [])
annotation class PrimaryKey(val autoGenerate: Boolean = false)
annotation class Query(val value: String)
annotation class Insert
annotation class Update
annotation class Upsert
abstract class RoomDatabase { class Builder<T : RoomDatabase> { fun build(): T = TODO() } }
object Room { fun <T : RoomDatabase> databaseBuilder(context: android.content.Context, klass: Class<T>, name: String): RoomDatabase.Builder<T> = TODO() }
