package davydov.dmytro.imgurclient.application

import android.app.Application


class ImgurClientApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent
            .builder()
            .context(this)
            .build()
            .also { appComponent = it }
            .inject(this)
    }

    companion object {
        lateinit var appComponent: ApplicationComponent
    }
}