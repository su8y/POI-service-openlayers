import Pagination from "../../common/pagination.js";

export function handleClickPoiPagination(){
    document.querySelector("#poi-page-field").addEventListener('click', async (event) => {
        event.preventDefault();
        // if (!event.target.classList.contains('page-link')) return;
        let pageLink = event.target.closest('.page-link')
        let targetLocation = pageLink.href
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
    })
}
export function poiSideUpdateList(res, ...option) {
    const {
        totalPage,
        pageable,
        content: dataList,
        first: firstPageFlag,
        last: lastPageFlag
    } = res;
    const poiSideList = document.querySelector('.poi-side-list')
    const {pageNumber} = pageable
    const panoPoiList = document.querySelector('.pano-footer')

    while (panoPoiList.firstChild) panoPoiList.removeChild(panoPoiList.firstChild)
    while (poiSideList.firstChild) poiSideList.removeChild(poiSideList.firstChild)

    for (let data of dataList) {
        let itemRoot = document.createElement('div');
        itemRoot.classList.add('item')
        itemRoot.dataset.id = data.id
        let itemTitle = document.createElement('div');
        itemTitle.classList.add('item-title')
        itemTitle.innerText = data.name
        let itemCategory = document.createElement('div');
        itemCategory.classList.add('item-category')
        itemRoot.dataset.lon = data.lon
        itemRoot.dataset.lat = data.lat
        let categoryElement = makeElementCategory(data);
        itemCategory.innerHTML = categoryElement


        itemRoot.appendChild(itemTitle)
        itemRoot.appendChild(itemCategory)
        poiSideList.appendChild(itemRoot)

        panoPoiList.appendChild(PanoPOITemplate({
            title: data.name,
            category: 'test',
            coord: [data.lon, data.lat],
            imgs: data.img
        }))
    }
    let itemDetail = document.createElement('div');
    itemDetail.classList.add('item', 'item-detail', 'd-none')
    poiSideList.appendChild(itemDetail)

    const paginationRootElement = document.querySelector('#poi-page-field');

    paginationRootElement.innerHTML=''

    const paginationFactory = new Pagination(res);
    let pagenationElementList = paginationFactory.makePagination();

    pagenationElementList.forEach(element => {
        paginationRootElement.appendChild(element)
    })
}
export function handleOldDetailItem(list) {
    let oldDetailItem = list.querySelector('.item-detail')
    if (oldDetailItem != null) {
        let displayNoneitemList = list.querySelectorAll('.item.d-none');
        displayNoneitemList.forEach(e => {
            e.classList.remove('d-none')
        })

        // list.removeChild(oldDetailItem)
    }

}
export function openDetailItem(element) {
    let poiId = element.dataset.id
    fetchDetailPoi(poiId).then(res => res.json()).then(res => {

        makePopupElement({name: res.name, telNo: res.telNo, lon: element.dataset.lon, lat: element.dataset.lat})
        let categoryElement = makeElementCategory(res);

        let imageList = toImageTag(res.images).imageList
        let itemDetailString = `
        <div id = "carouselExampleControls"
            class ="carousel slide w-100"
            data-bs-ride = "carousel" >
            <div class="carousel-inner" >
                ${imageList}
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls"
                    data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next " type="button" data-bs-target="#carouselExampleControls"
                    data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
            </div>
            <div class="list-header">
                <div class="item-title">
                    ${res.name}
                </div>
            </div>
            <div class="item-category">
                ${categoryElement}
            </div>
            <p class="item-content">
                ${res.description ? res.description : ''}
            </p>
            <div class="item-action">
                <a class="btn btn-outline-primary road-view-btn">로드뷰</a>
            </div>
            `
        let requestDetailList = document.querySelector('.poi-side-list')
        handleOldDetailItem(requestDetailList)

        let listDetailItem = document.querySelector('.item-detail')
        listDetailItem.classList.remove('d-none');
        listDetailItem.innerHTML = itemDetailString

        document.querySelector('.poi-side-list').insertBefore(listDetailItem, element)
        element.classList.add('d-none')
        document.querySelector('.road-view-btn').addEventListener('click', () => {
            const panoElement = document.querySelector('pano-element')
            const {lon, lat} = element.dataset
            panoElement.setAttribute('coord', `${lon},${lat}`)
            document.querySelector('#pano-box').classList.remove('d-none')
        })
    })
}
export const makeElementCategory = (data) => {
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

const PanoPOITemplate = info => {
    const {title, category, coord, imgs} = info;
    const li = document.createElement('li');

    const content = `
        <div class="content">
            <div>${title}</div>
            <p>category</p>
        </div>
    `
    li.innerHTML = `${factoryImage(imgs)}${content}`;
    li.setAttribute('coord', coord.join(','))

    return li;
}
const factoryImage = img => img ? `<img src="/download/${img.storeFilename}" alt="${img.originalFilename}"/>` : `<img src="/images/no-image.png" alt="기본 이미지"/>`
