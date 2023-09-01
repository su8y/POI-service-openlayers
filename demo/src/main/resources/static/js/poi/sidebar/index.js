import {handleClickPoiPagination, openDetailItem, poiSideUpdateList} from "./handle.js";

(function init() {
    const routeToggleBtn = document.querySelector("#route-list-btn");

    const poiToggleBtn = document.querySelector("#poi-list-btn");

    const poiSideBar = document.querySelector(".poi-list-side-bar");
    const routeSideBar = document.querySelector(".route-list-side-bar");
    const poiSideList = document.querySelector(".poi-side-list");

    poiToggleBtn.addEventListener('click', () => {
        poiToggleBtn.classList.add("btn-primary")
        routeToggleBtn.classList.add("btn-outline-primary")
        poiToggleBtn.classList.remove("btn-outline-primary")
        routeToggleBtn.classList.remove("btn-primary")
        poiSideBar.classList.remove("d-none");
        routeSideBar.classList.add("d-none");
        map_instance.changeLayerGroup()
    })


    /**
     * poi SideList Pagination 클릭시 해당 pagination 적용
     */
    handleClickPoiPagination();

    poiSideList.addEventListener('click', (event) => {
        let clickedItem = event.target.closest('.item')
        if (clickedItem !== null && clickedItem.classList.contains('item-detail') === false) {
            map.getView().animate({
                center: ol.proj.fromLonLat([clickedItem.dataset.lon, clickedItem.dataset.lat]),
                duration: 100,
            });
            openDetailItem(clickedItem)

        }
    })






}())
