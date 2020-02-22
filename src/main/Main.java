package main;


import graphics.Graphics;
import logic.Player;
import logic.System_;
import map.Map;
import movingelement.Train;

/**
 * Created by turbosnakes on 2017. 02. 10..
 */

public class Main {
    /**
     * A programunk main függvénye
     *
     * @param args parancssori argumentumok
     */
    public static void main(String[] args) {
        /**
         Útravaló:
         - Légyszi nézzétek majd át az osztályokban az egyes tagváltozókat és függvényeket kódoláskor, hogy megfelelnek-e
         a diagramon leírtaknak.
         - Hiba vagy egy új feature bejelentésére használjátok githubon az Issues fület.
         * megfelelő címkével
         * az adott mérföldkővel
         * akár segítség kéréssel (Assignees)
         - Az aranyszabály most is érvényes. Nincs közvetlen push a masterbe!!!
         - A Commitok tömörek legyenek, lényegre törő címmel és beszédes, de nem hosszú leírással
         * commit előtt kipróbálni a kódot (ha lehet)
         * formázottan és bemenet optimizáltan töltsétek fel (jobbklikk vagy Crtl+Alt+L és Crtl+Alt+O)
         * érdemes a környezet warnningjait átolvasni feltöltés előtt, nem annyra buta az..
         */

        System_.map = new Map();

        Graphics.init();
        while (true) {
            try {
                Train.removeTrain();
                for (Train train : System_.trains) {
                    train.nextStep();
                }

                Graphics.reDraw();

                Thread.sleep(Player.getSpeed());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
