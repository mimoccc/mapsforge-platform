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
package de.fub.utilsmodule.node.property;

import de.fub.utilsmodule.beans.PropertyDescriptor;
import de.fub.utilsmodule.beans.PropertyDescriptorUtil;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Serdar
 */
public class NodeProperty extends PropertySupport.ReadWrite<Object> {

    private final PropertyDescriptor property;
    private Object value = null;

    @SuppressWarnings("unchecked")
    public NodeProperty(PropertyDescriptor property) {
        super(property.getName(), getClassOf(property), property.getName(), property.getDescription());
        this.property = property;
        initValue();
    }

    private void initValue() {
        try {
            ClassLoader classLoader = Lookup.getDefault().lookup(ClassLoader.class);
            value = PropertyDescriptorUtil.getValue(classLoader.loadClass(property.getJavaType()), property);
        } catch (ClassNotFoundException ex) {
            value = PropertyDescriptorUtil.getValue(Object.class, property);
        }
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return value;
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        value = val;
        if (val != null && !val.equals(property.getValue())) {
            property.setValue(String.valueOf(val));
        } else {
            property.setValue(null);
        }
    }

    private static Class getClassOf(PropertyDescriptor property) {
        Class<?> clazz = Object.class;
        try {
            if (property != null && property.getJavaType() != null) {
                ClassLoader classLoader = Lookup.getDefault().lookup(ClassLoader.class);
                clazz = classLoader.loadClass(property.getJavaType());
            } else {
                clazz = Object.class;
            }
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return clazz;
    }
}
