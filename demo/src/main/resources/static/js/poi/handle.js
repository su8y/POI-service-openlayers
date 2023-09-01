import {poiSideUpdateList} from "./sidebar/handle.js";

export function handleClickResearchButton() {
    const reSearchBtn = document.querySelector("#current-position-research-btn ")
    map.on("moveend", () => {
        reSearchBtn.classList.remove("d-none");
    })

    /** 재검색 클릭 이벤트 */
    reSearchBtn.addEventListener("click", async event => {
        map_instance.changeLayerGroup();
        openPoiSideBar()
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
        const poiSearchInputTextElement = document.querySelector('#poi-search-input-text')
        const poiSearchInputText = poiSearchInputTextElement.value
        const res = await fetchPoiList({
            poiSearchInputText: poiSearchInputText,
            currentPositionValue: polygonJsonData,
            currentCategoryValue: currentCategoryValue,

        })

        window.localStorage.setItem("currentPosition", JSON.stringify(polygonJsonData))
        window.localStorage.setItem("currentCategory", JSON.stringify(currentCategoryValue))

        poiSideUpdateList(res, currentCategoryValue)
        addMarkers(res.content)

        reSearchBtn.classList.add("d-none")
    })
}
