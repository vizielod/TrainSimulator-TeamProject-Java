package map;

import graphics.Clickable;
import logic.System_;

import java.util.ArrayList;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class Tunnel implements Clickable {
    public String tunnelName;
    private ArrayList<TunnelEntrance> _exitPoints = new ArrayList<>();
    public ArrayList<Rail> _tunnelRails = new ArrayList<>();
    private Boolean _enabled;

    /**
     * Konstruktor, mely átveszi a tunnel összes adatát
     *
     * @param tunnelName   Neve
     * @param _exitPoints  Kimenő pontjai
     * @param _tunnelRails Sínjei
     * @param _enabled     Engedélyezett-e
     */
    public Tunnel(String tunnelName, ArrayList<TunnelEntrance> _exitPoints, ArrayList<Rail> _tunnelRails, Boolean _enabled) {
        this.tunnelName = tunnelName;
        this._exitPoints = _exitPoints;
        this._tunnelRails = _tunnelRails;
        this._enabled = _enabled;
    }

    /**
     * Paraméter nélküli konstruktor
     */
    public Tunnel() {
    }

    /**
     * Tunnel bejárat hozzáadása
     * @param te Hozzáadni kívánt bejárat
     */
    public void addTunnelEntrance(TunnelEntrance te) {
        _exitPoints.add(te);
    }

    /**
     * Tunnelben lévő sín hozzáadása
     * @param r Hozzáadni kívánt sín
     */
    public void addTunnelRail(Rail r) {
        _tunnelRails.add(r);
    }

    /**
     * Kilépési pontjai a tunnelnek
     * @return Exit pontok
     */
    public ArrayList<TunnelEntrance> getExitPoints() {
        return _exitPoints;
    }

    /**
     * Tunnel sínei
     * @return Sínek
     */
    public ArrayList<Rail> getTunnelRails() {
        return _tunnelRails;
    }

    /**
     * Engedélyezett-e már?
     * @return Engedélyezve van-e
     */
    public Boolean getEnabled() {
        return _enabled;
    }

    /**
     * Letiltja az alagutat.
     * @return sikeres-e a letiltás.
     */
    public Boolean disable() {
            //Azt tárolja, hogy üres-e az alagút.
            Boolean empty = true;
            //Meg kell nézni az alagútban található síneket.
            for (Rail tunnelRail: _tunnelRails) {
                if (tunnelRail.getOccupied_() != null)
                    empty = false;
            }
            //És az alagút bejáratait.
            for (TunnelEntrance entrance: _exitPoints) {
                if (entrance.getOccupied_() != null)
                    empty = false;
            }
            //Ha az alagút üres, akkor letiltjuk, és true értékkel térünk vissza.
            if (empty) {
                _enabled = false;
                return true;
            }
            //Ha az alagút nem üres, akkor false a visszatérési érték.
            return false;
    }

    /**
     * Tunnel engedélyezése
     *
     * @param b Érvényes-e
     */
    public void setEnabled(Boolean b) {
        _enabled = b;
    }
    /**
     * Engedélyezi az alagutat.
     */
    public void enable() {
        setEnabled(true);
    }

    /**
     * Kattintás eseménykezelője.
     * Akkor hívódik ha rámennek az éppen vizsgált tunnelre
     */
    @Override
    public void click() {
        Boolean tunnelEntranceOccupied = false;
        for (TunnelEntrance te : _exitPoints) {
            if (te.getOccupied_() != null) tunnelEntranceOccupied = true;
        }

        if (!_enabled && !tunnelEntranceOccupied) {
            System_.map.enableTunnel(this.tunnelName);
        }
    }
}
