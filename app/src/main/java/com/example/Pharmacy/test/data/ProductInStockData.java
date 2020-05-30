package com.example.Pharmacy.test.data;

import com.example.Pharmacy.TableColumn;
import com.xu.database.HardData;
import com.xu.database.Line;
import com.xu.database.MapData;

import java.util.Arrays;
import java.util.List;

public class ProductInStockData implements HardData {
    private final String[] column = {
            "pass",
            TableColumn.ProductInStockTable.PID,
            TableColumn.ProductInStockTable.TOTAL,
            TableColumn.ProductInStockTable.UNIT_ID,
            TableColumn.ProductInStockTable.WAREHOUSE_ID,
            TableColumn.ProductInStockTable.QUEUE_ID,
            TableColumn.ProductInStockTable.CATEGORIES_ID,
            TableColumn.ProductInStockTable.DATE,
            TableColumn.ProductInStockTable.PRICE
    };

    private final List<Line> data = Arrays.asList(
            new Line(new MapData(column[1], "10150"), new MapData(column[4], 2), new MapData(column[3], 1), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 360), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10151"), new MapData(column[4], 1), new MapData(column[3], 1), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 432), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10150"), new MapData(column[4], 2), new MapData(column[3], 3), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 4400), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10152"), new MapData(column[4], 3), new MapData(column[3], 5), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 2400), new MapData(column[7], "11-07-2019"), new MapData(column[8], 100000)),
            new Line(new MapData(column[1], "10153"), new MapData(column[4], 1), new MapData(column[3], 4), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 300), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10154"), new MapData(column[4], 2), new MapData(column[3], 1), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 180), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10150"), new MapData(column[4], 2), new MapData(column[3], 2), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 3600), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10155"), new MapData(column[4], 2), new MapData(column[3], 5), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 1300), new MapData(column[7], "11-07-2019"), new MapData(column[8], 50000)),
            new Line(new MapData(column[1], "10155"), new MapData(column[4], 1), new MapData(column[3], 6), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 320), new MapData(column[7], "11-07-2019"), new MapData(column[8], 75000)),
            new Line(new MapData(column[1], "10156"), new MapData(column[4], 1), new MapData(column[3], 1), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 2880), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10150"), new MapData(column[4], 3), new MapData(column[3], 4), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 500), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10157"), new MapData(column[4], 3), new MapData(column[3], 3), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 0), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10150"), new MapData(column[4], 2), new MapData(column[3], 2), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 4680), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10158"), new MapData(column[4], 1), new MapData(column[3], 1), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 1620), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10159"), new MapData(column[4], 2), new MapData(column[3], 3), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 1000), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10159"), new MapData(column[4], 3), new MapData(column[3], 4), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 1020), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10151"), new MapData(column[4], 3), new MapData(column[3], 3), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 700), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10152"), new MapData(column[4], 1), new MapData(column[3], 5), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 800), new MapData(column[7], "11-07-2019"), new MapData(column[8], 50000)),
            new Line(new MapData(column[1], "10152"), new MapData(column[4], 1), new MapData(column[3], 5), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 100), new MapData(column[7], "11-07-2019"), new MapData(column[8], 50000)),
            new Line(new MapData(column[1], "10153"), new MapData(column[4], 1), new MapData(column[3], 2), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 6120), new MapData(column[7], "11-07-2019"), new MapData(column[8], 500)),
            new Line(new MapData(column[1], "10155"), new MapData(column[4], 1), new MapData(column[3], 6), new MapData(column[5], 1), new MapData(column[6], 1), new MapData(column[2], 70), new MapData(column[7], "11-07-2019"), new MapData(column[8], 86000))
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
