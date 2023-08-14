// class MyMap {
//     map;
//     layers;
//     target;
//     view;
//     constructor(target) {
//         this.target = target;
//         this.layers = [
//             new ol.layer.Tile({
//                 source: new ol.source.OSM()
//             }),
//             vectorlayer
//         ]
//         this.view = new ol.View({
//             center: [14149156.937231855, 4510270.163387867],
//             zoom: 18
//         })
//     }
//
//     init() {
//         this.map = new ol.Map({
//             layers: this.layers,
//             target: this.target,
//             view: this.view
//         });
//     }
// }
var mylevel = 1;
let myhost = 1;
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
        var feature = map.forEachFeatureAtPixel(evt.pixel,
            function (feature) {
                return feature;
            });

        if (feature) {
            alert(feature.get('name'));
        }

    }
)
map.on("moveend", () => {
    const reSearchBtn = document.querySelector("#current-position-research-btn ")
    reSearchBtn.classList.remove("d-none");
})

let research_btn = document.querySelector("#current-position-research-btn");
research_btn.addEventListener("click", async (event) => {
    const size = map.getSize();
    const extent = map.getView().calculateExtent(size);
    const bottomLeft = ol.proj.toLonLat(ol.extent.getBottomLeft(extent));
    const topRight = ol.proj.toLonLat(ol.extent.getTopRight(extent));
    const bottomRight = ol.proj.toLonLat(ol.extent.getBottomRight(extent));
    const topLeft = ol.proj.toLonLat(ol.extent.getTopLeft(extent));


    // get full current screen shape
    const currentPolygon = [
        [bottomLeft[0], bottomLeft[1]],
        [bottomRight[0], bottomRight[1]],
        [topRight[0], topRight[1]],
        [topLeft[0], topLeft[1]],
        [bottomLeft[0], bottomLeft[1]]
    ];
    const polygonFeature = new ol.Feature({
        geometry: new ol.geom.Polygon([currentPolygon]),
        // 다른 속성들을 필요에 따라 추가할 수 있습니다.
    });

    const geoJSONFormat = new ol.format.GeoJSON();
    const polygonJsonData = geoJSONFormat.writeFeatureObject(polygonFeature);

    const categoryName = {
        1: "largeClassId",
        2: "middleClassId",
        3: "smallClassId",
        4: "detailClassId",
        5: "bottomClassId"
    }
    let currentCategoryValue = {}

    const idSelectorString = "#cate-";

    for (let i = 1; i <= 5; i++) {
        let selectedCategoryValue = document.querySelector(idSelectorString + i).value;
        currentCategoryValue[categoryName[i]] = selectedCategoryValue;
        console.log("cc1",currentCategoryValue)
    }
    const res = await fetchPoiList({
        currentPositionValue: polygonJsonData,
        currentCategoryValue: currentCategoryValue,
    })

    console.log(res)
    addMarkers(res.content)

    research_btn.classList.add("d-none")
})
const marker_style = new ol.style.Style({
    image: new ol.style.Icon({
        src: 'http://maps.google.com/intl/en_us/mapfiles/ms/micons/orange-dot.png'
    }),
});
const markerSource = new ol.source.Vector();
const markerLayer = new ol.layer.Vector({
    source:markerSource
})
map.addLayer(markerLayer);
function addMarkers(elements=[]){
    markerSource.clear()
    elements.forEach(e => {
        console.log(e.lon, e.lat)
        const marker = new ol.Feature({
            geometry: new ol.geom.Point(new ol.proj.fromLonLat([e.lon, e.lat])),
        });
        marker.setStyle(marker_style)
        markerSource.addFeature(marker)
    })


}
