import {fetchAddrToCoords, fetchRoute} from '../api.js'
import {RouteItemTemplate} from "../common/RouteItemTemplate.js";

const optionCode = {
    trafast: '실시간 빠른길',
    tracomfort: '실시간 편한길',
    traoptimal: '실시간 최적',
    traavoidtoll: '무료 우선',
    traavoidcaronly: '자동차 전용도로 회피 우선'
}

function updateRouteSideBar(res, routeLayerGroup) {

    const routeListElement = document.querySelector("#route-list");
    let keys = Object.keys(res.route)
    routeLayerGroup.clear()
    for (let i = 0; i < keys.length; i++) {

        let route = res.route[keys[i]][0]
        routeLayerGroup.addPath(route.path, i)
        routeLayerGroup.addStartPoint({coord:route.path[0],name:"start"})
        routeLayerGroup.addGoalPoint({coord:route.path[route.path.length-1],name:"goal"})
        let waypoints = route.summary.waypoints.map(e=> ({location:e.location, duration: e.duration}))
        console.log(waypoints)
        routeLayerGroup.addWayPoint({coords:waypoints})
        // routeLayerGroup.addStartPoint(route.path[route.path.size() - 1],"end")

        let guideString = route.guide.map(e => e.instructions).map(e => `<li>${e}</li>`).join('')

        let item = document.createElement("div")
        item.classList.add('item', 'item-detail')

        let json = {
            taxiFare: route.summary.taxiFare,
            tollFare: route.summary.tollFare,
            distance: route.summary.distance,
            duration: route.summary.duration,
            path: route.path,
            bbox: route.summary.bbox,
            startPosition: route.summary.start.location,
            startName: document.querySelector("#start-point").value,
            goalName: document.querySelector("#end-point").value,
            goalPosition: route.summary.goal.location,
            waypoints: route.summary.waypoints,
            guide: route.guide,
            routeType: optionCode[keys[i]]
        }

        item.innerHTML = RouteItemTemplate({
            routeTypeIndex: optionCode[keys[i]],
            guideString: guideString,
            summary: route.summary,
            json: JSON.stringify(json)

        });

        item.setAttribute('data-bbox-lefttop', route.summary.bbox[0].join(','))
        item.setAttribute('data-bbox-rightbottom', route.summary.bbox[1].join(','))

        routeListElement.appendChild(item);
    }
}


export const handleChangeRouteSearchInput = () => {
    const routeSearchForm = document.querySelector("#route-search-form");
    routeSearchForm.addEventListener('focusout', async event => {
        if (event.target.tagName === "INPUT") {
            const inputString = event.target.value
            if (inputString === '') {
                alert("빈 문자열입니다.")
                return;
            }
            const res = await fetchAddrToCoords(inputString)
            if (res.status === "OK" && res.addresses.length > 0) {
                event.target.dataset.coord = [res.addresses[0].x, res.addresses[0].y]
                event.target.value = res.addresses[0].roadAddress;
            } else {
                alert("다시입력해주세요")
                event.target.value = ''
            }

        }
    })
}

export function handleSummitRouteSearchForm(routeLayerGroup) {
    const routeSearchForm = document.querySelector("#route-search-form");
    const startPoint = document.querySelector("#start-point")
    const goalPoint = document.querySelector("#end-point");
    const wayPointList = document.querySelector("#way-point-list");

    routeSearchForm.addEventListener('submit', async (event) => {
        const start = startPoint.dataset.coord
        const goal = goalPoint.dataset.coord;
        event.preventDefault()
        if (start === '' || start === null || goal === '' || goal === null) {
            throw new Error("필수값들은 입력을 하셔야 합니다.")
        }

        let urlSearchParams = new URLSearchParams();

        urlSearchParams.append("start", start)
        urlSearchParams.append("goal", goal)

        let waypoints = []
        let waypointElements = wayPointList.children
        let wayPointElementsCount = wayPointList.childElementCount
        for (let i = 0; i < wayPointElementsCount; i++) {
            const waypointElement = waypointElements[i].querySelector('input')
            waypoints = [...waypoints, waypointElement.dataset.coord]

        }
        urlSearchParams.append("waypoints", waypoints.join('|'))
        urlSearchParams.append("option", "trafast:traavoidtoll:traavoidcaronly")


        try {
            const res = await fetchRoute(urlSearchParams).then(res => res.json())
            routeLayerGroup.__initRoute__()
            updateRouteSideBar(res, routeLayerGroup)

        } catch (e) {
            console.log(e)
            new Error("Error : Naver API ")
        }
    })
}

export function handleCloseWaypointButton() {
    const addWayPointBtn = document.querySelector('#add-waypoints-btn')
    // 경유지 삭제 EventListener
    addWayPointBtn.addEventListener('click', event => {
        let wayPointsListElement = document.querySelector('#way-point-list')
        let waypointFromElement = document.createElement('div')
        let count = wayPointsListElement.childElementCount
        if (count >= 5) return;

        waypointFromElement.innerHTML = ` 
         <div class="d-flex">
            <input type="text" class="waypoints form-control" name="waypoints" value="">
            <button class="delete-waypoint-btn btn btn-danger">
                <i class="bi bi-x-lg"></i>
            </button>
         </div>
            `
        wayPointsListElement.appendChild(waypointFromElement)
    })
    let wayPointsListElement = document.querySelector('#way-point-list')
    wayPointsListElement.addEventListener('click', (event) => {
        if (event.target.classList.contains('delete-waypoint-btn') === true) {
            const targetElement = event.target.parentElement
            wayPointsListElement.removeChild(targetElement)
        }
    })
}

export const handleClickRouteToggleBtn = (routeLayerGroup) => {
    const routeToggleBtn = document.querySelector("#route-list-btn");
    const poiToggleBtn = document.querySelector("#poi-list-btn");
    const poiSideBar = document.querySelector(".poi-list-side-bar");
    const routeSideBar = document.querySelector(".route-list-side-bar");
    const poiSideList = document.querySelector(".poi-side-list");
    routeToggleBtn.addEventListener('click', () => {
        poiToggleBtn.classList.add("btn-outline-primary")
        routeToggleBtn.classList.add("btn-primary")
        poiToggleBtn.classList.remove("btn-primary")
        routeToggleBtn.classList.remove("btn-outline-primary")
        poiSideBar.classList.add('d-none');
        routeSideBar.classList.remove('d-none');
        routeLayerGroup.__initRoute__()
    })
}

export const handleOnClickOpenRouteSaveForm = () => {
    const routeListElement = document.querySelector('#route-list')
    const saveModalElement = document.querySelector('save-modal-element')
    routeListElement.addEventListener('click', event => {
        if (event.target.classList.contains('open-route-save-form')) {
            event.preventDefault()
            console.log('clicked Button')
            const form = event.target.closest('form')
            const data = new FormData(form);
            saveModalElement.initFromData(JSON.parse(data.get('json')))
        }
    })

}