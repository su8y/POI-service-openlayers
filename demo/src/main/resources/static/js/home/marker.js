const marker_style = new ol.style.Style({
    image: new ol.style.Icon({
        src: 'http://maps.google.com/intl/en_us/mapfiles/ms/micons/orange-dot.png'
    }),
});
const markerSource = new ol.source.Vector();
const markerLayer = new ol.layer.Vector({
    source: markerSource
})

function addMarkers(elements = []) {
    markerSource.clear()
    elements.forEach(e => {
        const marker = new ol.Feature({
            geometry: new ol.geom.Point(new ol.proj.fromLonLat([e.lon, e.lat])),
            poi: e
        });
        marker.setStyle(marker_style)
        markerSource.addFeature(marker)
    })
}

function initMarker() {
    map_instance.addLayer(markerLayer)
    openDetailClickMarker()

}

function openDetailClickMarker() {
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
                let poiInstance = feature.get("poi")

                makePopupElement({
                    name: poiInstance.name,
                    telNo: poiInstance.telNo,
                    lon: poiInstance.lon,
                    lat: poiInstance.lat
                })


                let list = document.querySelector('.poi-side-list')
                let index = selectListItem(list, poiInstance.id)


                // oepn detail Item.
                fetchDetailPoi(poiInstance.id)
                    .then(res => res.json())
                    .then(res => {
                        let categoryElement  = makeElementCategory(res)
                        // detail보여주기 item안보여주고
                        let imageList = toImageTag(res.images).imageList
                        let listDetailItem = document.querySelector('.item-detail')
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
                        </div>
                            <div class="item-category">${categoryElement}</div>
                        <p class="item-content">
                            ${res.description ? res.description : ''}
                        </p>
                        <div class="item-action">
                                <div class="btn btn-outline-primary road-view-btn">로드뷰</div>
                        </div>
                    `


                        /** 오래된 디테일 아이템 삭제 및 d-none되어있는 poiItem d-none클래스네임 삭제 */
                        let requestDetailList = document.querySelector('.poi-side-list')
                        handleOldDetailItem(requestDetailList)

                        document.querySelector('.poi-side-list').insertBefore(listDetailItem, index)
                        index.classList.add('d-none')

                        openPoiSideBar()
                        document.querySelector('.road-view-btn').addEventListener('click',()=>{
                            const panoElement = document.querySelector('pano-element')
                            const {lon,lat} = res
                            panoElement.setAttribute('coord',`${lon},${lat}`)
                            document.querySelector('#pano-box').classList.remove('d-none')
                        })
                    })


                index.scrollIntoView({behavior: "smooth", block: "start"})
                index.classList.add('active')


            }
        }
    )


}
function selectListItem(list, id) {
    let items = document.querySelectorAll('.item')
    let index = Array.from(items).filter(e => {
        return e.dataset.id == id
    })
    return index[0]
}
const makeElementCategory = (data) => {
    let categoryElement = '';
    categoryElement += data.category.largeClassName ? `
                        <span class="badge rounded-pill bg-secondary">${data.category.largeClassName}</span>` : ''
    categoryElement += data.category.middleClassName ? `
                        <span class="badge rounded-pill bg-secondary">${data.category.middleClassName}</span>` : ''
    categoryElement += data.category.smallClassName ? `
                        <span class="badge rounded-pill bg-secondary">${data.category.smallClassName}</span>` : ''
    categoryElement += data.category.detailClassName ? `
                        <span class="badge rounded-pill bg-secondary">${data.category.detailClassName}</span>` : ''
    categoryElement += data.category.bottomClassName ? `
                        <span class="badge rounded-pill bg-secondary">${data.category.bottomClassName}</span>` : ''
    return categoryElement;
}
function handleOldDetailItem(list) {
    let oldDetailItem = list.querySelector('.item-detail')
    if (oldDetailItem != null) {
        let displayNoneitemList = list.querySelectorAll('.item.d-none');
        displayNoneitemList.forEach(e => {
            e.classList.remove('d-none')
        })

        // list.removeChild(oldDetailItem)
    }

}
