package movingelement;

import map.MapElement;

import java.awt.*;

/**
 * Created by turbosnakes on 2017. 04. 13..
 */
public class CoalCarriage extends Carriage {
    /**
     * A szeneskocsi konstruktora. Mivel a szeneskocsi színe feltétlenül fekete, és mindig teli van, nincs paraméteres konstruktor.
     */
    public CoalCarriage(String name, MapElement _currentElement, MapElement _previousElement) {
        super(Color.BLACK, false, name, _currentElement, _previousElement);
    }

    /**
     * Nincs implementálva, mivel a szeneskocsi telítettsége fix.
     * @param empty
     */
    public void setEmpty(Boolean empty) {
    }

}
