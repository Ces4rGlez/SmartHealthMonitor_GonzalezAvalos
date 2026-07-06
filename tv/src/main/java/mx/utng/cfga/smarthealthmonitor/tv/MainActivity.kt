package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import mx.utng.cfga.smarthealthmonitor.tv.R

/**
 * MainActivity para Android TV.
 * Es solo el contenedor: carga MainFragment.
 */
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commit()
        }
    }
}