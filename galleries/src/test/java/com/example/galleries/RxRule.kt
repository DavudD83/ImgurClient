package com.example.galleries

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxRule : TestRule {
    private val trampolineScheduler = Schedulers.trampoline()

    override fun apply(base: Statement, description: Description): Statement {
        return object: Statement() {
            override fun evaluate() {
                RxAndroidPlugins.setMainThreadSchedulerHandler { trampolineScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { trampolineScheduler }

                RxJavaPlugins.setIoSchedulerHandler { trampolineScheduler }

                base.evaluate()

                RxAndroidPlugins.reset()
                RxJavaPlugins.reset()
            }
        }
    }
}