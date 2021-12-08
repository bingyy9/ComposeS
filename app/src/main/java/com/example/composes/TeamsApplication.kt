package com.example.composes

import android.app.Activity
import android.app.Application
import android.content.Context

class TeamsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: TeamsApplication
            private set

        fun applicationContext(): Context {
            return instance.applicationContext
        }

        fun get(): TeamsApplication {
            return instance
        }

//        private var mAppInstance: TeamsApplication? = null
//
//        fun getInstance(): TeamsApplication? {
//            return mAppInstance
//        }
    }

}