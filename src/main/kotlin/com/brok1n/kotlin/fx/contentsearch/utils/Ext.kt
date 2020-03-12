package com.brok1n.kotlin.fx.contentsearch.utils

import javafx.application.Platform
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")


fun String.log(){
    println("LOG ${System.currentTimeMillis().toDateTimeStr()}:$this")
}

fun postRun( delay: Long, run: Runnable ) {
    thread {
        try {
            Thread.sleep(delay)
        }catch (e:Exception){
            e.printStackTrace()
        }
        run.run()
    }
}

fun postRunOnMainThread( delay: Long, run: Runnable ) {
    thread {
        try {
            Thread.sleep(delay)
        }catch (e:Exception){
            e.printStackTrace()
        }
        Platform.runLater(run)
    }
}

fun Long.toDateTimeStr():String {
    return simpleDataFormat.format(Date(this))
}