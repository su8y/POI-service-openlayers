const sideBar = document.querySelector('.home-side-bar')
const poiListSideBar = document.querySelector('.poi-list-side-bar')
const routeListSideBar = document.querySelector('.route-list-side-bar')
const routeToggleBtn = document.querySelector("#route-list-btn");
const poiToggleBtn = document.querySelector("#poi-list-btn");
function openPoiSideBar(){
    if (sideBar.classList.contains('d-none')) sideBar.classList.remove('d-none')
    if (poiListSideBar.classList.contains('d-none')) poiListSideBar.classList.remove('d-none')
    routeListSideBar.classList.add('d-none')
    poiToggleBtn.classList.add("btn-primary")
    routeToggleBtn.classList.add("btn-outline-primary")
    poiToggleBtn.classList.remove("btn-outline-primary")
    routeToggleBtn.classList.remove("btn-primary")
    map_instance.changeLayerGroup()
}
function openRouteSideBar(){
    if (sideBar.classList.contains('d-none')) sideBar.classList.remove('d-none')
    if (routeListSideBar.classList.contains('d-none')) routeListSideBar.classList.remove('d-none')
    poiListSideBar.classList.add('d-none')
    poiToggleBtn.classList.add("btn-outline-primary")
    routeToggleBtn.classList.add("btn-primary")
    poiToggleBtn.classList.remove("btn-primary")
    routeToggleBtn.classList.remove("btn-outline-primary")
}
