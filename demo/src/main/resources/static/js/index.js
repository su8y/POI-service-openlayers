const ol = require('ol')
const layer = require('ol/layer')
const olsource = require('ol/source')
import './testHello'
// import '../css/style.css'

window.hello = function hello(){
    alert('hello');
}
function component() {
    const element = document.createElement('div');

    // Lodash, now imported by this script
    element.innerHTML = _.join(['Hello', 'webpack'], ' ');

    return element;
}

document.body.appendChild(component());

var layers = [
    new layer.Tile({
        source: new olsource.OSM()
    }),
    // new ol.layer.Tile({
    //     extent: [-13884991, 2870341, -7455066, 6338219],
    //     source: new ol.source.TileWMS({
    //         url: 'https://ahocevar.com/geoserver/wms',
    //         params: {'LAYERS': 'topp:states', 'TILED': true},
    //         serverType: 'geoserver',
    //         // Countries have transparency, so do not fade tiles:
    //         transition: 0
    //     })
    // })
];
var map = new ol.Map({
    layers: layers,
    target: 'map',
    view: new ol.View({
        center: [-10997148, 4569099],
        zoom: 4
    })
});

