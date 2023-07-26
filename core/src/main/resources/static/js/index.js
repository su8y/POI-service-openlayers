import {Map, View} from "/ol";
import {OSM} from "/ol/source.js";
import TileLayer from "/ol/layer/Tile.js";

const map = new Map({
    target: 'map',
    layers: [
        new TileLayer({
            source: new OSM(),
        }),
    ],
    view: new View({
        center: [0, 0],
        zoom: 2,
    }),
});
