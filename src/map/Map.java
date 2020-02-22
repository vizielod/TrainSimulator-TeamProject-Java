package map;

import filemgmt.FileManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class Map {
    public ArrayList<MapElement> mapElements = new ArrayList<>();
    public ArrayList<MapElement> entrancePoints;
    public ArrayList<Tunnel> tunnels = new ArrayList<>();
    private Tunnel _enabledTunnel;
    private FileManager _fm;

    public FileManager getFileManager() {
        return _fm;
    }

    /**
     * Map osztály konstruktora. A Játék elindításakor inicializálja a Map-ot, betölti a pályát egy fájlból, majd sorba létrehozza a pályaelemeket: Rail, Switch, TunnelEntrance, Station.
     * Mivel ez csak egy példaprogram, hogy szemléltesse az osztály működését, ezért elemenként csak egy példányt hozunk étre, de valójában sokkal több elemből fog állni a pálya
     */
    public Map(ArrayList<MapElement> me, ArrayList<MapElement> ep, ArrayList<Tunnel> t, FileManager fm) {
        mapElements = me;
        entrancePoints = ep;
        tunnels = t;
        int r = new Random().nextInt(t.size());
        _enabledTunnel = t.get(r);
    }

    public void setEnabledTunnel(Tunnel t) {
        _enabledTunnel = t;
    }

    public void setFileManager(FileManager f) {
        _fm = f;
    }

    public Map() {
        mapElements = new ArrayList<>();
        entrancePoints = new ArrayList<>();
        tunnels = new ArrayList<>();
        _enabledTunnel = null;
        _fm = new FileManager();
    }

    public Tunnel findTunnel (TunnelEntrance te){
        return null;
    }

    /**
     * Alagút engedélyezése
     *
     * @param elementName Az engedélyezett alagút neve
     */
    public void enableTunnel(String elementName) {
        if (_enabledTunnel != null) {
            if (_enabledTunnel.disable()) {
                for (Tunnel t : tunnels) {
                    if (t.tunnelName.equals(elementName)) {
                        _enabledTunnel = t;
                    }
                }
                _enabledTunnel.enable();
            }
        } else {
            for (Tunnel t : tunnels) {
                if (t.tunnelName.equals(elementName)) {
                    _enabledTunnel = t;
                    _enabledTunnel.enable();
                }
                }
            }
    }

    /**
     * Engedélyezett alagút lekérése
     *
     * @return
     */
    public Tunnel getEnabledTunnel() {
        return _enabledTunnel;
    }

    /**
     * Az adott railhez tartozó alagút kikeresése
     * @param tr Vizsgált rail
     * @return Az esetlegesen hozzá tartozó tunnel
     */
    public Tunnel getTunnelFromRail(MapElement tr) {
        for (int i = 0; i < tunnels.size(); i++) {
            if (tunnels.get(i)._tunnelRails.contains(tr)) {
                return tunnels.get(i);
            }
        }
        return null;
    }

    /**
     * Elem lekérése egy megadott koordinátából
     *
     * @param x Az X koordináta
     * @param y Az Y koordináta
     * @return
     */
    public MapElement getElementAt(int x, int y) {
        for (Tunnel t : tunnels) {
            for (Rail tr : t._tunnelRails) {
                if ((tr.getPosition().getX() == x) && (tr.getPosition().getY() == y)) {
                    return tr;
                }
            }

        }

        for (MapElement me : mapElements) {
            if ((me.getPosition().getX() == x) && (me.getPosition().getY() == y)) {
                return me;
            }

        }
        return null;
    }

}
