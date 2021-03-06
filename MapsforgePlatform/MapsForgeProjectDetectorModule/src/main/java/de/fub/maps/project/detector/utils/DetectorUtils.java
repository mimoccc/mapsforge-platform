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
package de.fub.maps.project.detector.utils;

import de.fub.maps.project.detector.filetype.DetectorDataObject;
import de.fub.maps.project.detector.model.Detector;
import de.fub.maps.project.detector.model.inference.AbstractInferenceModel;
import de.fub.maps.project.detector.model.inference.processhandler.InferenceModelProcessHandler;
import de.fub.maps.project.detector.model.xmls.DetectorDescriptor;
import de.fub.maps.project.detector.model.xmls.InferenceModelDescriptor;
import de.fub.maps.project.detector.model.xmls.ProcessHandlerDescriptor;
import de.fub.maps.project.detector.model.xmls.Property;
import de.fub.maps.project.utils.MapsProjectUtils;
import de.fub.utilsmodule.xml.jax.JAXBUtil;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.xml.bind.JAXBException;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Serdar
 */
public class DetectorUtils {

    private static final Object DETECTOR_FILE_OPERATION_MUTEX = new Object();
    private static final Logger LOG = Logger.getLogger(DetectorUtils.class.getName());
    private static RequestProcessor requestProcessor;

    public static <T> T createInstance(Class<T> clazz, String className) {
        return createInstance(clazz, className, (Object[]) null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<T> clazz, String className, Object... arguments) {
        T instance = null;
        try {
            ClassLoader classLoader = Lookup.getDefault().lookup(ClassLoader.class);
            Class<?> forName = classLoader.loadClass(className);
            Class<T> cl = (Class<T>) forName;

            if (arguments == null || arguments.length == 0) {
                instance = cl.newInstance();
            } else {
                ArrayList<Class<?>> argumentTypes = new ArrayList<Class<?>>();
                for (Object object : arguments) {
                    argumentTypes.add(object.getClass());
                }
                Constructor<T> constructor = null;

                while (cl != null && instance == null) {
                    try {
                        constructor = cl.getConstructor(argumentTypes.toArray(new Class[argumentTypes.size()]));
                        instance = constructor.newInstance(arguments);
                    } catch (NoSuchMethodException ex) {
                        cl = (Class<T>) cl.getSuperclass();
                    }
                }
            }
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            Lookup.Result<T> lookupResult = Lookup.getDefault().lookupResult(clazz);
            for (T task : lookupResult.allInstances()) {
                Class<T> cl = (Class<T>) task.getClass();
                if (task.getClass().getName().equals(className)) {
                    try {
                        if (arguments == null || arguments.length == 0) {
                            instance = cl.newInstance();
                        } else {
                            ArrayList<Class<?>> argumentTypes = new ArrayList<Class<?>>();
                            for (Object object : arguments) {
                                argumentTypes.add(object.getClass());
                            }
                            Constructor<T> constructor = null;

                            while (cl != null && instance == null) {
                                try {
                                    constructor = cl.getConstructor(argumentTypes.toArray(new Class[argumentTypes.size()]));
                                    instance = constructor.newInstance(arguments);
                                } catch (NoSuchMethodException ex1) {
                                    cl = (Class<T>) cl.getSuperclass();
                                } catch (IllegalArgumentException ex1) {
                                    Exceptions.printStackTrace(ex1);
                                } catch (InvocationTargetException ex1) {
                                    Exceptions.printStackTrace(ex1);
                                }
                            }
                        }
                    } catch (InstantiationException ex1) {
                        Exceptions.printStackTrace(ex1);
                    } catch (IllegalAccessException ex1) {
                        Exceptions.printStackTrace(ex1);
                    }
                }
            }
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        return instance;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T getValue(Class<T> clazz, Property property) {
        T instance = null;
        if (clazz.getName().equals(Boolean.class.getName())) {
            instance = (T) Boolean.valueOf(property.getValue());
        } else if (clazz.getName().equals(Double.class.getName())) {
            instance = (T) Double.valueOf(property.getValue());
        } else if (clazz.getName().equals(Integer.class.getName())) {
            instance = (T) Integer.valueOf(property.getValue());
        } else if (clazz.getName().equals(String.class.getName())) {
            instance = (T) property.getValue();
        } else if (clazz.getName().equals(Color.class.getName())) {
            instance = (T) new Color(Integer.parseInt(property.getValue(), 16));
        } else if (clazz.getName().equals(Long.class.getName())) {
            instance = (T) Long.valueOf(property.getValue());
        } else if (clazz.isEnum()) {
            T[] enumConstants = clazz.getEnumConstants();
            for (T enu : enumConstants) {
                if (enu.toString().equals(property.getValue())) {
                    instance = enu;
                    break;
                }
            }
        } else {
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor(String.class);
                instance = constructor.newInstance(property.getValue());
            } catch (NoSuchMethodException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SecurityException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InstantiationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return instance;
    }

    @NbBundle.Messages({
        "# {0} - filepath",
        "CLT_File_not_found=Couldn't find associated xml process descriptor file at path: {0}"})
    @SuppressWarnings("unchecked")
    public static <T> T getXmlDescriptor(Class<T> destClass, Class<?> sourceClass) throws IOException {
        T descriptor = null;
        String filePatn = MessageFormat.format("/{0}.xml", sourceClass.getName().replaceAll("\\.", "/"));
        InputStream resourceAsStream = sourceClass.getResourceAsStream(filePatn);
        if (resourceAsStream != null) {
            try {
                javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(destClass);
                javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
                descriptor = (T) unmarshaller.unmarshal(resourceAsStream); //NOI18N
            } catch (javax.xml.bind.JAXBException ex) {
                throw new IOException(ex);
            } finally {
                resourceAsStream.close();
            }
        } else {
            throw new FileNotFoundException(Bundle.CLT_File_not_found(filePatn));
        }
        return descriptor;
    }

    public static ProcessHandlerDescriptor createProcessHandler(Class<? extends InferenceModelProcessHandler> aClass) {
        ProcessHandlerDescriptor descriptor = null;

        return descriptor;
    }

    @SuppressWarnings("unchecked")
    public static InferenceModelProcessHandler createProcessHandler(ProcessHandlerDescriptor descriptor, Detector detector) {
        InferenceModelProcessHandler model = null;
        try {
            if (descriptor != null && detector != null) {
                Class<?> clazz = Class.forName(descriptor.getJavaType());

                if (AbstractInferenceModel.class
                        .isAssignableFrom(clazz)) {
                    Class<? extends InferenceModelProcessHandler> abstractInferenceModel = (Class<? extends InferenceModelProcessHandler>) clazz;
                    Constructor<? extends InferenceModelProcessHandler> constructor = abstractInferenceModel.getConstructor(Detector.class);
                    model = constructor.newInstance(detector);
                }
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
//            Exceptions.printStackTrace(ex);
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
//            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
//            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
//            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
//            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
//            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
//            Exceptions.printStackTrace(ex);
        }

        return model;
    }

    @SuppressWarnings("unchecked")
    public static AbstractInferenceModel createInferenceModel(InferenceModelDescriptor descriptor, Detector detector) {
        AbstractInferenceModel model = null;
        try {
            if (descriptor != null && detector != null) {
                Class<?> clazz = Class.forName(descriptor.getJavaType());

                if (AbstractInferenceModel.class
                        .isAssignableFrom(clazz)) {
                    Class<? extends AbstractInferenceModel> abstractInferenceModel = (Class<? extends AbstractInferenceModel>) clazz;
                    Constructor<? extends AbstractInferenceModel> constructor = abstractInferenceModel.getConstructor(Detector.class);
                    model = constructor.newInstance(detector);
                }
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
        } catch (SecurityException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            LOG.log(Level.FINE, ex.getMessage(), ex);
        }

        return model;
    }

    public static synchronized Project findProject(FileObject fileObject) {
        return MapsProjectUtils.findProject(fileObject);
    }

    public static FileObject findFileObject(FileObject detectorFileObject, String relativePathInProject) {
        FileObject fileObject = null;
        if (detectorFileObject != null && detectorFileObject.getParent() != null) {
            try {
                Project project = findProject(detectorFileObject);

                if (project != null) {
                    fileObject = project.getProjectDirectory().getFileObject(relativePathInProject);
                }
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            for (Project project : OpenProjects.getDefault().getOpenProjects()) {
                fileObject = project.getProjectDirectory().getFileObject(relativePathInProject);
                if (fileObject != null) {
                    break;
                }
            }
        }
        return fileObject;
    }

    /**
     * Method to override the original detector description with the copy
     * instance.
     *
     * @param original - the detector instance that will be over written with
     * the copy instance.
     * @param copy - detector copy instance.
     */
    public static void mergeDetector(Detector original, Detector copy) {
        saveDetector(original.getDataObject(), copy.getDetectorDescriptor());
    }

    public static void saveDetector(DetectorDataObject dataObject) throws JAXBException, IOException {
        saveDetector(dataObject, dataObject.getDetectorDescriptor());
    }

    public static void saveDetector(final DataObject dataObject, final DetectorDescriptor descriptor) {
        saveDetector(dataObject.getPrimaryFile(), descriptor);
    }

    public static void saveDetector(final FileObject fileObject, final DetectorDescriptor descriptor) {
        FileUtil.runAtomicAction(new Runnable() {
            @Override
            public void run() {
                try {
                    JAXBUtil.saveDetector(fileObject, descriptor);
                } catch (JAXBException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    public static DetectorDescriptor getDetectorDescriptor(DataObject dataObject) throws JAXBException, IOException {
        return getDetectorDescriptor(dataObject.getPrimaryFile());
    }

    public static DetectorDescriptor getDetectorDescriptor(FileObject fileObject) throws JAXBException, IOException {
        return JAXBUtil.createDescriptor(DetectorDescriptor.class, fileObject);
    }

    public static Detector copyInstance(Detector detector) throws DetectorCopyException {
        Detector copy = null;
        DetectorDescriptor detectorDescriptor = detector.getDetectorDescriptor();
        try {
            if (detectorDescriptor == null) {
                throw new DetectorCopyException("descriptor of detector is null!");
            }
            File copyFile = File.createTempFile("detector", ".dec");
            FileObject fileObject = FileUtil.toFileObject(copyFile);
            saveDetector(fileObject, detectorDescriptor);
            DataObject dataObject = DataObject.find(fileObject);
            copy = dataObject.getNodeDelegate().getLookup().lookup(Detector.class);
            if (copy == null) {
                throw new DetectorCopyException(MessageFormat.format("Failed to create a copy of detector {0}", detector.getDetectorDescriptor().getName()));
            }
        } catch (IOException ex) {
            throw new DetectorCopyException(ex.getMessage(), ex);
        }

        return copy;
    }

    public static FileObject getDatasourceFileObject() {
        FileObject fileObject = null;
        ClassLoader classLoader = Lookup.getDefault().lookup(ClassLoader.class);
        try {
            Preferences preferences = NbPreferences.forModule(classLoader.loadClass("de.fub.mapsforge.project.datasource.MapsForgeDatasourceNodeFactory"));
            String filePath = preferences.get("GPX Datasource", null);
            if (filePath != null) {
                File file = new File(filePath);
                fileObject = FileUtil.toFileObject(file);
            }
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }

        return fileObject;
    }

    public static class DetectorCopyException extends Exception {

        private static final long serialVersionUID = 1L;

        public DetectorCopyException() {
        }

        public DetectorCopyException(String message) {
            super(message);
        }

        public DetectorCopyException(String message, Throwable cause) {
            super(message, cause);
        }

        public DetectorCopyException(Throwable cause) {
            super(cause);
        }

        public DetectorCopyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    public static RequestProcessor getDefaultRequestProcessor() {
        if (requestProcessor == null) {
            requestProcessor = new RequestProcessor(Detector.class.getName(), Runtime.getRuntime().availableProcessors() * 2);
        }
        return requestProcessor;
    }
}
