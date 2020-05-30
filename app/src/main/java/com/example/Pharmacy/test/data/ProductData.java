package com.example.Pharmacy.test.data;

import com.example.Pharmacy.TableColumn;
import com.xu.database.HardData;
import com.xu.database.Line;
import com.xu.database.MapData;

import java.util.Arrays;
import java.util.List;

/**
 * @since 11-07-2019
 * Truyen ve du lieu ban dau cho test
 */
public final class ProductData implements HardData {

    private final String column0 = TableColumn.ProductTable.ID;
    private final String column1 = TableColumn.ProductTable.NAME;

    private final List<Line> data = Arrays.asList(
            new Line(new MapData(column0, "10150"), new MapData(column1, "Paracetamol")),
            new Line(new MapData(column0, "10151"), new MapData(column1, "Morphine")),
            new Line(new MapData(column0, "10152"), new MapData(column1, "Methadone")),
            new Line(new MapData(column0, "10153"), new MapData(column1, "Amoxicillin")),
            new Line(new MapData(column0, "10154"), new MapData(column1, "Ampicillin")),
            new Line(new MapData(column0, "10155"), new MapData(column1, "Chloramphenicol")),
            new Line(new MapData(column0, "10156"), new MapData(column1, "Flucytosine")),
            new Line(new MapData(column0, "10157"), new MapData(column1, "Vitamin A")),
            new Line(new MapData(column0, "10158"), new MapData(column1, "Vitamin E")),
            new Line(new MapData(column0, "10159"), new MapData(column1, "Vitamin C"))
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
