/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforgeplatform.openstreetmap.service;

import de.fub.mapsforgeplatform.openstreetmap.xml.osm.Osm;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * Interface for map providers. The map will be specified via a bounding box
 * specified like by the OSM rest api.
 *
 * @author Serdar
 */
public interface MapProvider {

    /**
     * The name of the Map provider.
     *
     * @return always a String.
     */
    public String getName();

    /**
     * Describes what type of maps this provider provides the user, i.e. maps
     * for bike, trains or plain highways / roads.
     *
     * @return always a description string.
     */
    public String getDescription();

    /**
     * Returns a map as Osm, the openstreetmap xml format. The specification of
     * the bounding box is the same like from the OSM rest api
     *
     * @param leftLon the left longitude in degree.
     * @param bottomLat the bottom latitude in degree. (the smaller latitude
     * value)
     * @param rightLon the right longitude in degree.
     * @param topLat the top latitude in degree. (the bigger latitude value)
     * @return A Osm instance if the specified map was found, otherwise null.
     */
    public Osm getMap(double leftLon, double bottomLat, double rightLon, double topLat);

    /**
     * Factory class for convenience to create instances of all registered
     * MapProviders.
     */
    public static class Factory {

        /**
         * creates a Collection with newly created registered MapPRoviders.
         *
         * @return A Collection with MapProviders, empty collection if no
         * MapProvider was registered via ServiceProvider annotation.
         */
        public synchronized static Collection<MapProvider> getAllInstances() {
            ArrayList<MapProvider> resultList = new ArrayList<MapProvider>(20);
            Set<Class<? extends MapProvider>> allClasses = Lookup.getDefault().lookupResult(MapProvider.class).allClasses();

            for (Class<? extends MapProvider> mapProviderClass : allClasses) {
                try {
                    MapProvider mapProvider = mapProviderClass.newInstance();
                    resultList.add(mapProvider);
                } catch (InstantiationException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            return resultList;
        }
    }
}
