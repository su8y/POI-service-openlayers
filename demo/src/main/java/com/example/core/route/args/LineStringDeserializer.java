package com.example.core.route.args;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class LineStringDeserializer extends JsonDeserializer<LineString> {
    private final GeometryFactory geometryFactory;
    private static final GeometryFactory DEFAULT_GEOMETRY_FACTORY = getDefaultGeometryFactory();

    private static GeometryFactory getDefaultGeometryFactory() {
        return new GeometryFactory();
    }

    public LineStringDeserializer(@Nullable GeometryFactory geometryFactory) {
        this.geometryFactory = Optional.ofNullable(geometryFactory).orElse(DEFAULT_GEOMETRY_FACTORY);
    }


    @Override
    public LineString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        List<Coordinate> coordinates = new ArrayList<>();
        ArrayNode arrayNode = (ArrayNode) node;
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode coordinateNode = arrayNode.get(i);
            double x = coordinateNode.get(0).asDouble();
            double y = coordinateNode.get(1).asDouble();
            coordinates.add(new Coordinate(x, y));
        }
        Coordinate[] array = coordinates.toArray(Coordinate[]::new);
        LineString lineString = geometryFactory.createLineString(array);
        lineString.setSRID(4326);
        return lineString;

    }
}
