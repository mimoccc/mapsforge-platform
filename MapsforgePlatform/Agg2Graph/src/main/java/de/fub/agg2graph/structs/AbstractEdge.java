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
package de.fub.agg2graph.structs;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class with common methods and attributes for all classes modelling
 * different types of edges throughout the codebase.
 * 
 * @author Johannes Mitlmeier
 * 
 */
public abstract class AbstractEdge<T extends ILocation> implements IEdge<T>,
		Hideable {
	protected String ID;
	protected T from = null;
	protected T to = null;
	protected boolean visible = true;

	public AbstractEdge() {
		super();
	}

	public AbstractEdge(T from, T to) {
		this.setFrom(from);
		this.setTo(to);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + from.toString()
				+ " -> " + to.toString();
	}

	@Override
	public String toDebugString() {
		return this.getClass().getSimpleName() + ": " + from.toDebugString()
				+ " -> " + to.toDebugString();
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
		@SuppressWarnings("unchecked")
		AbstractEdge<? extends ILocation> other = (AbstractEdge<? extends ILocation>) obj;
		if (!this.getFrom().equals(other.getFrom())) {
			return false;
		}
		if (!this.getTo().equals(other.getTo())) {
			return false;
		}
		return true;
	}

	@Override
	public void setID(String ID) {
		this.ID = ID;
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public T getFrom() {
		return from;
	}

	@Override
	public T getTo() {
		return to;
	}

	@Override
	public IEdge<T> setFrom(T from) {
		this.from = from;
		return this;
	}

	@Override
	public IEdge<T> setTo(T to) {
		this.to = to;
		return this;
	}

	@Override
	public double getLength() {
		return GPSCalc.getDistance(getFrom(), getTo());
	}

	public List<T> toPointList() {
		return toPointList(this);
	}

	public List<T> toPointList(IEdge<T> edge) {
		if (edge == null) {
			return null;
		}
		List<T> result = new ArrayList<T>(2);
		result.add(edge.getFrom());
		result.add(edge.getTo());
		return result;
	}

	public List<T> toPointList(List<IEdge<T>> edges) {
		ArrayList<T> result = new ArrayList<T>();
		T lastNode = null, node = null;
		for (IEdge<T> edge : edges) {
			if (edge == null) {
				continue;
			}
			if (edge.getFrom() != null) {
				node = edge.getFrom();
				if (!node.equals(lastNode)) {
					result.add(node);
					lastNode = node;
				}
			}
			if (edge.getTo() != null) {
				node = edge.getTo();
				if (!node.equals(lastNode)) {
					result.add(node);
					lastNode = node;
				}
			}
		}
		return result;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}
}
