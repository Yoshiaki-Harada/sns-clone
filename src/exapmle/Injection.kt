package com.example.exapmle

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class AddInjection(private val default: Double) : Calc {
    override fun calc(d1: Double, d2: Double): Double {
        return d1 + d2
    }

    override fun calcDefault(d: Double): Double {
        return default + d
    }
}

val kodeinInjection = Kodein {
    bind<Calc>(tag="add1") with provider { AddInjection(instance(tag="add1")) }
    bind<Double>(tag="add1") with provider { 5.0 }
    bind<Calc>() with provider { AddInjection(instance(tag="add2")) }
    bind<Calc>(tag="add2") with provider { AddInjection(instance(tag="add2")) }
    bind<Double>(tag="add2") with provider { 1.0 }
}

fun main() {
    val add1 by kodeinInjection.instance<Calc>(tag = "add1")
    val add2 by kodeinInjection.instance<Calc>(tag = "add2")
    println("result ${add1.calcDefault(1.0)}")
    println("result ${add2.calcDefault(1.0)}")
}