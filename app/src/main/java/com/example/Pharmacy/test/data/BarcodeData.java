package com.example.Pharmacy.test.data;

import com.example.Pharmacy.TB_BarcodeManager;
import com.example.Pharmacy.TableColumn;
import com.xu.database.HardData;
import com.xu.database.Line;
import com.xu.database.MapData;

import java.util.Arrays;
import java.util.List;

public class BarcodeData implements HardData {
    private String column1 = TableColumn.BarCodeTable.BARCODE;
    private String column2 = TableColumn.BarCodeTable.PID;

    private List<Line> data = Arrays.asList(
            new Line(new MapData(column2, "10150"), new MapData(column1, "AAAABBBB")),
            new Line(new MapData(column2, "10159"), new MapData(column1, "12345566")),
            new Line(new MapData(column2, "10155"), new MapData(column1, "37333333")),
            new Line(new MapData(column2, "10155"), new MapData(column1, "48557244")),
            new Line(new MapData(column2, "10150"), new MapData(column1, "456786343")),
            new Line(new MapData(column2, "10154"), new MapData(column1, "456788887")),
            new Line(new MapData(column2, "10153"), new MapData(column1, "1235678678")),
            new Line(new MapData(column2, "10152"), new MapData(column1, "444566544")),
            new Line(new MapData(column2, "10150"), new MapData(column1, "788777777")),
            new Line(new MapData(column2, "10157"), new MapData(column1, "4545674456"))
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
