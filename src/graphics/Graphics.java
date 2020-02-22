package graphics;

/**
 * Created by turbosnakes on 2017. 04. 27..
 */
public class Graphics{
    public static GameWindow gameWindow;

    public static void init()
    {
        gameWindow = new GameWindow();

    }
    public static void reDraw() {
        gameWindow.reDraw();
    }

    public void gameOver() {
    }
}
