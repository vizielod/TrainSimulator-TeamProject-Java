package logic;

import filemgmt.FileManager;
import graphics.GameWindow;
import graphics.Graphics;
import map.Map;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class Player {

    public int currentLevel;
    public String player_name; //name-et valamiért aláhúzta lilával és az sose jó xd
    private static int level = 5;
    private static long speed = 600;
    Scanner s = new Scanner(System.in);

    public static long getSpeed() {
        return speed;
    }

    /**
     * Ez a függvény kezeli a felhasználói bemenetet a játék során.
     */
    public String readCommand() {
        String command = "";
            //Fontos, hogy mindig csak egy utasítást olvasunk be!
            if (s.hasNextLine()) {
                command = s.nextLine();
            }
        return command;
    }

    public static void increaseLevel() {
        if (level < 8) {
            level++;
            speed -= 100;
            int input = JOptionPane.showOptionDialog(null, "Next level", "Message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            if (input == JOptionPane.OK_OPTION) {
                System_.reInit();
                FileManager.load(level);
                GameWindow.newLevel();
            }
        }

        else
            System_.gameWon();
    }

    public static int getLevel() {
        return level;
    }
}
