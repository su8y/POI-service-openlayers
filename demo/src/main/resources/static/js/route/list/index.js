import {getRouteListByLoginUser, deleteRoute } from '../api.js';
import Pagination from '../../common/pagination.js'
import {handleMountRouteListPage} from "./handle.js";

const tableElement = document.querySelector("table");
const routeDeleteBtn = document.querySelector("#route-delete-btn");

(
    async function () {
        handleMountRouteListPage()

        let checkedList = [];
        tableElement.addEventListener('click', event => {
            if (event.target.tagName === "BUTTON") {
                alert("click button!!")
                return;
            }
            let tr = event.target.closest("tr")
            let routeId = tr.dataset.id
            let chkbox = tr.querySelector("input[type=checkbox]");
            if (!chkbox.checked) {
                checkedList = [...checkedList, routeId];
                chkbox.checked = !chkbox.checked
                tr.classList.add('table-primary')
            } else {
                checkedList = checkedList.filter(e => e !== routeId);
                chkbox.checked = !chkbox.checked
                tr.classList.remove('table-primary')
            }
        });


        routeDeleteBtn.addEventListener('click', event => {
            if (checkedList.length === 0) {
                alert("체크박스를 체크후 삭제해주세요.")
                return;
            }
            if (confirm(`${checkedList}를 삭제하시겠습니까 ?`) == true) {
                deleteRoute(checkedList).then(
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
        });
    }

)();

