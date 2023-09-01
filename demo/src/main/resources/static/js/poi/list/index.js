import {deletePois, fetchManagePoiList} from "../api/base.js";
import Pagination from "../../common/pagination.js";

(function init() {
    var checkedList = [] // using tableElement
    initManagePoiList()

    const tableElement = document.querySelector("table");
    tableElement.addEventListener('click', event => {
        if (event.target.tagName === "BUTTON") {
            let parent_li = event.target.closest("tr");
            let nodeValue = parent_li.querySelector(":nth-child(1) > input[type=checkbox]").value
            fetch(`/pois/manage/${nodeValue}`).then(
                res => res.json()
            ).then(res => {
                let element = document.querySelector(".modal-body");
                let input_poi_id = element.querySelector("#modal-poi-id")
                let input_poi_name = element.querySelector("#modal-poi-name")
                let input_poi_telNo = element.querySelector("#modal-poi-telNo")
                let input_poi_description = element.querySelector("#modal-poi-description")
                input_poi_id.value = res.id
                input_poi_name.value = res.name
                input_poi_telNo.value = res.telNo
                input_poi_description.value = res.description

            });
            return;
        }
        if (event.target.tagName === "A") {
            return;
        }
        let tr = event.target.closest("tr")
        let chkbox = tr.querySelector("input[type=checkbox]");
        const idList = chkbox.value.split(",")

        if (!chkbox.checked) {
            checkedList = [...checkedList, idList];
            chkbox.checked = !chkbox.checked
            tr.classList.add('table-primary')
        } else {
            checkedList = checkedList.filter(e => e !== idList);
            chkbox.checked = !chkbox.checked
            tr.classList.remove('table-primary')
        }


    })
    const poiDeletButtonElement = document.querySelector("#poi-delete-btn");
    poiDeletButtonElement.addEventListener('click', event => {
        if (checkedList.length === 0) {
            alert("체크박스를 체크후 삭제해주세요.")
            return;
        }
        if (confirm(`${checkedList}를 삭제하시겠습니까 ?`) == true) {
            deletePois(checkedList).then(
                res => {
                    if (res.status == 200) {
                        let table = document.querySelector("tbody");
                        let trList = table.querySelectorAll("tr")
                        trList.forEach(tr => {
                            let chk_td = tr.querySelector(":nth-child(1) > input[type=checkbox]")

                            if (checkedList.includes(tr.querySelector(":nth-child(1) > input").value)) {
                                table.removeChild(tr);
                            }
                        })

                    }
                }
            );
        }
    })

}());

async function initManagePoiList() {
    try {
        const paginationParent = document.querySelector(".pagination");
        const htmlTableSectionElement = document.querySelector("tbody");
        const jsAllCheckedElement = document.querySelector('.js-all-checked')

        const response = await fetchManagePoiList({searchSpec: window.location.search});
        const res = await response.json()
        const idList = []
        res._embedded?.pOIResponseDtoList.forEach(poi => {
            idList.push(poi.id)
            const tr = crateTableRow(poi)
            htmlTableSectionElement.appendChild(tr);
        })
        jsAllCheckedElement.value = idList.join(',')
        const pagination = new Pagination(res.page)
        let makeHetosaPagination = pagination.makeHetosaPagination();

        paginationParent.append(...makeHetosaPagination)
    } catch (e) {
        console.log(e)
    }

}

function crateTableRow(poi) {
    let tr = document.createElement("tr");
    let td = document.createElement("td");
    let check_box = document.createElement("input");
    check_box.value = poi.id
    check_box.type = "checkbox"
    td.appendChild(check_box)
    //checkbox
    tr.appendChild(td)
    td = document.createElement("td");
    let anker = document.createElement("a");
    anker.href = `/poi/detail/${poi.id}`;
    anker.appendChild(document.createTextNode(poi.name));
    td.appendChild(anker)
    tr.appendChild(td)

    td = document.createElement("td");
    td.appendChild(document.createTextNode(`${poi.category.largeClassName || "미분류"}`))
    tr.appendChild(td)
    td = document.createElement("td");
    td.appendChild(document.createTextNode(`${poi.category.middleClassName || "미분류"}`))
    tr.appendChild(td)
    td = document.createElement("td");
    td.appendChild(document.createTextNode(`${poi.category.smallClassName || "미분류"}`))
    tr.appendChild(td)
    td = document.createElement("td");
    td.appendChild(document.createTextNode(`${poi.category.detailClassName || "미분류"}`))
    tr.appendChild(td)
    td = document.createElement("td");
    td.appendChild(document.createTextNode(`${poi.category.bottomClassName || "미분류"}`))
    tr.appendChild(td)
    td = document.createElement("td");
    td.appendChild(document.createTextNode(poi.telNo))
    tr.appendChild(td)
    //edit button
    td = document.createElement("td");
    let my_button = document.createElement('button');
    my_button.classList.add('btn', 'btn-primary')
    my_button.setAttribute("data-bs-toggle", "modal")
    my_button.setAttribute("data-bs-target", "#exampleModal")
    my_button.appendChild(document.createTextNode("수정하기"))
    td.appendChild(my_button)
    tr.appendChild(td)
    return tr;

}


/**
 * 체크한 POI 삭제
 *  */
