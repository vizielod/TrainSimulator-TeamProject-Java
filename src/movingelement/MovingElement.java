package movingelement;

import graphics.Drawable;
import logic.System_;
import map.MapElement;
import map.Station;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public abstract class MovingElement implements Drawable {
    public String elementName;
    public MapElement currentElement;
    public MapElement previousElement;
    protected Point _pos;

    public void atStation(Station station) {
    }

    public void explode() {
        System_.gameOver();
    }

    public void vanish() {
        throw new NotImplementedException();
    }

    /**
    * beállítja a paraméterként átvett pontot
    * */
    public void setPoint(Point p) {
        _pos = p;
    }

    /**
    * visszaadja a pozíciót
    * */
    public Point getPosition() {
        return _pos;
    }

    /**
     * A léptetést kezeli.
     */
    public void nextStep() throws NullPointerException {
        if (_pos.getX() < 0) {
            _pos = new Point((int) (_pos.getX() + 1), (int) _pos.getY());
            return;
        }
        if (_pos.getX() > 12) {
            _pos = new Point((int) (_pos.getX() - 1), (int) _pos.getY());
            return;
        }
        if (_pos.getX() >= 0 && _pos.getY() >= 0) {

            currentElement = System_.map.getElementAt((int) (_pos.getX()), (int) _pos.getY());

            MapElement temp = currentElement;
            //Lekérjük a következő elemet.
            currentElement = currentElement.getNext_(previousElement, currentElement);

            //Ha a következő elem null (ami akkor lehet, ha már állnak rajta), akkor a vonat felrobban.
            if (currentElement == null)
                explode();
            else {
                //Az előző elemről lelépünk.
                previousElement = temp;
                previousElement.setOccupied_(null);
                //És rálépünk a következő elemre.
                    currentElement.setOccupied_(this);

                _pos = currentElement.getPosition();
            }
        }

        if (currentElement.getClass().equals(Station.class))
            atStation((Station)currentElement);
    }

    public abstract void printDetails();

}
