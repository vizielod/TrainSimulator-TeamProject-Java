package map;

import graphics.Clickable;
import graphics.Drawable;
import javafx.util.Pair;
import logic.System_;
import movingelement.MovingElement;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public abstract class MapElement implements Clickable, Drawable {
    public String elementName;
    protected Point _pos;
    protected MovingElement _occupiedBy;
    protected ArrayList<Pair<MapElement, MapElement>> _neighbors = new ArrayList<>();

    public MapElement(String elementName, Point _pos, ArrayList<Pair<MapElement, MapElement>> _neighbors) {
        this.elementName = elementName;
        this._pos = _pos;
        this._neighbors = _neighbors;
    }

    public MapElement() {

    }

    /**
     * Függvény egy pozíció beállítására.
     *
     * @param p az új pozíció
     */
    public void setPoint(Point p) {
        _pos = p;
    }

    /**
     * Új szomszédpár hozzáadása a listához.
     *
     * @param p a hozzáadni kívánt pár
     */
    public void addNeighbor(Pair<MapElement, MapElement> p) {
        _neighbors.add(p);
    }

    /**
     * Függvény a következő elem visszaadására
     *
     * @param previousElement Előző elem, ennek függvényében kapja a következőt. Tkp. az egyik szomédja annak, amin most áll.
     * @return A következő elem
     */
    public MapElement getNext_(MapElement previousElement, MapElement currentElement) {
        if (previousElement == null) {
            for (MapElement me : System_.map.mapElements) {
                for (Pair<MapElement, MapElement> ne : me._neighbors) {
                    if (ne.getKey() == currentElement)
                        return me;
                    if (ne.getValue() == currentElement)
                        return me;
                }
            }
        } else {
            //Megnézzük az összes párra, hogy valamelyik eleme volt-e az előző, ha igen akkor visszatérünk a pár másik tagjával.
            MapElement next = null;

            if (this._neighbors.size() > 1 && this.getClass() == Rail.class) {
                for (Pair<MapElement, MapElement> ne : _neighbors) {
                    if (previousElement == ne.getKey())
                        return ne.getValue();
                    if (previousElement == ne.getValue())
                        return ne.getKey();
                }
            }

            for (Tunnel t : System_.map.tunnels)
                for (Rail tr : t._tunnelRails) {
                    if (this == tr) {
                        for (Pair<MapElement, MapElement> p : tr._neighbors) {
                            if (p.getKey() == previousElement)
                                return p.getValue();
                            if (p.getValue() == previousElement)
                                return p.getKey();
                        }
                    }
                }

            for (MapElement me : System_.map.mapElements) {
                for (Pair<MapElement, MapElement> ne : me._neighbors) {
                    if (ne.getKey() == currentElement)
                        if (me != previousElement)
                            return me;
                    if (ne.getValue() == currentElement)
                        if (me != previousElement)
                            return me;
                }
            }
        }

        return null;
    }

    /**
     * Egy olyan függvény, mellyel lekérdezhetjük, hogy mi által van éppen elfoglalva.
     *
     * @return Az elem, ami rajta áll.
     */
    public MovingElement getOccupied_() {
        //Visszatérünk a MapElement-et elfoglaló MovingElement-tel
        return _occupiedBy;
    }

    /**
     * Egy olyan függvény, melyet akkor hívunk ha rálépünk az elemre.
     *
     * @param currentElement A jelenlegi elem, ami rálépett.
     */
    public void setOccupied_(MovingElement currentElement) {
        if (_occupiedBy != null && currentElement != null) {
            currentElement.explode();
        } else {
            //A paraméterként kapott MovingElement-et állítjuk be aktuális elemként.
            _occupiedBy = currentElement;
        }

    }

    /**
     * Visszaadja egy elem pozícióját.
     *
     * @return a visszaadott pozíció
     */
    public Point getPosition() {
        return _pos;
    }

    /**
     * Visszaadja egy MapElement szomszédait
     *
     * @return az elem szomszédai
     */
    public ArrayList<Pair<MapElement, MapElement>> getNeighbors() {
        return _neighbors;
    }

    public int getImage() {
        try {
            if (_neighbors.size() == 1) {
                if (_neighbors.get(0).getKey() == null) {
                    if (_pos.getX() == _neighbors.get(0).getValue().getPosition().getX())
                        return 29;
                    return 30;
                } else if (_neighbors.get(0).getKey().getPosition().getX() == _neighbors.get(0).getValue().getPosition().getX()) {
                    if (elementName.contains("tunnel")) {
                        return 11;
                    }
                    return 29;
                } else if (_neighbors.get(0).getKey().getPosition().getY() == _neighbors.get(0).getValue().getPosition().getY()) {
                    if (elementName.contains("tunnel")) {
                        return 10;
                    }
                    return 30;
                } else if ((_pos.getY() == _neighbors.get(0).getKey().getPosition().getY()) && (_neighbors.get(0).getKey().getPosition().getY() < _neighbors.get(0).getValue().getPosition().getY())) {
                    return 6;
                } else if ((_pos.getX() == _neighbors.get(0).getKey().getPosition().getX()) && (_neighbors.get(0).getKey().getPosition().getY() < _neighbors.get(0).getValue().getPosition().getY())) {
                    return 8;
                } else if ((_pos.getY() == _neighbors.get(0).getKey().getPosition().getY()) && (_neighbors.get(0).getKey().getPosition().getY() > _neighbors.get(0).getValue().getPosition().getY())) {
                    return 9;
                } else if ((_pos.getX() == _neighbors.get(0).getKey().getPosition().getX()) && (_neighbors.get(0).getKey().getPosition().getY() > _neighbors.get(0).getValue().getPosition().getY())) {
                    return 7;
                }
            } else
                return 38;
        } catch (Exception e) {

        }
        return -1;
    }
}