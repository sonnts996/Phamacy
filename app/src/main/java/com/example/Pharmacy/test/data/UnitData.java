package com.example.Pharmacy.test.data;

import com.example.Pharmacy.TableColumn;
import com.xu.database.HardData;
import com.xu.database.Line;
import com.xu.database.MapData;

import java.util.Arrays;
import java.util.List;

public class UnitData implements HardData {

    private final String column1 = TableColumn.UnitTable.UNIT;
    private final String column2 = TableColumn.UnitTable.UNIT2;
    private final String column3 = TableColumn.UnitTable.FACTOR2;
    private final String column4 = TableColumn.UnitTable.UNIT3;
    private final String column5 = TableColumn.UnitTable.FACTOR3;

    private final List<Line> data = Arrays.asList(
            // đơn vị chuẩn
            new Line(new MapData(column1, "Viên"), new MapData(column2, "Vĩ"), new MapData(column3, 6), new MapData(column4, "Hộp"), new MapData(column5, 36)),
            new Line(new MapData(column1, "Viên"), new MapData(column2, "Vĩ"), new MapData(column3, 12), new MapData(column4, "Hộp"), new MapData(column5, 72)),
            new Line(new MapData(column1, "Viên"), new MapData(column2, "Lọ"), new MapData(column3, 30), new MapData(column4, "Hộp"), new MapData(column5, 30)),
            new Line(new MapData(column1, "Viên"), new MapData(column2, "Lọ"), new MapData(column3, 100), new MapData(column4, "Hộp"), new MapData(column5, 100)),
            // don vi thuoc nuoc (Dung dich)
            new Line(new MapData(column1, "Lọ"), new MapData(column2, "ml"), new MapData(column3, 100), new MapData(column4, "Hộp"), new MapData(column5, 1)),
            new Line(new MapData(column1, "Lọ"), new MapData(column2, "ml"), new MapData(column3, 250), new MapData(column4, "Hộp"), new MapData(column5, 1))
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
