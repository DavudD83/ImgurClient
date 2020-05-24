package davydov.dmytro.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
