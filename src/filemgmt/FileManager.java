package filemgmt;

import javafx.util.Pair;
import logic.System_;
import map.*;
import movingelement.Carriage;
import movingelement.MovingElement;
import movingelement.Train;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by turbosnakes on 2017. 03. 13..
 */
public class FileManager {

    //Ideiglenes listák a betöltéshez.
    private static ArrayList<Rail> rails = new ArrayList<>();
    private static ArrayList<Station> stations = new ArrayList<>();
    private static ArrayList<Switch_> switches = new ArrayList<>();
    private static ArrayList<TunnelEntrance> tunnelentrances = new ArrayList<>();

    /**
     * Mentésnél kimentük a vonatokat és az aktuális pályát.
     *
     * @param mapIndex
     */
    public static void save(int mapIndex) {
        saveTrains(mapIndex);
        saveMap(mapIndex);
    }

    /**
     * Betöltéskor ideiglenesen betöltjük a pályaelemeket. Ezután a MovingElement-ek betöltése következik a referenciák beállításával. Végül a pályaelemeket véglegesen betöltjük.
     */
    public static void load(int mapIndex) {
        try {
            //loadTrains(mapIndex);
            System_.map = new Map();
            loadMap(mapIndex);
            System_.trains = new ArrayList<>();
            loadTrains(mapIndex);
        } catch (Exception e) {
            System.out.println("Az inicializalas sikertelen volt.");
        }

    }

    public static String ReadHelp(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder help = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                help.append(line);
                help.append(ls);
            }

            return help.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * A vonatokat és a hozzájuk tartozó kocsikat lementi a paraméterként kapott indexű pályába. Ehhz végigiterál a vonatokon és a kocsikon.
     *
     * @param mapIndex a pálya indexe, amibe mentünk
     */
    private static void saveTrains(Integer mapIndex) {
        PrintWriter writer1 = null;
        try {
            //Megnyitjuk a megfelelő fájlt.
            writer1 = new PrintWriter("map" + mapIndex.toString()+ "/trains" + mapIndex.toString() + ".dat");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Amíg van vonat, írunk.
        for (Integer i = 0; i < System_.trains.size(); i++) {
            writer1.println(System_.trains.get(i).elementName);
            if (System_.trains.get(i).currentElement == null) {
                writer1.println("null");
            } else {
                writer1.println(System_.trains.get(i).currentElement.elementName);
            }
            if (System_.trains.get(i).previousElement == null) {
                writer1.println("null");
            } else {
                writer1.println(System_.trains.get(i).previousElement.elementName);
            }
            writer1.println(((Double)System_.trains.get(i).getPosition().getX()).toString().split("\\.")[0]);
            writer1.println(((Double)System_.trains.get(i).getPosition().getY()).toString().split("\\.")[0]);

            writer1.println("Carriages:");
            //A kocsikon is végig kell itterálni.
            if (System_.trains.get(i)._carts != null) {
                if (System_.trains.get(i)._carts.size() != 0) {
                    for (Carriage carriage : System_.trains.get(i)._carts) {
                        saveMovingElement(carriage, writer1);
                        writer1.println(carriage.getColorString());
                        writer1.println(carriage.getEmpty().toString());
                        writer1.println("###");
                    }
                } else {
                    writer1.println("null");
                    writer1.println("###");
                }
            } else {
                writer1.println("null");
                writer1.println("###");
            }

            writer1.println("---");
        }
        writer1.close();
    }

    /**
     * A MovingElement közös tulajdonságait írja ki fájlba.
     * @param me a kiírandó MovingElement
     * @param writer a kiírás helye
     */
    private static void saveMovingElement(MovingElement me, PrintWriter writer) {
        writer.println(me.elementName);
        if (me.currentElement != null) {
            writer.println(me.currentElement.elementName);
        } else {
            writer.println("null");
        }
        if (me.previousElement != null) {
            writer.println(me.previousElement.elementName);
        } else {
            writer.println("null");
        }

        writer.println(((Double)me.getPosition().getX()).toString().split("\\.")[0]);
        writer.println(((Double)me.getPosition().getY()).toString().split("\\.")[0]);
    }

    /**
     * A síneket tölti be az adott indexű fájlból.
     * @param mapIndex a pálya indexe
     * @throws IOException
     */
    private static void loadRails(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/rails" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        while ((line = br.readLine()) != null && !line.equals("---")) {
            Rail rail = new Rail();
            rail.elementName = line;
            line = br.readLine();
            int x = Integer.parseInt(line);
            line = br.readLine();
            int y = Integer.parseInt(line);
            rail.setPoint(new Point(x, y));
            line = br.readLine();
            for (Train tr : System_.trains) {
                if (tr.elementName.equals(line))
                    rail.setOccupied_(tr);
                else {
                    for (Carriage ca : tr._carts) {
                        if (ca.elementName.equals(line))
                            rail.setOccupied_(ca);
                    }
                }
            }
            line = br.readLine();
            line = br.readLine();
            rails.add(rail);
        }
    }

    /**
     * Az állomásokat tölti be az adott indexű fájlból.
     * @param mapIndex a pálya indexe
     * @throws IOException
     */
    private static void loadStations(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/stations" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        while ((line = br.readLine()) != null && !line.equals("---")) {
            Station station = new Station();
            station.elementName = line;
            line = br.readLine();
            int x = Integer.parseInt(line);
            line = br.readLine();
            int y = Integer.parseInt(line);
            station.setPoint(new Point(x, y));
            line = br.readLine();
            for (Train tr : System_.trains) {
                if (tr.elementName.equals(line))
                    station.setOccupied_(tr);
                else {
                    for (Carriage ca : tr._carts) {
                        if (ca.elementName.equals(line))
                            station.setOccupied_(ca);
                    }
                }
            }
            line = br.readLine();
            line = br.readLine();
            station.setColorString(line);
            line = br.readLine();
            station.setEmpty(Boolean.valueOf(line));
            stations.add(station);
            line = br.readLine();
        }
    }

    /**
     * A váltókat tölti be az adott indexű fájlból.
     * @param mapIndex a pálya indexe
     * @throws IOException
     */
    private static void loadSwitches(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/switches" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        while ((line = br.readLine()) != null && !line.equals("---")) {
            Switch_ switch_ = new Switch_();
            switch_.elementName = line;
            line = br.readLine();
            int x = (Integer.parseInt(line));
            line = br.readLine();
            int y = (Integer.parseInt(line));
            switch_.setPoint(new Point(x, y));
            line = br.readLine();
            for (Train tr : System_.trains) {
                if (tr.elementName.equals(line))
                    switch_.setOccupied_(tr);
                else {
                    for (Carriage ca : tr._carts) {
                        if (ca.elementName.equals(line))
                            switch_.setOccupied_(ca);
                    }
                }
            }
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            //line = br.readLine();
            switches.add(switch_);
        }
    }

    /**
     * Az alagutakat tölti be az adott indexű fájlból.
     * @param mapIndex a pálya indexe
     * @throws IOException
     */
    private static void loadTunnelEntrances(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/tunnelentrances" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        while ((line = br.readLine()) != null && !line.equals("---")) {
            TunnelEntrance te = new TunnelEntrance();
            te.elementName = line;
            line = br.readLine();
            int x = (Integer.parseInt(line));
            line = br.readLine();
            int y = (Integer.parseInt(line));
            te.setPoint(new Point(x, y));
            line = br.readLine();
            for (Train tr : System_.trains) {
                if (tr.elementName.equals(line))
                    te.setOccupied_(tr);
                else {
                    for (Carriage ca : tr._carts) {
                        if (ca.elementName.equals(line))
                            te.setOccupied_(ca);
                    }
                }
            }
            line = br.readLine();
            line = br.readLine();
            tunnelentrances.add(te);
        }
    }

    /**
     * Beállítja a sínek szomszédossági viszonyait.
     * @param mapIndex az adott pálya indexe
     * @throws IOException
     */
    private static void loadRailsNeighbors(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/rails" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        int railNum = 0;
        while ((line = br.readLine()) != null && !line.equals("---")) {
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            if (!line.equals("null")) {
                String[] pairs = line.split(";");
                for (int i = 0; i < pairs.length; i++) {
                    String[] pairsName = pairs[i].split(",");
                    MapElement e1 = null;
                    MapElement e2 = null;
                    if (System_.map.mapElements != null) {
                        if (!pairsName[0].equals("null")) {
                            for (MapElement me : System_.map.mapElements) {
                                if (pairsName[0].equals(me.elementName))
                                    e1 = me;
                            }
                        }
                        if (!pairsName[1].equals("null")) {
                            for (MapElement me : System_.map.mapElements) {
                                if (pairsName[1].equals(me.elementName))
                                    e2 = me;
                            }
                        }

                        rails.get(railNum).addNeighbor(new Pair<MapElement, MapElement>(e1, e2));

                    }
                }
                line = br.readLine();
            }
            railNum++;

        }
    }

    /**
     * Beállítja az állomások szomszédossági viszonyait.
     * @param mapIndex az adott pálya indexe
     * @throws IOException
     */
    private static void loadStationsNeighbors(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/stations" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        int stationNum = 0;
        while ((line = br.readLine()) != null && !line.equals("---")) {
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();

            if (!line.equals("null")) {
                String[] pairs = line.split(";");
                for (int i = 0; i < pairs.length; i++) {
                    String[] pairsName = pairs[i].split(",");
                    MapElement e1 = null;
                    MapElement e2 = null;
                    if (!pairsName[0].equals("null")) {
                        for (MapElement me : System_.map.mapElements) {
                            if (pairsName[0].equals(me.elementName))
                                e1 = me;
                        }
                    }
                    if (!pairsName[1].equals("null")) {
                        for (MapElement me : System_.map.mapElements) {
                            if (pairsName[1].equals(me.elementName))
                                e2 = me;
                        }
                    }

                    stations.get(stationNum).addNeighbor(new Pair<MapElement, MapElement>(e1, e2));

                    line = br.readLine();
                    line = br.readLine();
                    line = br.readLine();
                }
            }
            stationNum++;
        }
    }

    /**
     * Beállítja a váltók szomszédossági viszonyait.
     * @param mapIndex az adott pálya indexe
     * @throws IOException
     */
    private static void loadSwitchesNeighbors(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/switches" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        int SwitchNum = 0;
        while ((line = br.readLine()) != null && !line.equals("---")) {
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            String[] pairs = line.split(";");
            for (int i = 0; i < pairs.length; i++) {
                String[] pairsName = pairs[i].split(",");
                MapElement e1 = null;
                MapElement e2 = null;
                for (MapElement me : System_.map.mapElements) {
                    if (pairsName[0].equals(me.elementName))
                        e1 = me;
                }
                for (MapElement me : System_.map.mapElements) {
                    if (pairsName[1].equals(me.elementName))
                        e2 = me;
                }
                switches.get(SwitchNum).addNeighbor(new Pair<MapElement, MapElement>(e1, e2));
            }
            MapElement e1 = null;
            MapElement e2 = null;
            line = br.readLine();
            String[] active = line.split(",");
            for (MapElement me : System_.map.mapElements) {
                if (active[0].equals(me.elementName))
                    e1 = me;
            }
            for (MapElement me : System_.map.mapElements) {
                if (active[1].equals(me.elementName))
                    e2 = me;
            }
            switches.get(SwitchNum).setActivePair(new Pair<MapElement, MapElement>(e1, e2));
            line = br.readLine();
            SwitchNum++;
        }
    }

    /**
     * Betölti az alagutakat fájlból.
     * @param mapIndex az adott pálya indexe
     * @throws IOException
     */
    private static void loadTunnels(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/tunnels" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        while ((line = br.readLine()) != null && !line.equals("---")) {
            Tunnel tunnel = new Tunnel();
            tunnel.tunnelName = line;
            line = br.readLine();
            String[] tunnelRails = line.split(";");
            for (String s : tunnelRails) {
                for (Rail te : rails) {
                    if (te.elementName.equals(s))
                        tunnel.addTunnelRail(te);
                }
            }
            line = br.readLine();
            String[] tunnelEntrances = line.split(";");
            for (String s : tunnelEntrances) {
                for (TunnelEntrance te : tunnelentrances) {
                    if (te.elementName.equals(s))
                        tunnel.addTunnelEntrance(te);
                }
            }
            line = br.readLine();
            tunnel.setEnabled(Boolean.valueOf(line));
            System_.map.tunnels.add(tunnel);
            if (tunnel.getEnabled())
                System_.map.enableTunnel(tunnel.tunnelName);
            line = br.readLine();
        }
    }

    /**
     * Beállítja az alagútbejáratok szomszédossági viszonyait.
     * @param mapIndex az adott pálya indexe
     * @throws IOException
     */
    private static void loadTENeighbors(Integer mapIndex) throws IOException {
        String line;
        InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/tunnelentrances" + mapIndex.toString() + ".dat");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        int TunENum = 0;
        while ((line = br.readLine()) != null && !line.equals("---")) {
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            if (!line.equals("null")) {
                String[] pairs = line.split(";");
                for (int i = 0; i < pairs.length; i++) {
                    String[] pairsName = pairs[i].split(",");
                    MapElement e1 = null;
                    MapElement e2 = null;
                    for (MapElement me : System_.map.mapElements) {
                        if (pairsName[0].equals(me.elementName))
                            e1 = me;
                    }
                    for (MapElement me : System_.map.mapElements) {
                        if (pairsName[1].equals(me.elementName))
                            e2 = me;
                    }
                    tunnelentrances.get(TunENum).addNeighbor(new Pair<MapElement, MapElement>(e1, e2));
                    line = br.readLine();

                }
            }
            TunENum++;
        }
    }

    /**
     * A pálya betöltését kezelő függvény. A paraméterként kapott indexű pályafájlból betölti a rendszerbe az adatokat.
     * @param mapIndex
     * @throws IOException
     */
    private static void loadMap(Integer mapIndex) throws IOException {
        rails = new ArrayList<>();
        stations = new ArrayList<>();
        switches = new ArrayList<>();
        tunnelentrances = new ArrayList<>();

        loadRails(mapIndex);
        loadStations(mapIndex);
        loadSwitches(mapIndex);
        loadTunnelEntrances(mapIndex);

        //Ideiglenesen, hogy a szomszédossági viszonyokat be lehessen állítani.
        System_.map.mapElements.addAll(rails);
        System_.map.mapElements.addAll(switches);
        System_.map.mapElements.addAll(stations);
        System_.map.mapElements.addAll(tunnelentrances);

        loadRailsNeighbors(mapIndex);
        loadStationsNeighbors(mapIndex);
        loadSwitchesNeighbors(mapIndex);
        loadTENeighbors(mapIndex);
        loadTunnels(mapIndex);

        System_.map.mapElements = new ArrayList<>();
        System_.map.mapElements.addAll(rails);
        System_.map.mapElements.addAll(switches);
        System_.map.mapElements.addAll(stations);
        System_.map.mapElements.addAll(tunnelentrances);

    }

    /**
     * Betölti a kapott indexű pályafájlból a kocsikat és a hozzájuk tartozó síneket.
     * @param mapIndex a pálya indexe
     */
    private static void loadTrains(Integer mapIndex) {
        try {
            String line;
            InputStream fis = new FileInputStream("map" + mapIndex.toString()+ "/trains" + mapIndex.toString() + ".dat");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            Train train = new Train();
            ArrayList<Carriage> carriages = new ArrayList<>();
            Carriage carriage = new Carriage();
            while ((line = br.readLine()) != null && !line.equals("---") && !line.equals("###")) {
                try {
                    train = new Train();
                    carriages = new ArrayList<>();
                    carriage = new Carriage();
                    train.elementName = line;
                    line = br.readLine();
                    //a currentElement lehet null
                    if (!line.equals("null")) {
                        if (System_.map.mapElements != null) {
                            for (MapElement mp : System_.map.mapElements) {
                                if (mp.elementName.equals(line))
                                    train.currentElement = mp;
                            }
                            line = br.readLine();
                            if (!line.equals("null")) {
                                for (MapElement mp : System_.map.mapElements) {
                                    if (mp.elementName.equals(line))
                                        train.previousElement = mp;
                                }
                            }
                        }
                    } 

                    line = br.readLine();
                    int x = Integer.parseInt(line);
                    line = br.readLine();
                    int y = Integer.parseInt(line);
                    train.setPoint(new Point(x, y));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                line = br.readLine();
                line = br.readLine();
                //a kocsik értéke lehet null
                if (!line.equals("null")) {
                    while ( line != null && !line.equals("###") && !line.equals("---")) {
                        try {
                            carriage = new Carriage();
                            carriage.elementName = line;
                            line = br.readLine();
                            if (System_.map.mapElements != null) {
                                for (MapElement mp : System_.map.mapElements) {
                                    if (mp.elementName.equals(line))
                                        carriage.currentElement = mp;
                                }
                                line = br.readLine();
                                for (MapElement mp : System_.map.mapElements) {
                                    if (mp.elementName.equals(line))
                                        carriage.previousElement = mp;
                                }
                            } 
                            line = br.readLine();
                            int x = Integer.parseInt(line);
                            line = br.readLine();
                            int y = Integer.parseInt(line);
                            carriage.setPoint(new Point(x, y));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        line = br.readLine();
                        carriage.setColorString(line);
                        line = br.readLine();
                        carriage.setEmpty(Boolean.valueOf(line));
                        line = br.readLine();
                        carriages.add(carriage);
                        line = br.readLine();
                    }
                    train._carts = carriages;
                }
                System_.trains.add(train);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Kimenti a pályán található MapElementeket.
     * @param mapIndex
     */
    private static void saveMap(Integer mapIndex) {
        PrintWriter writer1 = null;
        PrintWriter writer2 = null;
        PrintWriter writer3 = null;
        PrintWriter writer4 = null;
        PrintWriter writer5 = null;
        try {
            //5 db fájl kell.
            writer1 = new PrintWriter("map" + mapIndex.toString()+ "/rails" + mapIndex.toString() + ".dat");
            writer2 = new PrintWriter("map" + mapIndex.toString()+ "/stations" + mapIndex.toString() + ".dat");
            writer3 = new PrintWriter("map" + mapIndex.toString()+ "/switches" + mapIndex.toString() + ".dat");
            writer4 = new PrintWriter("map" + mapIndex.toString()+ "/tunnelentrances" + mapIndex.toString() + ".dat");
            writer5 = new PrintWriter("map" + mapIndex.toString()+ "/tunnels" + mapIndex.toString() + ".dat");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < System_.map.mapElements.size(); i++) {
            if (System_.map.mapElements.get(i).getClass().equals(Rail.class)) {
                saveMapElement(System_.map.mapElements.get(i), writer1);
                writer1.println("---");
            }

            if (System_.map.mapElements.get(i).getClass().equals(Station.class)) {
                saveMapElement(System_.map.mapElements.get(i), writer2);
                writer2.println(((Station) System_.map.mapElements.get(i)).getColorString());
                writer2.println(((Station) System_.map.mapElements.get(i)).getEmpty().toString());
                writer2.println("---");
            }

            if (System_.map.mapElements.get(i).getClass().equals(Switch_.class)) {
                saveMapElement(System_.map.mapElements.get(i), writer3);
                writer3.println(((Switch_) System_.map.mapElements.get(i)).getActivePair().getKey().elementName + "," + ((Switch_) System_.map.mapElements.get(i)).getActivePair().getValue().elementName);
                writer3.println("---");
            }

            if (System_.map.mapElements.get(i).getClass().equals(TunnelEntrance.class)) {
                saveMapElement(System_.map.mapElements.get(i), writer4);
                writer4.println("---");
            }
        }

        if (System_.map.tunnels != null && System_.map.tunnels.size() > 0) {
            for (Tunnel tunnel : System_.map.tunnels) {
                String tunnelRails = "";
                String exitPoints = "";
                if (tunnel.getTunnelRails() != null) {
                    for (Rail tunnelrail : tunnel.getTunnelRails()) {
                        tunnelRails += tunnelrail.elementName + ";";
                    }
                } else {
                    tunnelRails = "null\n";
                }
                if (tunnel.getExitPoints() != null) {
                    for (TunnelEntrance exitpoint : tunnel.getExitPoints()) {
                        exitPoints += exitpoint.elementName + ";";
                    }
                } else {
                    exitPoints = "null\n";
                }

                writer5.println(tunnel.tunnelName);
                writer5.println(tunnelRails);
                writer5.println(exitPoints);
                writer5.println(tunnel.getEnabled());
                writer5.println("---");
            }
        }

        writer1.close();
        writer2.close();
        writer3.close();
        writer4.close();
        writer5.close();
    }

    /**
     * A kapott MapElement mindenkire vonatkozó részeit írja ki az átvett helyre.
     * @param me a kiírandó elem
     * @param writer hova kell kiírni
     */
    private static void saveMapElement(MapElement me, PrintWriter writer) {
        writer.println(me.elementName);
        writer.println(((Double)me.getPosition().getX()).toString().split("\\.")[0]);
        writer.println(((Double)me.getPosition().getY()).toString().split("\\.")[0]);
        String name = "";
        if (me.getOccupied_() != null) {
            name = me.getOccupied_().elementName;
        } else {
            name = "null";
        }
        writer.println(name);
        String neighbors = "";
        if (me.getNeighbors() != null && me.getNeighbors().size() > 0) {
            for (Pair<MapElement, MapElement> pair : me.getNeighbors()) {
                if (pair.getKey() == null)
                    neighbors += "null,";
                else neighbors += pair.getKey().elementName + ",";
                if (pair.getValue() == null)
                    neighbors += "null;";
                else
                    neighbors += pair.getValue().elementName + ";";
            }
        } else {
            neighbors = "null";
        }
        writer.println(neighbors);
    }

}
