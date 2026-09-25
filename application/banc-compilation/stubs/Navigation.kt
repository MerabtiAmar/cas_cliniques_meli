@file:Suppress("unused", "UNUSED_PARAMETER")
package androidx.navigation
open class NavController { fun navigate(route: String) {}; fun popBackStack(): Boolean = true }
class NavHostController : NavController()
class NavBackStackEntry { val arguments: android.os.Bundle? = null }
class NavGraphBuilder
