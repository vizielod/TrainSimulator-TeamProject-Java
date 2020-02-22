package map;

import javafx.util.Pair;
import logic.System_;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class TunnelEntrance extends MapElement {

    /**
     * A TunnelEntrance paraméteres konstruktora
     *
     * @param name      tunnelentrance neve
     * @param pos       tunnelentrance pozíciója
     * @param neighbors tunnelentrance szomszédai
     */
    public TunnelEntrance(String name, Point pos, ArrayList<Pair<MapElement, MapElement>> neighbors) {
        elementName = name;
        _pos = pos;
        _neighbors = neighbors;
    }

    /**
     * Paraméter nélküli konstruktor
     */
    public TunnelEntrance() {
    }

    /**
     * Kattintás kezelése
     */
    @Override
    public void click() {
    }

    /**
     * KÖövetkező elem lekérése
     *
     * @param previousElement Előző elem, ennek függvényében kapja a következőt. Tkp. az egyik szomédja annak, amin most áll.
     * @param currentElement  Az elem, amin jelenleg áll.
     * @return A következő elem.
     */
    @Override
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
            for (TunnelEntrance te : System_.map.getEnabledTunnel().getExitPoints()) {
                if (currentElement == te) {
                    for (Rail tunnelRail : System_.map.getEnabledTunnel().getTunnelRails()) {
                        if (System_.map.getEnabledTunnel()._tunnelRails.contains(previousElement)) {
                            break; //Ha már a tunnelből jön akkor ne menjen oda!
                        }
                        if (tunnelRail.getNeighbors().get(0).getKey().equals(currentElement) || tunnelRail.getNeighbors().get(0).getValue().equals(currentElement))
                            return tunnelRail;
                    }
                }
            }
            for (MapElement me : System_.map.mapElements) {
                for (Pair<MapElement, MapElement> ne : me._neighbors) {
                    if (ne.getKey() == currentElement || ne.getValue() == currentElement)
                        if (me != previousElement) {
                            boolean tr = false;
                            for (Tunnel t : System_.map.tunnels)
                                for (Rail tunnelRail : t.getTunnelRails())
                                    if (tunnelRail == me)
                                        tr = true;
                            if (!tr)
                                return me;
                        }
                }
            }
        }
        return null;
    }

}
