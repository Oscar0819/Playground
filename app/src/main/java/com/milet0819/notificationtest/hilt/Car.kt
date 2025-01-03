package com.milet0819.notificationtest.hilt

import javax.inject.Inject

class Car @Inject constructor(private val engine: Engine) {
    fun drive(): String {
        return engine.start()
    }
}