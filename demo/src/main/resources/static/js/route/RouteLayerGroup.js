class RouteLayerGroup {
    map_instance
    pathColor = {
        0: 'blue',
        1: 'black',
        2: 'red'
    }

    lineSource = new ol.source.Vector()
    lineLayer = new ol.layer.Vector({
        source: this.lineSource
    });

    routePointSource = new ol.source.Vector()
    routePointLayer = new ol.layer.Vector({
        source: this.routePointSource
    });
    routeLayerGroup = new ol.layer.Group({
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM()
            }),
            this.lineLayer,
            this.routePointLayer
        ]
    });

    constructor(_map_instance) {
        this.map_instance = _map_instance;
    }

    lineStyle = (color) => new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: this.pathColor[color],
            width: 10
        })
    });

    __initRoute__() {
        this.map_instance.changeLayerGroup(this.routeLayerGroup)
    }

    clear() {
        // map.removeLayer(lineLayer)
        this.lineSource.clear()
        this.routePointSource.clear();
    }

    addStartPoint({coord, name}) {
        const marker_style = new ol.style.Style({
            image: new ol.style.Icon({
                src: '/images/start.png',
                scale: 0.1
            }),
        });
        let pointCoord = ol.proj.fromLonLat(coord);
        let pointFeature = new ol.Feature({
            geometry: new ol.geom.Point(pointCoord),
            scale: 0.1
        })
        pointFeature.setStyle(marker_style)
        this.routePointSource.addFeature(pointFeature);
    }

    addGoalPoint({coord, name}) {
        const marker_style = new ol.style.Style({
            image: new ol.style.Icon({
                src: '/images/goal-flag.png',
                scale: 0.1
            }),
        });
        let pointCoord = ol.proj.fromLonLat(coord);
        let pointFeature = new ol.Feature({
            geometry: new ol.geom.Point(pointCoord),
        })
        pointFeature.setStyle(marker_style);
        this.routePointSource.addFeature(pointFeature);
    }

    addWayPoint({coords}) {
        const marker_style = new ol.style.Style({
            image: new ol.style.Icon({
                src: 'http://maps.google.com/intl/en_us/mapfiles/ms/micons/orange-dot.png'
            })
        });
        const waypointsFeatures = coords.map(coord => {
            let waypointFeature = new ol.Feature({
                geometry: new ol.geom.Point(ol.proj.fromLonLat(coord.location)),
            })
            waypointFeature.setStyle(marker_style)
            return waypointFeature;
        })
        this.routePointSource.addFeatures(waypointsFeatures);
    }

// 2nd array를 통한 Line Feature 생성
    addPath(linePaths, color) {
        var lineCoordinates = linePaths.map(point => ol.proj.fromLonLat(point));
        var lineFeature = new ol.Feature({
            geometry: new ol.geom.LineString(lineCoordinates),
        })
        lineFeature.setStyle(this.lineStyle(color))
        this.lineSource.addFeature(lineFeature)
        return lineFeature
    }
}

export default RouteLayerGroup