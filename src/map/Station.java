package map;

import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class Station extends MapElement {

    private Color _color;
    private Boolean empty;

    public  Station(){}

    /**
     * Állomás paraméteres konstruktora
     * @param name állomás neve
     * @param pos állomás pozíciója
     * @param neighbors állomás szomszédai
     * @param color állomás színe
     */
    public Station(String name, Point pos, ArrayList<Pair<MapElement, MapElement>> neighbors, Color color) {
        elementName = name;
        _pos = pos;
        _neighbors = neighbors;
        _color = color;
        empty = true;
    }

    /**
     * Visszaadja, hogy üres-e az állomás
     * @return ha üres az állomás akkor true, egyébként false
     */
    public Boolean getEmpty() {
        return empty;
    }

    /**
     * Beállíthatjuk egy állomásról, hogy üres-e vagy sem.
     * @param b a beállítani kívánt érték
     */
    public void setEmpty(Boolean b) {
        empty = b;
    }

    /**
     * Visszaadja egy állomás színét.
     * @return az állomás színe
     */
    public Color getColor() {
        return _color;
    }

    /**
     * Visszatér egy szín nevével stringként. Ez a fájlba való kimentéshez kell.
     * @return a szín neve stringként
     */
    public String getColorString() {
        String colorName = null;
        if (Color.BLACK.equals(_color)) {
            colorName = "BLACK";
        }
        else if (Color.GREEN.equals(_color)) {
            colorName = "GREEN";
        }
        else if (Color.RED.equals(_color)) {
            colorName = "RED";
        }
        else if (Color.BLUE.equals(_color)) {
            colorName = "BLUE";
        }
        else if (Color.YELLOW.equals(_color)) {
            colorName = "YELLOW";
        }
        return colorName;
    }

    /**
     * Stringből színt hoz létre a visszatöltéshez.
     * @param color a szín amit be szeretnénk állítani
     */
    public void setColorString(String color) {
        if (color.equals("BLACK")) {
            _color = Color.BLACK;
        }
        else if (color.equals("GREEN")) {
            _color = Color.GREEN;
        }
        else if (color.equals("RED")) {
            _color = Color.RED;
        }
        else if (color.equals("BLUE")) {
            _color = Color.BLUE;
        }
        else if (color.equals("YELLOW")) {
            _color = Color.YELLOW;
        }
    }

    /**
     * Egy állomás adatinak kiírása.
     */
    public void printDetails() {
        System.out.println("<" + this.elementName + "><" + (int)this._pos.getX() + "," + (int)this._pos.getY() + "><" + getColorString() + "><" + empty.toString() + ">");
    }

    /**
     * Kattintás kezelése
     */
    @Override
    public void click() {
    }
    
    public int getImage() {
        if (_neighbors.get(0).getKey().getPosition().getX() == _neighbors.get(0).getValue().getPosition().getX()) {
            switch (getColorString()) {
                case "BLUE":
                    if (empty) {
                        return 20;
                    }
                    return 24;
                case "YELLOW":
                    if (empty) {
                        return 22;
                    }
                    return 26;
                case "RED":
                    if (empty) {
                        return 23;
                    }
                    return 25;
                case "GREEN":
                    if (empty) {
                        return 28;
                    }
                    return 28;
                default:
                    break;
            }
        } else if (_neighbors.get(0).getKey().getPosition().getY() == _neighbors.get(0).getValue().getPosition().getY()) {
            switch (getColorString()) {
                case "BLUE":
                    if (empty) {
                        return 19;
                    }
                    return 15;
                case "YELLOW":
                    if (empty) {
                        return 16;
                    }
                    return 12;
                case "RED":
                    if (empty) {
                        return 17;
                    }
                    return 13;
                case "GREEN":
                    if (empty) {
                        return 18;
                    }
                    return 14;
                default:
                    break;
            }
        }
        return -1;
    }
}
