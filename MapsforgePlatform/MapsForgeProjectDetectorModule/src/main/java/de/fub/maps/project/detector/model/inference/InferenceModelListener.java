/*
 * Copyright (C) 2013 Serdar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fub.maps.project.detector.model.inference;

import java.util.EventListener;

/**
 *
 * @author Serdar
 */
public interface InferenceModelListener extends EventListener {

    public void startedTraining();

    public void finishedTraining();

    public void startedCrossValidation();

    public void finishedCrossValidation();

    public void startedClustering();

    public void finishedClustering();
}
