package graphics;

import filemgmt.FileManager;
import logic.System_;
import map.MapElement;
import map.Rail;
import map.Switch_;
import map.Tunnel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.IOException;

import static graphics.Graphics.gameWindow;

/**
 * Created by turbosnakes on 2017. 04. 27..
 */
public class GameWindow extends JFrame {
    private static JButton[][] buttons = new JButton[13][13];
    JSplitPane splitPane;
    boolean init = true;
    private JPanel gamePanel;
    private JMenuBar menuPanel;
    private JPanel statusPanel;
    private ImageResources imageResources;

    /**
     * Paraméter nélküli konstruktor, mely megjeleníti az ablakot
     */
    public GameWindow() {
        super("Vonatos jatek 1.0");
        gamePanel = new JPanel(new GridLayout(13, 13));
        menuPanel = new JMenuBar();
        statusPanel = new JPanel();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gamePanel, statusPanel);
        imageResources = new ImageResources();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 1000));
        splitPane.setDividerSize(1);
        splitPane.setDividerLocation(950);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        /**
         * File menü
         */
        JMenu fileMenu = new JMenu("File");

        /**
         * New game menüpont
         */
        JMenuItem newItem = new JMenuItem("New game");
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System_.reInit();
                addACtionListener();
                newLevel();
                FileManager.load(5);
                setTunnelBorder();
            }
        });
        fileMenu.add(newItem);

        /**
         * Load menüpont
         */
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System_.reInit();
                addACtionListener();

                FileManager.load(1000);
                setTunnelBorder();
            }
        });
        fileMenu.add(loadItem);

        /**
         * Exit menüpont
         */
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileManager.save(1000);
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);

        /**
         * Help menü
         */
        JMenu helpMenu = new JMenu("Help");

        JMenuItem addHelp = new JMenuItem("Help");
        addHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String help = "segitseg";
                try {
                    help = FileManager.ReadHelp("help.dat");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                JOptionPane.showMessageDialog(new JFrame(), help);
            }
        });

        JMenuItem addAbout = new JMenuItem("About");
        addAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String about = "Vonatos jatek\nVerzió: 1.0\n© 2017 turbosnakes. Minden jog fenntartva.\n\nKészítők:\nDobó Ádám\nFenes Áron\nPapp Attila\nSalamon Krisztián\nVízi Előd";
                JOptionPane.showMessageDialog(new JFrame(), about);
            }
        });


        helpMenu.add(addHelp);
        helpMenu.add(addAbout);


        menuPanel.add(fileMenu);
        menuPanel.add(helpMenu);
        setJMenuBar(menuPanel);

        splitPane.setResizeWeight(0.95);
        splitPane.setEnabled(false);
        getContentPane().add(splitPane);
        pack();
        setVisible(true);
    }

    private static void setTunnelBorder() {
        try {

            Tunnel temp = System_.map.getEnabledTunnel();
            ArrayList<Rail> tempRails = temp.getTunnelRails();
            for (int i = 0; i < tempRails.size(); i++) {
                int xx = (int) tempRails.get(i).getPosition().getX();
                int yy = (int) tempRails.get(i).getPosition().getY();
                buttons[yy][xx].setBackground(Color.GREEN);
                buttons[yy][xx].setBorder(BorderFactory.createRaisedBevelBorder());
            }

            for (Tunnel t : System_.map.tunnels) {
                if (t != System_.map.getEnabledTunnel())
                    for (int i = 0; i < t.getTunnelRails().size(); i++) {
                        int xx = (int) t.getTunnelRails().get(i).getPosition().getX();
                        int yy = (int) t.getTunnelRails().get(i).getPosition().getY();

                        buttons[yy][xx].setBorder(null);
                        buttons[yy][xx].setBackground(null);
                    }
            }

        } catch (Exception e) {
        }
    }

    public static void newLevel() {
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 13; y++) {
                buttons[x][y].setIcon(null);
                buttons[x][y].setBorder(null);
                buttons[x][y].setBackground(null);
            }
        }
        setTunnelBorder();
    }

    /**
     * Kattintás kezelése
     */
    public void addACtionListener() {
        reDraw();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int x = 0; x < 13; x++) {
                            for (int y = 0; y < 13; y++) {
                                if (e.getSource() == buttons[x][y]) {
                                    MapElement clickedElement = System_.map.getElementAt(y, x);
                                    Tunnel tryTunnel = System_.map.getTunnelFromRail(clickedElement);
                                    try {
                                        if (tryTunnel != null) {
                                            tryTunnel.click();
                                            setTunnelBorder();
                                        } else {
                                            clickedElement.click();
                                        }
                                    } catch (Exception ec) {
                                    }
                                    buttons[x][y].setIcon(ImageResources.getImage(System_.map.getElementAt(y, x)));
                                }

                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Újrarajzolás
     */
    public void reDraw() {
        if (init) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    buttons[x][y] = new JButton();
                    gamePanel.add(buttons[x][y]);
                }
            }
        }
        init = false;
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 13; y++) {

                //buttons[x][y].setEnabled(false);
                if (ImageResources.getImage(System_.map.getElementAt(y, x)) != null)
                    buttons[x][y].setIcon(ImageResources.getImage(System_.map.getElementAt(y, x)));
                if (ImageResources.getImage(System_.getMEat(y, x)) != null)
                    if (!System_.map.getEnabledTunnel().getTunnelRails().contains(System_.map.getElementAt(y, x)))
                        buttons[x][y].setIcon(ImageResources.getImage(System_.getMEat(y, x)));

                try {
                    if (System_.map.getElementAt(y, x).getClass() == Switch_.class) {
                        buttons[x][y].setBackground(Color.GREEN);
                        buttons[x][y].setBorder(BorderFactory.createRaisedBevelBorder());
                    }
                } catch (Exception ex) {
                }


                revalidate();
            }
        }
    }

    private void bindImageToButton(Image image, Point pos) {

    }

    public JButton[][] getButtons() {
        return buttons;
    }

}
