package map;

import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class Rail extends MapElement {

    /**
     * A rail paraméter nélküli konstruktora, meghívja az ősosztály konstruktorát.
     */
    public Rail() {
        super("Nevtelen sin", new Point(0, 0), new ArrayList<>());
    }

    /**
     * Rail paraméterezett konstruktora, megadhatunk egy sínt az összes adatával együtt
     *
     * @param name      a sín neve
     * @param pos       a sín pozíciója
     * @param neighbors a sín szomszédai
     */
    public Rail(String name, Point pos, ArrayList<Pair<MapElement, MapElement>> neighbors) {
        elementName = name;
        _pos = pos;
        _neighbors = neighbors;
    }

    /**
     * Kattintás kezelése. Jelenleg nem történik semmi
     */
    @Override
    public void click() {
    }
}