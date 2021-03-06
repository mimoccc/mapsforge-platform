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
package de.fub.maps.project.openstreetmap.service;

import java.text.MessageFormat;
import javax.ws.rs.core.MediaType;

/**
 * Jersey REST client generated for REST resource:Geocoding Service [geo]<br>
 * USAGE:
 * <pre>
 *        NewJerseyClient client = new NewJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * Web client instance to access OSM data via the official OSM and Overpass-Api
 * RESTful webservice.
 *
 * @author Serdar
 */
public class OpenstreetMapService {

    private final com.sun.jersey.api.client.WebResource webResource;
    private final com.sun.jersey.api.client.Client client;
    private final com.sun.jersey.api.client.WebResource webOverpassResource;
    private final com.sun.jersey.api.client.Client overpassClient;
    private static final String BASE_URI = "http://api.openstreetmap.org/";
    private static final String OVERPASS_BASE_URI = "http://overpass-api.de/";

    public OpenstreetMapService() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = com.sun.jersey.api.client.Client.create(config);
        webResource = client.resource(BASE_URI).path("api/0.6/");
        overpassClient = com.sun.jersey.api.client.Client.create(config);
        webOverpassResource = overpassClient.resource(OVERPASS_BASE_URI).path("api/");
    }

    /**
     * Fetches all gpx data that are within the specified bounding box and page
     * number.
     *
     * @param responseType Class representing the response
     * @param String leftLong
     * @param String bottomLat
     * @param String rightLong
     * @param String topLat
     * @param String page
     * @return response object (instance of responseType class)
     */
    public <T> T getGpsTracks(Class<T> responseType,
            String leftLong,
            String bottomLat,
            String rightLong,
            String topLat,
            String page) throws com.sun.jersey.api.client.UniformInterfaceException {
        javax.ws.rs.core.MultivaluedMap<String, String> qParams = new com.sun.jersey.api.representation.Form();
        qParams.add("bbox", MessageFormat.format("{0},{1},{2},{3}", leftLong, bottomLat, rightLong, topLat));
        qParams.add("page", page);
        return webResource.path("trackpoints").queryParams(qParams).accept(MediaType.TEXT_XML, MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param leftLong
     * @param bottomLat
     * @param rightLong
     * @param topLat
     * @return
     */
    public <T> T getOSMMap(Class<T> responseType,
            String leftLong,
            String bottomLat,
            String rightLong,
            String topLat) {
        javax.ws.rs.core.MultivaluedMap<String, String> qParams = new com.sun.jersey.api.representation.Form();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder = stringBuilder
                .append("(")
                .append("node")
                .append("({0},{1},{2},{3});")
                .append("way")
                .append("[\"highway\"~\"primary|secondary|tertiary|motorway|trunk|living_street|pedestrian|residential|unclassified|service|track|raceway|path|footway\"]")
                .append("({0},{1},{2},{3});")
                .append(");")
                .append("(._;")
                .append("way")
                .append("[\"landuse\"~\"residential\"]")
                .append("({0},{1},{2},{3});")
                .append(");")
                .append("(._;")
                .append("way")
                .append("[\"railway\"~\"rail|abandoned|construction|disused|funicular|light_rail|miniature|monorail|narrow_gauge|preserved\"]")
                .append("({0},{1},{2},{3});")
                .append(");")
                .append("(._;")
                .append("way[\"railway\"~\"tram\"]({0},{1},{2},{3});")
                .append(");")
                .append("(._;")
                .append("way[\"railway\"~\"subway\"]({0},{1},{2},{3});")
                .append(");")
                .append("out meta;");

        String parameter = MessageFormat.format(stringBuilder.toString(),
                bottomLat, leftLong, topLat, rightLong);
        qParams.add("data", parameter);
        T post = webOverpassResource.path("interpreter")
                .accept(MediaType.TEXT_XML, MediaType.APPLICATION_XML, "application/osm3s+xml")
                .post(responseType, qParams);
        return post;
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param leftLong
     * @param bottomLat
     * @param rightLong
     * @param topLat
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getOSMHighwayMap(Class<T> responseType,
            String leftLong,
            String bottomLat,
            String rightLong,
            String topLat) {
        javax.ws.rs.core.MultivaluedMap<String, String> qParams = new com.sun.jersey.api.representation.Form();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder = stringBuilder
                .append("(")
                .append("node")
                .append("({0},{1},{2},{3});")
                .append("way")
                .append("[\"highway\"~\"primary|secondary|tertiary|motorway|trunk|living_street|pedestrian|residential|unclassified|service|track|raceway|path|footway\"]")
                .append("({0},{1},{2},{3});")
                .append(");")
                .append("(._;")
                .append("way")
                .append("[\"landuse\"=\"residential\"]")
                .append("({0},{1},{2},{3})")
                .append(");")
                .append("out meta;");
        String parameter = MessageFormat.format(stringBuilder.toString(),
                bottomLat, leftLong, topLat, rightLong);
        qParams.add("data", parameter);
        T post = webOverpassResource.path("interpreter")
                .accept(MediaType.TEXT_XML, MediaType.APPLICATION_XML, "application/osm3s+xml")
                .post(responseType, qParams);
        return post;
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param leftLong
     * @param bottomLat
     * @param rightLong
     * @param topLat
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getOSMTrainMap(Class<T> responseType,
            String leftLong,
            String bottomLat,
            String rightLong,
            String topLat) {
        javax.ws.rs.core.MultivaluedMap<String, String> qParams = new com.sun.jersey.api.representation.Form();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(")
                .append("node")
                .append("({0},{1},{2},{3});")
                .append("way")
                .append("[\"railway\"~\"rail|abandoned|construction|disused|funicular|light_rail|miniature|monorail|narrow_gauge|preserved\"]")
                .append("({0},{1},{2},{3});")
                .append(");")
                .append("out meta;");
        String parameter = MessageFormat.format(stringBuilder.toString(),
                bottomLat, leftLong, topLat, rightLong);
        qParams.add("data", parameter);
        T post = webOverpassResource.path("interpreter")
                .accept(MediaType.TEXT_XML, MediaType.APPLICATION_XML, "application/osm3s+xml")
                .post(responseType, qParams);
        return post;
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param leftLong
     * @param bottomLat
     * @param rightLong
     * @param topLat
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getOSMTramMap(Class<T> responseType,
            String leftLong,
            String bottomLat,
            String rightLong,
            String topLat) {
        javax.ws.rs.core.MultivaluedMap<String, String> qParams = new com.sun.jersey.api.representation.Form();
        StringBuilder stringBuilder = new StringBuilder()
                .append("(")
                .append("node({0},{1},{2},{3});")
                .append("way[\"railway\"=\"tram\"]({0},{1},{2},{3});")
                .append(");").
                append("out meta;");
        String parameter = MessageFormat.format(stringBuilder.toString(),
                bottomLat, leftLong, topLat, rightLong);
        qParams.add("data", parameter);
        T post = webOverpassResource.path("interpreter")
                .accept(MediaType.TEXT_XML, MediaType.APPLICATION_XML, "application/osm3s+xml")
                .post(responseType, qParams);
        return post;
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param leftLong
     * @param bottomLat
     * @param rightLong
     * @param topLat
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getOSMSubwayMap(Class<T> responseType,
            String leftLong,
            String bottomLat,
            String rightLong,
            String topLat) {
        javax.ws.rs.core.MultivaluedMap<String, String> qParams = new com.sun.jersey.api.representation.Form();
        StringBuilder stringBuilder = new StringBuilder()
                .append("(")
                .append("node({0},{1},{2},{3});")
                .append("way[\"railway\"=\"subway\"]({0},{1},{2},{3});")
                .append(");")
                .append("out meta;");
        String parameter = MessageFormat.format(stringBuilder.toString(),
                bottomLat, leftLong, topLat, rightLong);
        qParams.add("data", parameter);
        T post = webOverpassResource.path("interpreter")
                .accept(MediaType.TEXT_XML, MediaType.APPLICATION_XML, "application/osm3s+xml")
                .post(responseType, qParams);
        return post;
    }

    public <T> T getOSMBusMap(Class<T> responseType,
            String leftLong,
            String bottomLat,
            String rightLong,
            String topLat) {
        javax.ws.rs.core.MultivaluedMap<String, String> qParams = new com.sun.jersey.api.representation.Form();
        StringBuilder stringBuilder = new StringBuilder()
                .append("(")
                .append("rel[\"route\"=\"bus\"](52.32442898,13.070297241,52.698441698,13.769302368);")
                .append("way(r);")
                .append("node(w);")
                .append(");")
                .append("out;");
        String parameter = MessageFormat.format(stringBuilder.toString(),
                bottomLat, leftLong, topLat, rightLong);
        qParams.add("data", parameter);
        T post = webOverpassResource.path("interpreter")
                .accept(MediaType.TEXT_XML, MediaType.APPLICATION_XML, "application/osm3s+xml")
                .post(responseType, qParams);
        return post;
    }

    public void close() {
        client.destroy();
        overpassClient.destroy();
    }
}
