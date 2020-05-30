package com.xu.database

import java.util.ArrayList
import java.util.Collections

class Line(vararg mapData: MapData) {

    private val line = ArrayList<MapData>()

    init {
        Collections.addAll(line, *mapData)
    }

    fun get(): List<MapData> {
        return line
    }
}
