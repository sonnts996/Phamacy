package com.example.Pharmacy.test.data;

import com.example.Pharmacy.TB_CategoriesManager;
import com.example.Pharmacy.TableColumn;
import com.xu.database.HardData;
import com.xu.database.Line;
import com.xu.database.MapData;

import java.util.Collections;
import java.util.List;

public final class CategoriesData implements HardData {

    private String column1 = TableColumn.CategoriesTable.CATEGORIES;

    private List<Line> data = Collections.singletonList(
            new Line(new MapData(column1, "Hàng thường"))
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
