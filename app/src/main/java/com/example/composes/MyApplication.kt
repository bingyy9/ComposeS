package com.example.composes

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mAppInstance = this
    }

    companion object {
        private var mAppInstance: MyApplication? = null

        fun getInstance(): MyApplication? {
            return mAppInstance
        }
    }

}