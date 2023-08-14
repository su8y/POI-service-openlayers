class MapObj {
    #layers;
    #view;
    map;
    #target;
    #previousPoint;
    #markerStyle;
    #markerSource;
    #markerLayer;

    constructor(target) {
        this.#markerSource = new ol.source.Vector();
        this.#markerLayer = new ol.layer.Vector({
            source: this.#markerSource,
            name: "marker_layer",
            zIndex: 15
        })
        this.#target = target
        this.#layers = [
            new ol.layer.Tile({
                source: new ol.source.OSM(),
                zIndex: 10
            })
        ];
        this.#view = new ol.View({
            center: [127.10150880516524, 37.5049184952403],
            zoom: 18,
            projection: 'EPSG:4326'
        })

        this.#markerStyle = new ol.style.Style({
            image: new ol.style.Icon({
                src: 'http://maps.google.com/intl/en_us/mapfiles/ms/micons/orange-dot.png'
            }),
        })
    }

    init() {
        this.map = new ol.Map({
            layers: this.#layers,
            target: this.#target,
            view: this.#view
        });
        this.map.addLayer(this.#markerLayer);

    }

    getInstance() {
        return this.map
    }

    getCoordinate() {
        return this.#previousPoint
    }

    createMarkerByPoint([lon, lat], ...params) {
        this.#markerSource.clear();
        this.#previousPoint = [lon, lat]

        const point = new ol.geom.Point([lon, lat])
        const marker = new ol.Feature({
            geometry: point
        })
        marker.setStyle(this.#markerStyle)
        this.#markerSource.addFeature(marker);
    }
}

let mapObj1 = new MapObj('poi_map');
mapObj1.map
mapObj1.init();
var map = mapObj1.getInstance();

map.on('click', (event) => {
    // mapObj1.createMarkerByPoint(event.coordinate)
    mapObj1.createMarkerByPoint(event.coordinate)
})


const saveForm = document.querySelector("#saveForm")
saveForm.addEventListener('submit', (e) => {
    e.preventDefault();
    let coordinate = mapObj1.getCoordinate();
    const categoryData = getCategoryByDom();
    const categoryCode = document.getElementById("cate-code").textContent;
    console.log("categorycode",categoryCode);
    const frmData = new FormData(e.target);
    let poiRequestData = {
        name: frmData.get("name"),
        telNo: frmData.get("telNo"),
        description: frmData.get("description"),
        category: categoryData
    }

    let categoryBlob = new Blob([JSON.stringify(categoryData)],{
        type: 'application/json',
    });
    frmData.append("categoryCode", categoryCode);
    frmData.append("coordinate",coordinate)
    poiRequestData.coordinate = coordinate;

    fetch('/pois', {
        method: "POST",
        body: frmData
    }).then(r => r.json())
        .then(r => console.log(r))

})
