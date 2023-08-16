let routeToggleBtn = document.querySelector("#route-list-btn");

let poiToggleBtn = document.querySelector("#poi-list-btn");

let poiSideBar = document.querySelector(".poi-list-side-bar");
let routeSideBar = document.querySelector(".route-list-side-bar");
let poiSideList = document.querySelector(".poi-side-list");
// On : route-side-bar , Off : poi-side-bar
var displayNoneClass = "d-none"
poiToggleBtn.addEventListener('click', () => {
    poiToggleBtn.classList.add("btn-primary")
    routeToggleBtn.classList.add("btn-outline-primary")
    poiToggleBtn.classList.remove("btn-outline-primary")
    routeToggleBtn.classList.remove("btn-primary")
    poiSideBar.classList.remove(displayNoneClass);
    routeSideBar.classList.add(displayNoneClass);
})
routeToggleBtn.addEventListener('click', () => {
    poiToggleBtn.classList.add("btn-outline-primary")
    routeToggleBtn.classList.add("btn-primary")
    poiToggleBtn.classList.remove("btn-primary")
    routeToggleBtn.classList.remove("btn-outline-primary")
    poiSideBar.classList.add(displayNoneClass);
    routeSideBar.classList.remove(displayNoneClass);

})

function poiSideUpdateList(result, ...option) {
    let poiSideList = document.querySelector('.poi-side-list')
    let dataList = result.content
    let totalPage = result.totalPages
    let pageable = result.pageable.offset
    let firstPageFlag = result.first
    let lastPageFlag = result.last
    let pageNumber = result.pageable.pageNumber
    let searchOption = option?.searchOption;
    while (poiSideList.firstChild) {
        poiSideList.removeChild(poiSideList.firstChild)
    }

    for (let data of dataList) {
        let titleString = data.name
        let categoryString = data.telNo

        let itemRoot = document.createElement('div');
        itemRoot.classList.add('item')
        let itemTitle = document.createElement('div');
        itemTitle.classList.add('item-title')
        itemTitle.innerText = data.name
        let itemCategory = document.createElement('div');
        itemCategory.classList.add('item-category')
        itemRoot.dataset.lon = data.lon
        itemRoot.dataset.lat = data.lat
        //아직 완성안됌
        itemCategory.innerText = data.category.largeClassName + " "
            + data.category.middleClassName + " "
            + data.category.smallClassName + " "
            + data.category.detailClassName + " "
            + data.category.bottomClassName;

        itemRoot.appendChild(itemTitle)
        itemRoot.appendChild(itemCategory)
        poiSideList.appendChild(itemRoot)
    }
    //pagination
    let pageField = document.querySelector('#poi-page-field');
    while (pageField.firstChild) {
        pageField.removeChild(pageField.firstChild)
    }

    if (firstPageFlag == false) {
        let previousPageNumber = pageNumber - 1;
        let pageItemNode = document.createElement("div");
        let pageItemString = `<li class="page-item"><a class="page-link" href="page=${previousPageNumber}"><</a></li>`
        pageItemNode.innerHTML = pageItemString
        pageField.appendChild(pageItemNode)
    }
    let rangeStart = pageNumber - 2;
    let rangeEnd = pageNumber + 2;
    if (rangeStart < 0) rangeStart = 0;
    if (rangeEnd > totalPage) rangeEnd = totalPage - 1
    if (rangeEnd - rangeStart < 3) {
        while (rangeEnd++ < totalPage && rangeEnd - rangeStart < 5) ;
    }

    for (let i = rangeStart; i < rangeEnd - 1; i++) {
        let pageItemNode = document.createElement("div");
        let pageItemString = '';
        if (i == pageNumber) {
            pageItemString = `<li class="page-item active"><a class="page-link" href="?page=${i}">${i}</a></li>`
        } else {
            pageItemString = `<li class="page-item"><a class="page-link" href="?page=${i}">${i}</a></li>`
        }
        pageItemNode.innerHTML = pageItemString
        pageField.appendChild(pageItemNode)
    }

    if (lastPageFlag == false) {
        let nextPageNumber = pageNumber + 1
        let pageItemNode = document.createElement("div");
        let pageItemString = `<li class="page-item"><a class="page-link" href="?page=${nextPageNumber}">></a></li>`
        pageItemNode.innerHTML = pageItemString
        pageField.appendChild(pageItemNode)
        // 검색 페이지가 10개 많다면 lastPage +10하는 Button생성
        if (rangeEnd + 10 <= totalPage - pageNumber) {
            let pageItemNode = document.createElement("div");
            let pageItemString = `<li class="page-item"><a class="page-link" href="?page=${lastPageFlag + 10}">>></a></li>`
            pageItemNode.innerHTML = pageItemString
            pageField.appendChild(pageItemNode)

        }
    }

}

document.querySelector("#poi-page-field").addEventListener('click', async (event) => {
    event.preventDefault();
    if(!event.target.classList.contains('page-link')) return;
    let targetLocation = event.target.href
    let split = targetLocation.split('?');
    let page = split[1].split("=");
    let currentPosition = JSON.parse(localStorage.getItem('currentPosition'))
    let currentCategory = JSON.parse(localStorage.getItem('currentCategory'))
    const res = await fetchPoiList({
        currentPositionValue: currentPosition,
        currentCategoryValue: currentCategory,
        page: {
            page: page[1]
        }
    })
    addMarkers(res.content)
    poiSideUpdateList(res, currentCategory)


    // Button 입력 처리
})


poiSideList.addEventListener('click', (event) => {
    let clickedItem = event.target
    if (clickedItem.classList.contains('item')) {
        view.animate({
            center: ol.proj.fromLonLat([clickedItem.dataset.lon, clickedItem.dataset.lat]),
            duration: 100,
        });
    }
})