package com.example.Pharmacy.test.data;

import com.example.Pharmacy.TableColumn;
import com.xu.database.HardData;
import com.xu.database.Line;
import com.xu.database.MapData;

import java.util.Collections;
import java.util.List;

public class QueueData implements HardData {
    private String column1 = TableColumn.QueueTable.QUEUE;

    private List<Line> data = Collections.singletonList(
            new Line(new MapData(column1, "Hàng chờ"))
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
