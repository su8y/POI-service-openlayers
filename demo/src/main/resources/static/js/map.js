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

let research_btn = document.querySelector("#current-position-research-btn");
research_btn.addEventListener("click", async (event) => {
    const size = map.getSize();
    const extent = map.getView().calculateExtent(size);
    const bottomLeft = ol.proj.toLonLat(ol.extent.getBottomLeft(extent));
    const topRight = ol.proj.toLonLat(ol.extent.getTopRight(extent));
    const bottomRight = ol.proj.toLonLat(ol.extent.getBottomRight(extent));
    const topLeft = ol.proj.toLonLat(ol.extent.getTopLeft(extent));
    console.log("extent" ,extent);


    // get full current screen shape
    const currentPolygon = [
        [bottomLeft[0], bottomLeft[1]],
        [bottomRight[0], bottomRight[1]],
        [topRight[0], topRight[1]],
        [topLeft[0], topLeft[1]],
        [bottomLeft[0], bottomLeft[1]]
    ];
    console.log(currentPolygon);
    const polygonFeature = new ol.Feature({
        geometry: new ol.geom.Polygon([currentPolygon]),
        // 다른 속성들을 필요에 따라 추가할 수 있습니다.
    });

    const geoJSONFormat = new ol.format.GeoJSON();
    const polygonJsonData = geoJSONFormat.writeFeatureObject(polygonFeature);

    // console.log(JSON.stringify(polygonFeature));

    console.log(polygonJsonData)
    const categoryName = {
        1: "largeClassId",
        2: "middleClassId",
        3: "detailClassId",
        4: "smallClassId",
        5: "bottomClassId"
    }
    let currentCategoryValue = {}

    const idSelectorString = "#cate-";

    for (let i = 1; i <= 5; i++) {
        let selectedCategoryValue = document.querySelector(idSelectorString + i).value;
        currentCategoryValue[categoryName[i]] = selectedCategoryValue;
    }
    const res = await fetchPoiList({currentPositionValue: polygonJsonData, currentCategoryValue: currentCategoryValue})

    console.log(res)
})

