package com.example.projet

import java.lang.reflect.Constructor

class Model {
    lateinit var title: String
    lateinit var desc: String
    val photo : Int

    constructor(a: String, b: String,c: Int) {
        this.title = a
        this.desc = b
        this.photo = c
    }
    public fun gettitle() : String {
        return this.title
    }
}