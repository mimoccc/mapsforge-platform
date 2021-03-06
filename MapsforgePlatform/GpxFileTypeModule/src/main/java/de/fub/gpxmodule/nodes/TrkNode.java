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
package de.fub.gpxmodule.nodes;

import de.fub.gpxmodule.xml.Trk;
import java.beans.IntrospectionException;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages("CLT_no_name_available=No name available")
public class TrkNode extends BeanNode<Trk> {

    public TrkNode(Trk t) throws IntrospectionException {
        this(t, Children.LEAF);
    }

    public TrkNode(Trk t, Children chldrn) throws IntrospectionException {
        this(t, chldrn, Lookups.singleton(t));
    }

    public TrkNode(Trk t, Children chldrn, Lookup lkp) throws IntrospectionException {
        super(t, chldrn, lkp);

        if (t.getName() == null) {
            setDisplayName(Bundle.CLT_no_name_available());
        }
    }
}
