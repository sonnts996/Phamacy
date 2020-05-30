package com.example.Pharmacy.test.data;

import com.example.Pharmacy.TableColumn;
import com.xu.database.HardData;
import com.xu.database.Line;
import com.xu.database.MapData;

import java.util.Collections;
import java.util.List;

public class AccountData implements HardData {
    private String column1 = TableColumn.AccountTable.USERNAME;
    private String column2 = TableColumn.AccountTable.PASSWORD;

    private List<Line> data = Collections.singletonList(
            new Line(new MapData(column1, "admin"), new MapData(column2, "0000"))
    );

    @Override
    public List<Line> getData() {
        return data;
    }

    @Override
    public Line[] getDataToArray() {
        return (Line[]) data.toArray();
    }
}
