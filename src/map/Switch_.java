package map;

import javafx.util.Pair;
import movingelement.MovingElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class Switch_ extends MapElement {

    //Melyik irányba áll éppen a switch
    private Pair<MapElement, MapElement> activePair;

    /**
     * Konstruktor, mely beállítja a megadott értékeket
     *
     * @param name      Neve
     * @param pos       Iránya
     * @param neighbors Szomszédai
     */
    public Switch_(String name, Point pos, ArrayList<Pair<MapElement, MapElement>> neighbors) {
        elementName = name;
        _pos = pos;
        int neightbourSize = neighbors.size();
        int r2 = new Random().nextInt(neightbourSize);
        activePair = neighbors.get(r2);
        _neighbors = neighbors;
    }

    /**
     * Paraméter nélküli konstruktor
     */
    public Switch_() {
    }

    public void setOccupied_(MovingElement currentElement) {
        if (currentElement != null) {
            if (activePair.getKey() != currentElement.previousElement && activePair.getValue() != currentElement.previousElement)
                currentElement.explode();
        } else
            super.setOccupied_(currentElement);

    }

    /**
     * A jelenlegi állás lekérdezése
     *
     * @return Az aktív pár
     */
    public Pair<MapElement, MapElement> getActivePair() {
        return activePair;
    }

    /**
     * Az aktív pár beállítása
     *
     * @param p A kívánt pár
     */
    public void setActivePair(Pair<MapElement, MapElement> p) {
        activePair = p;
    }

    /**
     * Váltás az egyik pozícióból a másikba
     */
    public void switch_() {
        for (int i = 0; i < _neighbors.size(); i++) {
            if ((_neighbors.get(i).getKey().elementName.equals(activePair.getKey().elementName) && _neighbors.get(i).getValue().elementName.equals(activePair.getValue().elementName)) ||
                    (_neighbors.get(i).getKey().elementName.equals(activePair.getValue().elementName) && _neighbors.get(i).getValue().elementName.equals(activePair.getKey().elementName))) {
                //Körkörösen vált!
                if (i == _neighbors.size() - 1) {
                    activePair = _neighbors.get(0);
                    return;
                } else {
                    activePair = _neighbors.get(i + 1);
                    return;
                }
            }
        }
    }

    /**
     * A következő elem visszaadása
     *
     * @param previousElement Előző elem, ennek függvényében kapja a következőt. Tkp. az egyik szomédja annak, amin most áll.
     * @param currentElement A jelenlegi elem.
     * @return A kért elem
     */
    @Override
    public MapElement getNext_(MapElement previousElement, MapElement currentElement) {
        if (previousElement == activePair.getKey()) {
            return activePair.getValue();
        } else if (previousElement == activePair.getValue()) {
            return activePair.getKey();
        } else
            return null;
    }

    /**
     * Kattintás kezelése
     */
    @Override
    public void click() {
        switch_();
    }

    /**
     * Kép visszadását végző függvény
     *
     * @return
     */
    @Override
    public int getImage() {
        if (activePair.getKey().getPosition().getX() == activePair.getValue().getPosition().getX()) {
            return 29;
        } else if (activePair.getKey().getPosition().getY() == activePair.getValue().getPosition().getY()) {
            return 30;
        } else if ((getPosition().getY() == activePair.getKey().getPosition().getY())
                &&
                (activePair.getKey().getPosition().getY() < activePair.getValue().getPosition().getY())) {
            return 6;
        } else if ((getPosition().getX() == activePair.getKey().getPosition().getX())
                &&
                (activePair.getKey().getPosition().getY() < activePair.getValue().getPosition().getY())) {
            return 8;
        } else if ((getPosition().getY() == activePair.getKey().getPosition().getY())
                &&
                (activePair.getKey().getPosition().getY() > activePair.getValue().getPosition().getY())) {
            return 9;
        } else if ((getPosition().getX() == activePair.getKey().getPosition().getX())
                &&
                (activePair.getKey().getPosition().getY() > activePair.getValue().getPosition().getY())) {
            return 7;
        }
        return -1;
    }

}