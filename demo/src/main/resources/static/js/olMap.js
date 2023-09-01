class OlMap {
    map
    #view
    #defaultLayerGroup


    constructor(target) {
        this.#defaultLayerGroup = new ol.layer.Group({
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM()
                })
            ]
        });
        this.#view = new ol.View({
            center: [14149156.937231855, 4510270.163387867],
            zoom: 18

        })
        this.map = new ol.Map({
            target: target,
            view: this.#view
        })
        this.changeLayerGroup()
    }

    changeLayerGroup(layerGroup) {
        console.log("in changeLayerGroup",layerGroup)
        if (!layerGroup) this.map.setLayerGroup(this.#defaultLayerGroup)
        else this.map.setLayerGroup(layerGroup)
    }

    getMap() {
        return this.map
    }

    addLayer(layer) {
        this.map.addLayer(layer)
        return layer
    }

    removeLayer(layer) {
        this.map.removeLayer(layer)
    }
}

var map;




