package com.example.Pharmacy.test.data;

import com.example.Pharmacy.TableColumn;
import com.xu.database.HardData;
import com.xu.database.Line;
import com.xu.database.MapData;

import java.util.Arrays;
import java.util.List;

public class WarehouseData implements HardData {
    private String column1 = TableColumn.WarehouseTable.WAREHOUSE;

    private List<Line> data = Arrays.asList(
            new Line(new MapData(column1, "Kho hàng chờ 1")),
            new Line(new MapData(column1, "Kho hàng chờ 2")),
            new Line(new MapData(column1, "Kho hàng chờ 3"))
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
