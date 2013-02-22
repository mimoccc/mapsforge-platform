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

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import de.fub.agg2graph.graph.RamerDouglasPeuckerFilter;
import de.fub.agg2graph.input.GPSCleaner;
import de.fub.agg2graph.input.GPXReader;
import de.fub.agg2graph.management.MiniProfiler;
import de.fub.agg2graph.structs.GPSSegment;
import de.fub.agg2graph.ui.gui.jmv.TestUI;

public class CalcThread extends Thread {
	private String task;
	private final TestUI parent;

	public static final Map<String, Integer> levels;
	static {
		levels = new HashMap<String, Integer>();
		levels.put("input", 0);
		levels.put("clean", 1);
		levels.put("agg", 2);
		levels.put("road", 3);
		levels.put("osm", 4);
	}

	public static String[] stepNames = new String[] { "Input", "Clean",
			"Aggregation", "Road Gen", "OSM Export" };

	public CalcThread(TestUI parent) {
		this.parent = parent;
	}

	public void setTask(String task) {
		this.task = task;
	}

	@Override
	public void run() {

		parent.setLoading(true);
		System.out.println(MiniProfiler.print(String.format("before step %s",
				task)));
		UIStepStorage stepStorage = parent.uiStepStorage;
		if (task.equals("input")) {
			stepStorage.clear(levels.get(task));
			stepStorage.inputSegmentList = new ArrayList<GPSSegment>();

			File folder = (File) parent.sourceFolderCombo.getSelectedItem();
			File[] files = folder.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".gpx");
				}
			});
			Arrays.sort(files);

			for (File file : files) {
				if (parent.deselectedTraceFiles.contains(file)) {
					continue;
				}
				System.out.println("processing file " + file);
				List<GPSSegment> segments = GPXReader.getSegments(file);
				if (segments == null) {
					System.out.println("Bad file: " + file);
					continue;
				}
				for (GPSSegment segment : segments) {
					parent.parseDim(segment);
					segment.addIDs("I" + stepStorage.inputSegmentList.size());
					stepStorage.rawLayer.addObject(segment);
					stepStorage.inputSegmentList.add(segment);
				}
			}

			System.out.println(String.format("Loaded %d gps segments",
					stepStorage.inputSegmentList.size()));

			parent.getMainPanel().showArea(parent.dataBoundingBox);
			parent.getLayerManager().setSize(new Dimension(1, 1));
			repaintEverything();
		} else if (task.equals("clean")) {
			stepStorage.clear(levels.get(task));
			stepStorage.cleanSegmentList = new ArrayList<GPSSegment>();

			// clean data
			GPSCleaner gpsCleaner = stepStorage.getGpsCleaner();
			RamerDouglasPeuckerFilter rdpf = stepStorage
					.getCleaningRamerDouglasPeuckerFilter();

			for (GPSSegment segment : stepStorage.inputSegmentList) {
				for (GPSSegment cleanSegment : gpsCleaner.clean(segment)) {
					// run through Douglas-Peucker here (slightly modified
					// perhaps to avoid too long edges)
					cleanSegment = rdpf.simplify(cleanSegment);
					stepStorage.cleanLayer.addObject(cleanSegment);
					stepStorage.cleanSegmentList.add(cleanSegment);
				}
			}
			repaintEverything();
		} else if (task.equals("agg")) {
			stepStorage.clear(levels.get(task));
			int counter = 0;
			// use clean data if cleaning step has been performed
			if (stepStorage.levelReached >= 1) {
				for (GPSSegment cleanSegment : stepStorage.cleanSegmentList) {
					System.out.println(String.format("adding segment no. %d",
							++counter));
					stepStorage.getAggContainer().addSegment(cleanSegment);
				}
			} else {
				for (GPSSegment inputSegment : stepStorage.inputSegmentList) {
					System.out.println(String.format("adding segment no. %d",
							++counter));
					stepStorage.getAggContainer().addSegment(inputSegment);
				}
			}
			repaintEverything();
		} else if (task.equals("road")) {
			stepStorage.clear(levels.get(task));
			stepStorage.getRoadNetwork().parse(stepStorage.getAggContainer(),
					stepStorage);
			repaintEverything();
		} else if (task.equals("osm")) {
			stepStorage.clear(levels.get(task));
			if (stepStorage.getExporter().getTargetFile() == null) {
				stepStorage.getExporter()
						.setTargetFile(new File("osm-out.xml"));
			}
			stepStorage.getExporter().export(stepStorage.getRoadNetwork());
			if (stepStorage.isOpenOsmExportFile()
					&& stepStorage.getExporter().getTargetFile().exists()) {
				System.out.println("opening file "
						+ stepStorage.getExporter().getTargetFile());
				try {
					Desktop.getDesktop().open(
							stepStorage.getExporter().getTargetFile());
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}
		stepStorage.levelReached = levels.get(task);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				parent.tabbedPane.setSelectedIndex((levels.get(task) + 1)
						% levels.size());
			}
		});
		System.out.println(MiniProfiler.print(String.format("after step %s",
				task)));
		parent.setLoading(false);
	}

	private void repaintEverything() {
		parent.setPainting(true);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				parent.getLayerManager().repaintAllLayers();
			}
		});
	}
}
