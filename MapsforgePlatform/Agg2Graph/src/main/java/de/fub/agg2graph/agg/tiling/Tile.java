/**
 * *****************************************************************************
 * Copyright 2013 Johannes Mitlmeier
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package de.fub.agg2graph.agg.tiling;

import de.fub.agg2graph.structs.ILocation;
import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A tile in the tiled structure of the spatial data.
 *
 * @author Johannes Mitlmeier
 *
 * @param <T> Type of location data to hold
 */
public class Tile<T extends ILocation> {

    public Rectangle2D.Double size;
    public boolean isLeaf;
    private List<Tile<T>> children;
    private final Set<T> elements;
    public Tile<T> parent;
    private String ID = "";
    public boolean isLoaded;
    public TileManager tm;
    private TileCache tc;

    public Tile(TileManager tm, TileCache tc, Tile<T> parent,
            Rectangle2D.Double size) {
        this.tm = tm;
        this.tc = tc;
        this.parent = parent;
        this.size = size;
        isLeaf = true;
        elements = new HashSet<T>(tm.getMaxElementsPerTile() / 10);
    }

    public Set<T> getElements() {
        return elements;
    }

    public int getElemCount() {
        if (isLeaf) {
            return getElements().size();
        }
        int elemCount = 0;
        for (Tile<T> child : getChildren()) {
            elemCount += child.getElemCount();
        }
        return elemCount;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    /**
     * Split this tile. The number of sub-tiles created depends on
     *
     * @{link {@link TileManager#maxElementsPerTile}, e.g. a value of 3 will
     * make 9 new tiles.
     */
    public void split() {
        if (!isLeaf) {
            return;
        }
        // make new leafs
        Rectangle2D.Double tileSize = getSize();
        int splitFactor = tm.getSplitFactor();
        double subTileWidth = tileSize.getWidth() / splitFactor;
        double subTileHeight = tileSize.getHeight() / splitFactor;
        double x, y;
        for (int i = 0; i < splitFactor * splitFactor; i++) {
            x = i % splitFactor;
            y = i / splitFactor;
            Tile<T> childTile = new Tile<T>(tm, tc, this,
                    new Rectangle2D.Double(tileSize.getMinX() + subTileWidth
                    * x, tileSize.getMinY() + subTileHeight * y,
                    subTileWidth, subTileHeight));
            childTile.setID("".equals(getID()) ? String.valueOf(i) : MessageFormat.format("{0}-{1}", getID(), i));
            getChildren().add(childTile);
        }
        // push points
        for (T elem : getElements()) {
            Tile<T> subTile = getSubTile(elem);
            if (subTile == null) {
                continue;
            }
            subTile.getElements().add(elem);
        }
        // turn old leaf to node
        isLeaf = false;
        getElements().clear();

        // recurse!
        for (Tile<T> subTile : getChildren()) {
            subTile.setLoaded(isLoaded);
            if (subTile.getElemCount() > tm.getMaxElementsPerTile()
                    && subTile.size.getWidth() >= tm.getMinimumSplitSize().getWidth()
                    && subTile.size.getHeight() >= tm.getMinimumSplitSize().getHeight()) {
                subTile.split();
            }
        }
    }

    public List<Tile<T>> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<Tile<T>>(200);
        }
        return this.children;
    }

    /**
     * Find the sub-tile an {@link ILocation} would belong in.
     *
     * @param loc
     * @return
     */
    public Tile<T> getSubTile(ILocation loc) {
        Rectangle2D.Double tileSize = getSize();
        int splitFactor = tm.getSplitFactor();
        double relXPos = loc.getLon() - tileSize.getMinX();
        double relYPos = loc.getLat() - tileSize.getMinY();
        double subTileWidth = tileSize.getWidth() / splitFactor;
        double subTileHeight = tileSize.getHeight() / splitFactor;
        int xTile = (int) Math.floor(relXPos / subTileWidth);
        int yTile = (int) Math.floor(relYPos / subTileHeight);
        int index = yTile * splitFactor + xTile;
        Tile<T> tile = null;
        if (!getChildren().isEmpty()) {
            if (index > -1 && index < getChildren().size()) {
                tile = getChildren().get(index);
            } else if (index > getElements().size() - 1) {
                tile = getChildren().get(getChildren().size() - 1);
            } else if (index < 0) {
                tile = getChildren().get(0);
            }
        }
        return tile;
    }

    /**
     * Find out if a tile is loaded which means the data are in sync with any
     * data previously saved to file and in sync with all data added in memory
     * afterwards.
     *
     * @return
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Indicate if the data from disk have been loaded.
     *
     * @param isLoaded
     */
    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    /**
     * in meters
     *
     * @return
     */
    public Rectangle2D.Double getSize() {
        return size;
    }

    /**
     * Recursively retrieve all nodes stored in this tile.
     *
     * @return
     */
    public Set<T> getInnerNodes() {
        if (isLeaf) {
            return getElements();
        }
        Set<T> result = new HashSet<T>(tm.getMaxElementsPerTile() * 2);
        for (Tile<T> subTile : getChildren()) {
            result.addAll(subTile.getInnerNodes());
        }
        return result;
    }

    @Override
    public String toString() {
        return toStringHelper(false);
    }

    public String toDebugString() {
        return toStringHelper(true);
    }

    public String toStringHelper(boolean debug) {
        StringBuilder sb = new StringBuilder();
        int count = getElemCount();
        if (isLeaf) {
            sb.append(String
                    .format("Tile %s [%.15f ~ %.15f ; %.15f ~ %.15f], %d elements, %d%% full",
                    getID(),
                    getSize().getMinX(),
                    getSize().getMaxX(),
                    getSize().getMinY(),
                    getSize().getMaxY(),
                    count,
                    Math.round(count / (double) tm.getMaxElementsPerTile()
                    * 100.0)));
            if (debug) {
                for (T elem : getElements()) {
                    sb.append("\n\t").append(elem.toDebugString());
                }
            }
        } else {
            sb.append(String.format("Tile %s [%.15f ~ %.15f ; %.15f ~ %.15f]",
                    getID(), getSize().getMinX(), getSize().getMaxX(),
                    getSize().getMinY(), getSize().getMaxY()));
            for (Tile<T> subTile : getChildren()) {
                sb.append("\n  ").append(
                        subTile.toStringHelper(debug).replace("\n", "\n  "));
            }
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        result = prime * result + (isLeaf ? 1231 : 1237);
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        Tile other = (Tile) obj;
        if (ID == null) {
            if (other.ID != null) {
                return false;
            }
        } else if (!ID.equals(other.ID)) {
            return false;
        }
        if (isLeaf != other.isLeaf) {
            return false;
        }
        if (size == null) {
            if (other.size != null) {
                return false;
            }
        } else if (!size.equals(other.size)) {
            return false;
        }
        return true;
    }

    public Set<Tile<T>> getAllChildren() {
        Set<Tile<T>> resultSet = new HashSet<Tile<T>>();
        if (isLeaf) {
            resultSet.add(this);
            return resultSet;
        }
        for (Tile<T> child : getChildren()) {
            resultSet.addAll(child.getLeafChildren());
        }
        return resultSet;
    }

    public Set<Tile<T>> getLeafChildren() {
        Set<Tile<T>> resultSet = new HashSet<Tile<T>>();
        if (isLeaf) {
            return resultSet;
        }
        for (Tile<T> child : getChildren()) {
            resultSet.addAll(child.getLeafChildren());
        }
        return resultSet;
    }

    public boolean isRoot() {
        return "0".equals(getID());
    }
}
