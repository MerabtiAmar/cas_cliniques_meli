package dz.meli.cascliniques

import android.app.Application
import dz.meli.cascliniques.donnees.Depot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class CasCliniquesApp : Application() {
    lateinit var depot: Depot
        private set

    override fun onCreate() {
        super.onCreate()
        depot = Depot(this, CoroutineScope(SupervisorJob() + Dispatchers.Default))
    }
}
