package com.mercadolibre.candidate.uicontrollers.application

import android.app.Application
import android.support.v7.app.AppCompatDelegate

class ApplicationCandidate : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}