package com.example.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Space {
    String type;
    List<List<List<Double>>> coordinates;
}
