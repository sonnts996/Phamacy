package com.xu.database;

public class MapData {
    private String key;
    private String value;

    public MapData(String key, String value){
        this.key = key;
        this.value = value;
    }

    public MapData(String key, int value){
        this.key = key;
        this.value = String.valueOf(value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String[] toArray(){
        return new String[] {key, value};
    }

    @Override
    public String toString(){
        return key + ">" + value;
    }
}
