import * as ol from 'ol';
import './testHello'
import './map2'
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
    new ol.layer.Tile({
        source: new ol.source.OSM()
    }),
];
var vectorsource = new ol.source.Vector();
var vectorlayer = new ol.layer.Vector({
    source: vectorsource,
    style: new ol.style.Style({
        stroke: new ol.style.Stroke({
            width: 2,
            color: [0, 0, 255, 1]
        })
    })
});

var map = new ol.Map({
    layers: [
        new ol.layer.Tile({
            source: new ol.source.OSM()
        }),
        vectorlayer
    ],
    target: 'map',
    view: new ol.View({
        center: [14149156.937231855, 4510270.163387867],
        zoom: 18
    })
});
map.on('click', (evt) => {
    var size = map.getSize();
    var extent = map.getView().calculateExtent(size);
    var bottomLeft = ol.proj.toLonLat(ol.extent.getBottomLeft(extent));
    var topRight = ol.proj.toLonLat(ol.extent.getTopRight(extent));
    var bottomRight = ol.proj.toLonLat(ol.extent.getBottomRight(extent));
    var topLeft = ol.proj.toLonLat(ol.extent.getTopLeft(extent));

    // get full current screen shape
    var box = new ol.Feature(new ol.geom.LineString(
        [
            [bottomLeft[0], bottomLeft[1]],
            [bottomRight[0], bottomRight[1]],
            [topRight[0], topRight[1]],
            [topLeft[0], topLeft[1]],
            [bottomLeft[0], bottomLeft[1]]
        ])
    );
    var coordinates = [
        [bottomLeft[0], bottomLeft[1]],
        [bottomRight[0], bottomRight[1]],
        [topRight[0], topRight[1]],
        [topLeft[0], topLeft[1]],
        [bottomLeft[0], bottomLeft[1]]
    ]
    console.log(coordinates)
    var current_projection = ol.proj.get('EPSG:4326');
    var new_projection = ol.proj.get('EPSG:3857');

    box.getGeometry().transform(current_projection, new_projection);
    vectorsource.addFeatures([box]);
})
