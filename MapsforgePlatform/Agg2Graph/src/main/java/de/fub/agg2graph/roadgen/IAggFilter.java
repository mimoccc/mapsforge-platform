/*******************************************************************************
 * Copyright (c) 2012 Johannes Mitlmeier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Affero Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/agpl-3.0.html
 * 
 * Contributors:
 *     Johannes Mitlmeier - initial API and implementation
 ******************************************************************************/
package de.fub.agg2graph.roadgen;

import de.fub.agg2graph.agg.AggContainer;

/**
 * Filtering methods for the aggregated data.
 * 
 * @author Johannes Mitlmeier
 * 
 */
public interface IAggFilter {

	public void filter(AggContainer agg);
}