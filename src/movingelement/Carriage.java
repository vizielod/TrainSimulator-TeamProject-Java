package movingelement;

import map.MapElement;

import java.awt.*;
import java.util.Random;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class Carriage extends MovingElement {
    /**
     * szín eltárolása*/
    protected Color _color;
    /**
     * annak eltárolása, hogy a moving element üres-e*/
    protected Boolean _empty;

    /**
    * Carriage konstruktora
     * minden értéket a praméterként kapottak alapján állít be
     */
    public Carriage(Color color, Boolean empty, String name, MapElement _currentElement, MapElement _previousElement) {
        _color = color;
        _empty = empty;
        elementName = name;
        previousElement = _previousElement;
        currentElement = _currentElement;
    }

    /**
    * Carriage konstruktora
    * a színt és ürességet random állítja be
    * @param name, _currentElementm _previousElement
    * */
    public Carriage(String name, MapElement _currentElement, MapElement _previousElement) {
        /**
        * szín véletlen meghatározása, random szám 0-4-ig
        * */
        int r = new Random().nextInt(5);
        switch (r) {
            case 0:
                _color = Color.BLACK;
                break;
            case 1:
                _color = Color.RED;
                break;
            case 2:
                _color = Color.GREEN;
                break;
            case 3:
                _color = Color.BLUE;
                break;
            case 4:
                _color = Color.YELLOW;
                break;
        }
        /**
        * üresség véletlen meghatározása
        * */
        int r2 = new Random().nextInt(2);
        if (r2 == 0) {
            _empty = false;
        } else {
            _empty = true;
        }

        /**
        * paraméterként kapott értékek alapján állítja be
        * */
        elementName = name;
        previousElement = _previousElement;
        currentElement = _currentElement;
        _pos = new Point(currentElement.getPosition());
    }

    public Carriage(){}

    /**
    * a szín átalakítása stringgé, a stringgel térünk vissza
    *
    * @return String szín
    * */
    public String getColorString() {
        String colorName = null;
        if (Color.BLACK.equals(_color)) {
            colorName = "BLACK";
        } else if (Color.GREEN.equals(_color)) {
            colorName = "GREEN";
        } else if (Color.BLUE.equals(_color)) {
            colorName = "BLUE";
        } else if (Color.RED.equals(_color)) {
            colorName = "RED";
        } else if (Color.YELLOW.equals(_color)) {
            colorName = "YELLOW";
        }
        return colorName;
    }


    public void setColorString(String color) {
        if (color.equals("BLACK")) {
            _color = Color.BLACK;
        } else if (color.equals("GREEN")) {
            _color = Color.GREEN;
        } else if (color.equals("RED")) {
            _color = Color.RED;
        } else if (color.equals("BLUE")) {
            _color = Color.BLUE;
        } else if (color.equals("YELLOW")) {
            _color = Color.YELLOW;
        }
    }

    /**
     * Visszaadja a kocsi színét.
     * @return a kocsi színe
     */
    public Color getColor() {
        return _color;
    }

    public void setColor(Color newColor) {
        _color = newColor;
    }

    /**
     * Visszatér azzal, hogy üres-e a kocsi.
     * @return üres-e a kocsi
     */
    public Boolean getEmpty() {
        return _empty;
    }

    /**
     * A paraméterként kapott értéknek megfelelően beállítja, hogy üres-e a kocsi.
     *
     * @param empty milyen érékre akarjuk állítan a kocsi ürességét
     */
    public void setEmpty(Boolean empty) {
        _empty = empty;
    }

    /**
     *"Eltünteti" a kocsit.
     */
    @Override
    public void vanish() {
        currentElement.setOccupied_(null);
        previousElement.setOccupied_(null);
    }

    /**
     * "Felrobbantja" a kocsit.
     */
    @Override
    public void explode() {
        if (currentElement != null)
            currentElement.setOccupied_(null);
    }

    /**
    * az elem nevének, pozíciójának, színének, ürességének
    * kiírása a standard kimenetre
    */
    public void printDetails() {
        System.out.println("<" + this.elementName + "><" + (int)this._pos.getX() + "," + (int)this._pos.getY() + "><" + getColorString() + "><" + _empty.toString()+">");
    }

    /**
     * Kép visszaadása
     *
     * @return
     */
    @Override
    public int getImage() {

        if (_empty) {
            if (currentElement.getNeighbors().get(0).getKey() != null
                    && currentElement.getNeighbors().get(0).getValue() != null
                    && currentElement.getNeighbors().get(0).getKey().getPosition().getX() == currentElement.getNeighbors().get(0).getValue().getPosition().getX())
                return 37;
            return 36;
        }

        switch (getColorString()) {
            case "BLUE":
                try {
                    if (currentElement.getNeighbors().get(0).getKey() != null
                            && currentElement.getNeighbors().get(0).getValue() != null
                            && currentElement.getNeighbors().get(0).getKey().getPosition().getX() == currentElement.getNeighbors().get(0).getValue().getPosition().getX())
                        return 5;
                    else
                        return 34;
                } catch (Exception es) {
                }
            case "GREEN":
                if (currentElement.getNeighbors().get(0).getKey() != null
                        && currentElement.getNeighbors().get(0).getValue() != null
                        && currentElement.getNeighbors().get(0).getKey().getPosition().getX() == currentElement.getNeighbors().get(0).getValue().getPosition().getX()) {
                    return 4;
                } else {
                    return 33;
                }
            case "YELLOW":
                if (currentElement.getNeighbors().get(0).getKey() != null
                        && currentElement.getNeighbors().get(0).getValue() != null
                        && currentElement.getNeighbors().get(0).getKey().getPosition().getX() == currentElement.getNeighbors().get(0).getValue().getPosition().getX()) {
                    return 1;
                } else {
                    return 21;
                }
            case "RED":
                if (currentElement.getNeighbors().get(0).getKey() != null
                        && currentElement.getNeighbors().get(0).getValue() != null
                        && currentElement.getNeighbors().get(0).getKey().getPosition().getX() == currentElement.getNeighbors().get(0).getValue().getPosition().getX()) {
                    return 3;
                } else {
                    return 35;
                }
            case "BLACK":
                if (currentElement.getNeighbors().get(0).getKey() != null
                        && currentElement.getNeighbors().get(0).getValue() != null
                        && currentElement.getNeighbors().get(0).getKey().getPosition().getX() == currentElement.getNeighbors().get(0).getValue().getPosition().getX()) {
                    return 2;
                } else {
                    return 32;
                }
        }
        return -1;
    }
}