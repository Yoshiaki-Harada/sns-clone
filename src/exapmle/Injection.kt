package com.example.exapmle

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class AddInjection(private val default: Double) : Calc {
    override fun calc(d1: Double, d2: Double): Double {
        return d1 + d2
    }

    override fun calc(d: Double): Double {
        return default + d
    }
}

val kodeinInjection = Kodein {
    bind<Calc>() with provider { AddInjection(instance()) }
    bind<Double>() with provider { 5.0 }
}

fun main() {
    val add by kodeinInjection.instance<Calc>()
    println("result = ${add.calc(1.0, 2.0)}")

    val addDefault by kodeinInjection.instance<Calc>()
    print("result ${add.calc(1.0)}")
}