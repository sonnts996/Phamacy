package com.xu.database

data class MapData (val key: String,var value: String) {
    constructor(key: String, value: Int) : this(key, value.toString())

    fun toArray(): Array<String?> {
        return arrayOf(key, value)
    }

    override fun toString(): String {
        return "$key>$value"
    }
}
