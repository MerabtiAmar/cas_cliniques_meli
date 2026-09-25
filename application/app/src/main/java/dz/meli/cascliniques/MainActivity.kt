package dz.meli.cascliniques

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import dz.meli.cascliniques.ui.LocalDepot
import dz.meli.cascliniques.ui.Navigation
import dz.meli.cascliniques.ui.ThemeCas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val depot = (application as CasCliniquesApp).depot
        setContent {
            ThemeCas {
                CompositionLocalProvider(LocalDepot provides depot) {
                    Navigation()
                }
            }
        }
    }
}
