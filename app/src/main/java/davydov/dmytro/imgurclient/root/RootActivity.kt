package davydov.dmytro.imgurclient.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import davydov.dmytro.imgurclient.R
import kotlinx.android.synthetic.main.activity_root.*

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        savedInstanceState ?: init()
    }

    private fun init() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.rootContainer, RootFragment.newInstance())
            .commit()
    }
}
