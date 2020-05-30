package com.xu.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Line {

    private List<MapData> line = new ArrayList<>();

    public Line(MapData... mapData){
        Collections.addAll(line, mapData);
    }

    public List<MapData> get() {
        return line;
    }
}
