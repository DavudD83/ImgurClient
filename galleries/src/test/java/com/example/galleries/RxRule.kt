package com.example.galleries

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.ExternalResource

class RxRule : ExternalResource() {
    private val trampolineScheduler = Schedulers.trampoline()

    override fun before() {
        super.before()

        RxAndroidPlugins.setMainThreadSchedulerHandler { trampolineScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { trampolineScheduler }

        RxJavaPlugins.setIoSchedulerHandler { trampolineScheduler }
    }

    override fun after() {
        super.after()

        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}