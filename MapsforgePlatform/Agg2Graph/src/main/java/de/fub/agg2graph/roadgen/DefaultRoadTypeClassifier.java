/*******************************************************************************
   Copyright 2013 Johannes Mitlmeier

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
******************************************************************************/
package de.fub.agg2graph.roadgen;

public class DefaultRoadTypeClassifier implements IRoadTypeClassifier {
	public double minWeightPrimary = 4;
	public double minWeightSecondary = 2;
	public double minWidthPrimary = 4;
	public double minWidthSecondary = 2;

	@Override
	public void classify(RoadNetwork network) {
		for (Road road : network.roads) {
			if (road.getType() != Road.RoadType.UNKNOWN) {
				continue;
			}
			/*
			 * classification algorithm with respect to weight and average
			 * distance saved in the AggConnections in the Road
			 */
			if (road.getWeight() >= minWeightPrimary
					&& road.getAvgDist() >= minWeightPrimary) {
				road.setType(Road.RoadType.PRIMARY);
			} else if (road.getWeight() >= minWeightPrimary
					&& road.getAvgDist() >= minWeightPrimary) {
				road.setType(Road.RoadType.SECONDARY);
			} else {
				road.setType(Road.RoadType.TERTIARY);
			}
		}
	}
}
