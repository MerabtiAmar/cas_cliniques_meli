@file:Suppress("unused", "UNUSED_PARAMETER")
package androidx.navigation.compose
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
@Composable fun rememberNavController(): NavHostController = TODO()
@Composable fun NavHost(navController: NavHostController, startDestination: String, builder: NavGraphBuilder.() -> Unit) {}
fun NavGraphBuilder.composable(route: String, content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit) {}
