package com.example.hw2_project

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = SharedPreferenceUtil(applicationContext)
    }

    companion object {
        lateinit var prefs: SharedPreferenceUtil
    }

}