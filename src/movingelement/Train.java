package movingelement;

import logic.Player;
import logic.System_;
import map.MapElement;
import map.Station;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class Train extends MovingElement  {
    public ArrayList<Carriage> _carts = new ArrayList<>();
    private static ArrayList<Train> toRemove = new ArrayList<>();

    /**
     * Konstruktor
     *
     * @param elementName     Neve
     * @param currentElement  Jelenlegi elem
     * @param previousElement Előző elem
     * @param x               X koordináta
     * @param y               Y koordináta
     */
    public Train(String elementName, MapElement currentElement, MapElement previousElement, int x, int y) {
        this.elementName = elementName;
        this.currentElement = currentElement;
        this.previousElement = previousElement;
        _pos = new Point(x, y);
    }

    /**
     * Paraméter nélküli konstruktora
     */
    public Train() {
        super();
    }

    /**
     *
     */
    public void nextStep() {
        try {
            if (!toRemove.contains(this)) {
                super.nextStep();
            }
            if (!toRemove.contains(this)) {
                for (Carriage carriage : _carts) {
                    carriage.nextStep();
                }
            }
        } catch (NullPointerException e) {}
    }

    /**
     * A vonat callback függvénye. Egy állomás akkor hívja meg, ha rá vonat érkezett.
     * Az utasok leszállítását végzi, illetve ha arra szükség van (a vonat kiürült), meghívja saját maga vanish() függvényét.
     *
     * @param station
     */
    @Override
    public void atStation(Station station) {
        Color stationColor = station.getColor();
        Carriage firstFullCarriage = null;
        Boolean found = false;

        //első teljes kocsi megkeresése
        for (Carriage c : _carts) {
            Color color = c.getColor();
            if (color != Color.BLACK && !found) {
                if (!c.getEmpty()) {
                    firstFullCarriage = c;
                    found = true;
                }
            }
        }

        //felszállás megvalósítása
        if (firstFullCarriage != null && !station.getEmpty()) {
            Color carriageColor = firstFullCarriage.getColor();
            Boolean stationEmpty = false;
            for (Carriage c : _carts) {
                if (c.getEmpty()) {
                    if (c.getColor() == stationColor) {
                        c.setEmpty(false);
                        stationEmpty = true;
                    }
                }
            }
            if (stationEmpty) {
                station.setEmpty(true);
            }

        }
        if (station.getColor() == firstFullCarriage.getColor()) {
            firstFullCarriage.setEmpty(true);
            station.setEmpty(false);
        }


        Boolean isEveryCarriageEmpty = true;
        for (Carriage c : _carts) {
            if (c.getColor() != Color.BLACK) {
                if (!c.getEmpty()) {
                    isEveryCarriageEmpty = false;
                }
            }
        }
        if (isEveryCarriageEmpty) {
            this.vanish();
        }

        if (System_.trains.size() - toRemove.size() == 0)
            Player.increaseLevel();

    }

    public static void removeTrain() {
        System_.trains.removeAll(toRemove);
        toRemove = new ArrayList<>();
    }

    /**
     * Ha a vonat minden kocsija kiürült, akkor a vonat eltűnik. Ennek a szituációnak a lejezelésére szolgál ez a függvény.
     */
    @Override
    public void vanish() {

        for (Carriage c : _carts) {
            c.vanish();
        }
        this.currentElement.setOccupied_(null);
        this.previousElement.setOccupied_(null);
        toRemove.add(this);

    }

    /**
     * Ha vonatok ütköznek, vagy rosszul áll egy váltó, akkor a vonat felrobban. Ennek megvalósítására szolgál ez a függvény.
     */
    @Override
    public void explode() {
        for (Carriage c : _carts) {
            c.explode();
        }
        System_.gameOver();

    }

    /**
    * a vonat nevének, pozíciójának kiírása
    * meghívjuk az összes kocsinak is a kiírását
    * */
    public void printDetails() {
        System.out.println("<" + elementName + "><" + (int) _pos.getX() + "," + (int) _pos.getY() + ">");
        for (Carriage c : _carts) {
            c.printDetails();
        }
    }

    /**
     * Kép visszaadása
     * @return
     */
    @Override
    public int getImage() {
        if (currentElement.getNeighbors().get(0).getKey() != null
                && currentElement.getNeighbors().get(0).getValue() != null
                && currentElement.getNeighbors().get(0).getKey().getPosition().getX() == currentElement.getNeighbors().get(0).getValue().getPosition().getX()) {
            return 0;
        } else {
            return 31;
        }
    }
}
