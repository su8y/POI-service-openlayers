import {getRouteListByLoginUser, ROUTE_BASE_LOCATION} from "../api.js";
import Pagination from "../../common/pagination.js";

export const handleMountRouteListPage = async () => {
    const tableBody = document.querySelector('.table > tbody');
    let currentSearchParam = new URLSearchParams(document.location.search);
    let pageNumber = currentSearchParam.get("page")


    try {
        const res = await getRouteListByLoginUser({page: {page: pageNumber}}).then(res => res.json())
        if(res.empty ===true) {
            location.replace(ROUTE_BASE_LOCATION);
            throw new Error("ERROR: Invalid Current Page");
        }
        res.content.forEach(e => {
                var data = makeRouteTableRow(e);
                tableBody.appendChild(data)
            }
        );

        let pageRoot = document.querySelector('.pagination')
        let pageClass = new Pagination(res);
        var elementList = pageClass.makePagination();
        elementList.forEach(element => {
            pageRoot.appendChild(element)
        })
    } catch (e) {
    }
};

function makeRouteTableRow({empty, id, title, description, createAt, startPosition, endPosition, path}) {
    if (empty) {
        throw new Error("ERROR: Invalid Current Page")
    }
    let tr = document.createElement("tr");
    tr.dataset.id = id;
    tr.innerHTML = `
            <td scope="row"><input type="checkbox" value="${id}"></td>
            <td>
                <a href="/route/detail/${id}" onclick="event.stopPropagation()" th:href="@{/route/detail/1}">
                    ${id}
                </a>
            </td>
            <td>${title}</td>
            <td>${description}</td>
            <td>${createAt || '0000-00-00'}</td>
    `
    return tr;

}
