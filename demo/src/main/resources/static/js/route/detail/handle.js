import {getDetail} from "../api.js";
import {toImageTag} from "../../common/image.js";

export const handleMountPage = async () => {
    let pathName = window.location.pathname;
    let routeId = pathName.substring(pathName.lastIndexOf("/detail/") + "/detail/".length, pathName.length)
    try {
        const res = await getDetail(routeId)
        await successRouteDetail(res)
    } catch (e) {
        alert(`${routeId} 디테일 정보를 가져오는데 실패하였습니다`)
        throw Error(`Failed fetch route Detail ${routeId}`, e)
        // location.replace(ROUTE_BASE_LOCATION)
    }
}

const successRouteDetail = (route) => {
    let summary =JSON.parse(route.json)
    console.log(summary)
    let guides = summary.guide.map(e=>{
        return `<li>${e.instructions}</li>`
    })
    let guideString = guides.join(',')


    elementInnerContent('.detail-id', '#' + route.id, false)
    elementInnerContent('.detail-name', route.title, false)
    elementInnerContent('.detail-description', route.description, true)
    elementInnerContent('.detail-guide', guideString, true)
    // summary Info
    // let summaryJson = JSON.parse(route['summary'])                          // extract summary from response json
    elementInnerContent('.detail-route-type', summary.routeType, false)
    elementInnerContent('.detail-distance', Math.round(summary.distance/1000).toFixed(2), false)
    elementInnerContent('.detail-fee', summary.taxiFare+ "원,"+summary.tollFare+"원", false)
    elementInnerContent('.detail-time', Math.round(summary.duration/1000/60).toFixed(1)+"분", false)
    // elementInnerContent('detail-fee', summaryJson.fee, false)

    //image
    try {
        let carousel = toImageTag(route.images)
        elementInnerContent('.carousel-inner', carousel.imageList,true)
        elementInnerContent('.carousel-indicators', carousel.indicatorList,true)
    } catch (e) {
        throw new Error("Error image Loading");
    }
}
const elementInnerContent = (elementQuerySelectorString, content, htmlFlag) => {
    let element = document.querySelector(`${elementQuerySelectorString}`);
    if (htmlFlag) {
        element.innerHTML = content
        return;
    }
    element.innerText = content
}
