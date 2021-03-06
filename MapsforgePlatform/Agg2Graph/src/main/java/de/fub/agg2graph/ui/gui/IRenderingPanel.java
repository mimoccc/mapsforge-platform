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
package de.fub.agg2graph.ui.gui;

import de.fub.agg2graph.ui.gui.jmv.Layer;
import de.fub.agg2graph.ui.gui.jmv.TestUI;
import java.util.List;

public interface IRenderingPanel {
	public List<Layer> getLayers();

	public TestUI getUi();

	public void setUi(TestUI ui);

	public void addLayer(Layer layer);

}
