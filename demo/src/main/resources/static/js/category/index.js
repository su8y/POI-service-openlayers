import {fetchCategory, fetchCategorys} from "./api.js";

(function(){
    let cate1Select = document.querySelector("#cate-1");
    let cate2Select = document.querySelector("#cate-2");
    let cate3Select = document.querySelector("#cate-3");
    let cate4Select = document.querySelector("#cate-4");
    let cate5Select = document.querySelector("#cate-5");
    const defaultOptionNode = ' <option selected value>None</option> ';

    const categoryId = [
        'undefinded',
        'largeClassId',
        'middleClassId',
        'smallClassId',
        'detailClassId',
        'bottomClassId'
    ];
    fetchCategory().then(res => {
        res.forEach(cate => {
            let option = document.createElement("option");
            option.setAttribute("value", cate.largeClassId);
            option.appendChild(document.createTextNode(cate.largeClassName));
            cate1Select.appendChild(option)
        })
    })

    cate1Select.addEventListener("change", (event) => {
        let cate1SelectValue = cate1Select.value;
        fetchCategorys({
            largeClassId: cate1SelectValue,
        }).then(
            res => {
                cate2Select.innerHTML = defaultOptionNode;
                cate3Select.innerHTML = defaultOptionNode;
                cate4Select.innerHTML = defaultOptionNode;
                cate5Select.innerHTML = defaultOptionNode;

                res.forEach(cate => {
                    if (cate.middleClassId == 0) {

                        let option = document.createElement("option");
                        option.setAttribute("value", cate.middleClassId);
                        option.appendChild(document.createTextNode("미분류"));
                        cate2Select.appendChild(option)
                    }
                    if (cate.middleClassId > 0) {

                        let option = document.createElement("option");
                        option.setAttribute("value", cate.middleClassId);
                        option.appendChild(document.createTextNode(cate.middleClassName));
                        cate2Select.appendChild(option)
                    }
                })
            }
        )
    })
    cate2Select.addEventListener("change", (event) => {
            let cate1SelectValue = cate1Select.value;
            let cate2SelectValue = cate2Select.value;
            fetchCategorys({
                largeClassId: cate1SelectValue,
                middleClassId: cate2SelectValue,
            }).then(
                res => {
                    cate3Select.innerHTML = defaultOptionNode;
                    cate4Select.innerHTML = defaultOptionNode;
                    cate5Select.innerHTML = defaultOptionNode;
                    res.forEach(cate => {
                        if (cate.smallClassId == 0) {
                            let option = document.createElement("option");
                            option.setAttribute("value", cate.smallClassId);
                            option.appendChild(document.createTextNode("미분류"));
                            cate3Select.appendChild(option)
                        }
                        if (cate.smallClassId > 0) {
                            let option = document.createElement("option");
                            option.setAttribute("value", cate.smallClassId);
                            option.appendChild(document.createTextNode(cate.smallClassName));
                            cate3Select.appendChild(option)
                        }
                    })
                })
        }
    )
    cate3Select.addEventListener("change", (event) => {
        let cate1SelectValue = cate1Select.value;
        let cate2SelectValue = cate2Select.value;
        let cate3SelectValue = cate3Select.value;
        fetchCategorys({
            largeClassId: cate1SelectValue,
            middleClassId: cate2SelectValue,
            smallClassId: cate3SelectValue,
        }).then(
            res => {
                cate4Select.innerHTML = defaultOptionNode;
                cate5Select.innerHTML = defaultOptionNode;
                res.forEach(cate => {
                    if (cate.detailClassId == 0) {

                        let option = document.createElement("option");
                        option.setAttribute("value", cate.detailClassId);
                        option.appendChild(document.createTextNode("미분류"));
                        cate4Select.appendChild(option)
                    }
                    if (cate.detailClassId > 0) {

                        let option = document.createElement("option");
                        option.setAttribute("value", cate.detailClassId);
                        option.appendChild(document.createTextNode(cate.detailClassName));
                        cate4Select.appendChild(option)
                    }
                })
            }
        )
    })
    cate4Select.addEventListener("change", (event) => {
        let cate1SelectValue = cate1Select.value;
        let cate2SelectValue = cate2Select.value;
        let cate3SelectValue = cate3Select.value;
        let cate4SelectValue = cate4Select.value;
        fetchCategorys({
            largeClassId: cate1SelectValue,
            middleClassId: cate2SelectValue,
            smallClassId: cate3SelectValue,
            detailClassId: cate4SelectValue
        }).then(
            res => {
                cate5Select.innerHTML = defaultOptionNode;
                res.forEach(cate => {
                    if (cate.bottomClassId == 0) {

                        let option = document.createElement("option");
                        option.setAttribute("value", cate.bottomClassId);
                        option.appendChild(document.createTextNode("미분류"));
                        cate5Select.appendChild(option)
                    }
                    if (cate.bottomClassId > 0) {

                        let option = document.createElement("option");
                        option.setAttribute("value", cate.bottomClassId);
                        option.appendChild(document.createTextNode(cate.bottomClassName));
                        cate5Select.appendChild(option)
                    }
                })
            }
        )
    })
    cate5Select.addEventListener("change", (event) => {
        let cate1SelectValue = cate1Select.value;
        let cate2SelectValue = cate2Select.value;
        let cate3SelectValue = cate3Select.value;
        let cate4SelectValue = cate4Select.value;
        let cate5SelectValue = cate5Select.value;
        fetchCategorys({
            largeClassId: cate1SelectValue,
            middleClassId: cate2SelectValue,
            smallClassId: cate3SelectValue,
            detailClassId: cate4SelectValue,
            bottomClassId: cate5SelectValue
        }).then(
            res => {
                document.getElementById('cate-code').innerText = res[0].categoryCode
            }
        )
    })
}())





