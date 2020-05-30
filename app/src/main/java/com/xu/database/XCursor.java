package com.xu.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XCursor extends CursorWrapper {

    private List<XData> hashMapList;
    private int index = -1;

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public XCursor(Cursor cursor) {
        super(cursor);
        this.hashMapList = convertToHashMap();
    }

    private List<XData> convertToHashMap() {
        List<XData> list = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            XData hashMap = new XData();
            for (int i = 0; i < getColumnCount(); i++) {
                hashMap.put(getColumnName(i), getString(i));
            }
            list.add(hashMap);
            moveToNext();
        }
        return list;
    }


    public Cursor getCursor() {
        return this;
    }

    public XData getFirstLine() {
        return hashMapList.get(0);
    }

    public XData getLastLine() {
        return hashMapList.get(hashMapList.size() - 1);
    }

    public XData getLine(int index) {
        return hashMapList.get(index);
    }

    public XData getLine() {
        return hashMapList.get(this.index);
    }

    public void moveToFirstLine() {
        this.index = 0;
    }

    public void moveToLastLine() {
        this.index = hashMapList.size() - 1;
    }

    public void moveToNextLine() {
        this.index++;
        if (this.index >= hashMapList.size()) this.index = hashMapList.size();
    }

    public int getLineIndex() {
        return index;
    }

    public boolean isAfterLastLine() {
        return this.index == hashMapList.size();
    }

    public boolean isBeforeFisrtLine() {
        return this.index == -1;
    }


    public class XData extends HashMap<String, String> {

        public void put(int key, int value) {
            String k, v;
            k = String.valueOf(key);
            v = String.valueOf(value);
            this.put(k, v);
        }

        public void put(String key, int value) {
            String v;
            v = String.valueOf(value);
            this.put(key, v);
        }

        public void put(int key, String value) {
            String k;
            k = String.valueOf(key);
            this.put(k, value);
        }

        public String getString(String key) {
            String rs = this.get(key);
            if (rs == null) throw new RuntimeException("Not found value: " + key);
            return rs;
        }

        public String getString(int key) {
            String inx;
            inx = String.valueOf(key);
            String rs = this.get(inx);
            if (rs == null) throw new RuntimeException("Not found value: " + inx);
            return rs;
        }

        public int getInt(String key) {
            String rs = this.get(key);
            if (rs != null) {
                try {
                    return Integer.valueOf(rs);
                } catch (Exception ex) {
                    throw new RuntimeException("Value not a number: " + rs);
                }
            } else {
                throw new RuntimeException("Not found value: " + key);
            }
        }

        public int getInt(int key) {
            String inx;
            inx = String.valueOf(key);
            String rs = this.get(inx);
            if (rs != null) {
                try {
                    return Integer.valueOf(rs);
                } catch (Exception ex) {
                    throw new RuntimeException("Value not a number: " + rs);
                }
            } else {
                throw new RuntimeException("Not found value: " + key);
            }
        }

        public void remove(int key) {
            String k;
            k = String.valueOf(key);
            this.remove(k);
        }
    }
}
