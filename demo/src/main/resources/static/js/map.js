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

var view = new ol.View({
    center: [14149156.937231855, 4510270.163387867],
    zoom: 18

})
var map = new ol.Map({
    layers: [
        new ol.layer.Tile({
            source: new ol.source.OSM()
        }),
        vectorlayer
    ],
    target: 'map',
    view: view
});

let popupContainer = document.createElement('div');
popupContainer.setAttribute("id", "popup")
popupContainer.classList.add('item');

document.body.appendChild(popupContainer)

let poi_overlay = new ol.Overlay({
    element: document.getElementById("popup"),
});
map.addOverlay(poi_overlay)
map.on('click', (evt) => {
        var feature = map.forEachFeatureAtPixel(evt.pixel,
            function (feature) {
                return feature;
            });

        /**
         * Overlay pop-ups on the map
         * when click features on the map
         */
        if (feature) {
            while (popupContainer.firstChild) popupContainer.removeChild(popupContainer.firstChild) // delete All children
            let poiInstance = feature.get("poi")
            let content = document.createElement('div')

            let items = document.querySelectorAll('.item')
            let index = Array.from(items).filter(e => {
                return e.dataset.id == poiInstance.id
            })

            popupContainer.appendChild(content)
            content.innerHTML
                = `<div class="popup-field" data-set>
                    <div>
                        <strong>${poiInstance.name} </strong>${poiInstance.telNo}
                    </div>
                    <div>
                        <button id="selectStartPosition" class="btn btn-primary" onclick="console.log('출발지')">출발지</button>
                        <button id="selectFinishPosition" class="btn btn-danger" onclick="console.log('도착지')">도착지</button>
                    </div>
                </div>`

            var coordinate = ol.proj.fromLonLat([feature.get("poi").lon, feature.get("poi").lat])
            poi_overlay.setPosition(coordinate)

            let listDetailItem = document.createElement("div");
            listDetailItem.classList.add('item', 'item-detail')
            fetchDetailPoi(poiInstance.id)
                .then(res => res.json())
                .then(res => {
                    // detail보여주기 item안보여주고
                    let imageList = toImageTag(res.images).imageList

                    listDetailItem.innerHTML
                        = ` 
                             <div id="carouselExampleControls" class="carousel slide w-100" data-bs-ride="carousel">
                                <div class="carousel-inner">
                                    ${imageList}
                                </div>
                                <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next " type="button" data-bs-target="#carouselExampleControls" data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
                            </div>
                            <div class="list-header">
                            <div class="item-title">
                            ${res.name}
                            </div>
                            <div class="item-category">산업/서비스산업/기타서비스</div>
                        </div>
                        <p class="item-content">
                            ${res.description}
                        </p>
                        <div class="item-action">
                            <div class="btn btn-outline-info">경로 찾기</div>
                            <div class="btn btn-outline-primary">로드뷰</div>
                        </div>
                    `
                    document.querySelector('.poi-side-list').insertBefore(listDetailItem, index[0])
                    index[0].classList.add('d-none')
                })
            let sideBar = document.querySelector('.home-side-bar')
            if (sideBar.classList.contains('d-none')) sideBar.classList.remove('d-none')
            let poiListSideBar = document.querySelector('.poi-list-side-bar')
            let routeListSideBar = document.querySelector('.route-list-side-bar')
            if (poiListSideBar.classList.contains('d-none')) poiListSideBar.classList.remove('d-none')
            routeListSideBar.classList.add('d-none')

            index[0].scrollIntoView({behavior: "smooth", block: "start"})
            index[0].classList.add('active')


        }
    }
)
map.on("moveend", () => {
    const reSearchBtn = document.querySelector("#current-position-research-btn ")
    reSearchBtn.classList.remove("d-none");
})

let research_btn = document.querySelector("#current-position-research-btn");

/** 재검색 클릭 이벤트 */
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
    }
    const res = await fetchPoiList({
        currentPositionValue: polygonJsonData,
        currentCategoryValue: currentCategoryValue,
    })

    window.localStorage.setItem("currentPosition", JSON.stringify(polygonJsonData))
    window.localStorage.setItem("currentCategory", JSON.stringify(currentCategoryValue))

    poiSideUpdateList(res, currentCategoryValue)
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
    source: markerSource
})
map.addLayer(markerLayer);

function addMarkers(elements = []) {
    markerSource.clear()
    elements.forEach(e => {
        console.log("in add Marker ", e);
        const marker = new ol.Feature({
            geometry: new ol.geom.Point(new ol.proj.fromLonLat([e.lon, e.lat])),
            poi: e
        });
        marker.setStyle(marker_style)
        markerSource.addFeature(marker)
    })
}
