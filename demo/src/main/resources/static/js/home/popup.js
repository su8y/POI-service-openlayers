function handleRoutePosition({point,name,pointString}) {
    let element = document.querySelector(`${pointString}`);


    element.dataset.coord = point
    element.value=name;
}

const popupContainer = document.createElement('div');
popupContainer.setAttribute("id", "popup")
popupContainer.classList.add('item');

document.body.appendChild(popupContainer)
let poi_overlay = new ol.Overlay({
    element: document.getElementById("popup"),
});

function initPopup() {
    map.addOverlay(poi_overlay)
}

function addFeaturePopupContainer(str, lon, lat) {
    while (popupContainer.firstChild) popupContainer.removeChild(popupContainer.firstChild) // delete All children
    let content = document.createElement('div')

    popupContainer.appendChild(content)
    content.innerHTML = str

    var coordinate = ol.proj.fromLonLat([lon, lat])
    poi_overlay.setPosition(coordinate)
}

function makePopupElement(obj
) {
    const popupString = `<div class="popup-field" data-set>
                    <div>
                        <strong>${obj.name} </strong>${obj.telNo ? obj.telNo : ''}
                    </div>
                    <div>
                        <button id="selectStartPosition" class="btn btn-primary" onclick="(()=>{
                            // obj.lon , lat을 naverapi 를 통해서 주소로 변환하여 넣는다.
                            handleRoutePosition({point:'${obj.lon},${obj.lat}',name:'${obj.name}',pointString:'#start-point'})
                            openRouteSideBar()
                        })()">출발지</button>
                        <button id="selectFinishPosition" class="btn btn-danger" onclick="(()=>{
                            handleRoutePosition({point:'${obj.lon},${obj.lat}',name:'${obj.name}',pointString:'#end-point'})
                            openRouteSideBar()
                        })()">도착지</button>
                    </div>
                </div>`
    addFeaturePopupContainer(popupString, obj.lon, obj.lat)
}
