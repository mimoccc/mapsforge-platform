/*
 * Copyright 2013 Serdar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fub.agg2graphui.layers;

import de.fub.agg2graph.roadgen.Road;
import de.fub.agg2graph.roadgen.Road.RoadType;
import de.fub.agg2graph.roadgen.RoadNetwork;
import de.fub.agg2graph.structs.ILocation;
import de.fub.agg2graph.ui.gui.RenderingOptions;
import de.fub.agg2graphui.controller.AbstractLayer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author Serdar
 */
public class RoadNetworkLayer extends AbstractLayer<RoadNetwork> {

    private RoadType roadType = RoadType.UNKNOWN;

    public RoadNetworkLayer() {
        super("Roads", "Roads", new RenderingOptions());
        init();
    }

    public RoadNetworkLayer(String name, String description) {
        super(name, description, new RenderingOptions());
        init();
    }

    private void init() {
        getRenderingOptions().setRenderingType(RenderingOptions.RenderingType.ALL);
        getRenderingOptions().setzIndex(5);
        getRenderingOptions().setOpacity(1);
        getRenderingOptions().setStrokeBaseWidthFactor(1.5f);
    }

    public void setRoadType(RoadType roadType) {
        this.roadType = roadType;
    }

    public RoadType getRoadType() {
        return this.roadType;
    }

    @Override
    protected void drawDrawables(Graphics2D graphics, Rectangle rectangle) {
        for (RoadNetwork roadNetwork : getItemList()) {
            for (Road r : roadNetwork.getRoads()) {
                if (r.isVisible() && r.getType() == getRoadType()) {
                    List<? extends ILocation> nodes = r.getNodes();
                    for (int i = 1; i < nodes.size(); i++) {
                        drawLine(nodes.get(i - 1),
                                nodes.get(i),
                                null,
                                getRenderingOptions(), 1,
                                (i == nodes.size() - 1 && r.isOneWay()));
                    }
                }
            }
        }
    }
}
