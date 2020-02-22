package logic;

import filemgmt.FileManager;
import map.Map;
import movingelement.Carriage;
import movingelement.MovingElement;
import movingelement.Train;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class System_ {
    public static Map map;
    private static Player _player = new Player();
    public static ArrayList<Train> trains = new ArrayList<>();
    private Timer timer1 = new Timer();

    /**
     * Paraméter nélküli konstruktor
     */
    public System_() {
    }

    public static MovingElement getMEat(int x, int y) {
        for (Train t : trains) {
            if (t.getPosition().getX() == x && t.getPosition().getY() == y)
                return t;
            for (Carriage c : t._carts) {
                if (c.getPosition().getX() == x && c.getPosition().getY() == y)
                    return c;
            }
        }
        return null;
    }

    /**
     * Ez a függvény kezeli azt az esetet, amikor a játékos veszít.
     */
    public static void gameOver() {
        map = new Map();
        trains = new ArrayList<>();
        System.out.println("game over");
        JOptionPane.showMessageDialog(new JFrame(), "Game over.");
    }

    public static void reInit(){
        map.mapElements.clear();
        map.tunnels.clear();
        map.setEnabledTunnel(null);
        map.setFileManager(null);
        trains = new ArrayList<>();
    }

    /**
     * Játék megnyerése esetén hívódó függvény
     */
    public static void gameWon() {
        map = new Map();
        trains = new ArrayList<>();
        JOptionPane.showMessageDialog(new JFrame(), "You won.");
    }
}
