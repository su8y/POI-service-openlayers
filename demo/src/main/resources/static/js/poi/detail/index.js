import {toImageTag} from "../../common/image.js";
import {getDetail} from "../api/base.js";

(async function () {
    let pathName = window.location.pathname;
    let poiId = pathName.substring(pathName.lastIndexOf('detail/') + 7, pathName.length)
    try {
        let res = getDetail(poiId).then(res => res.json()).then(res => {
            handlePoiDetail(res)
        });
    } catch (e) {
    }
}())

const detailClassEnum = {
    id: '.detail-id',
    name: '.detail-name',
    description: '.detail-description',
    telNo: '.detail-telNo',
    coordinate: '.detail-lon',
}
let handlePoiDetail = (poi) => {
    poi['coordinate'] = [poi.lon, poi.lat]
    poi['id'] = '#' + poi.id

    let keys = Object.keys(detailClassEnum)
    for (var key of keys) {
        let element = document.querySelector(detailClassEnum[key])
        element.innerText = poi[key] || 'nothing tel Number'
    }
    //member
    let memberIdElement = document.querySelector('.detail-member-id')
    memberIdElement.innerText = poi['member'].id
    //categor
    const category = poi.category;
    let categoryElement = document.querySelector('.detail-category')
    categoryElement.innerHTML = `
                                    <span class="badge rounded-pill bg-primary">${category.largeClassName} </span>
                                    <span class="badge rounded-pill bg-info">${category.middleClassName} </span>
                                    <span class="badge rounded-pill bg-success">${category.smallClassName} </span>
                                    <span class="badge rounded-pill bg-secondary">${category.detailClassName} </span>
                                    <span class="badge rounded-pill bg-black">${category.bottomClassName} </span>
        `
    //images
    let carousel = toImageTag(poi.images)
    let imageListElement = document.querySelector(".carousel-inner");
    imageListElement.innerHTML = carousel.imageList

    let element1 = document.querySelector(".carousel-indicators");
    element1.innerHTML = carousel.indicatorList


}