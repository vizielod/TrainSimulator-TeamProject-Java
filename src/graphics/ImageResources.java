package graphics;

import map.MapElement;
import movingelement.MovingElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by turbosnakes on 2017. 04. 27..
 */
public class ImageResources {
    //A játékelemek képekként
    public static ArrayList<ImageIcon> images = new ArrayList<>();

    /**
     * Paraméter nélküli konstruktor, mely betölti a képeket az images mappából
     */
    public ImageResources() {
        for (int i = 0; i < 39; i++) {
            try {
                Image img = ImageIO.read(new File("images/" + i + ".jpg"));
                ImageIcon icon = new ImageIcon(img);
                images.add(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Kép lekérése MapElement esetén
     *
     * @param me Melyik ME
     * @return Maga a kép
     */
    public static ImageIcon getImage(MapElement me) {
        try {
            if (me.getImage() != -1)
                return images.get(me.getImage());
        } catch (Exception e) {}
        return null;
    }

    /**
     * Képek lekérése MovingElement esetén
     *
     * @param me
     * @return
     */
    public static ImageIcon getImage(MovingElement me) {
        try {
            if (me.getImage() != -1)
                return images.get(me.getImage());
        } catch (Exception e) {
        }
        return null;
    }
}
