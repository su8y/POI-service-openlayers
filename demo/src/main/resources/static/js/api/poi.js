async function fetchPoiList({
                                currentPositionValue,
                                currentCategoryValue,
                                page = {
                                    size: 10,
                                    page: 0
                                },
                                success,
                                error
                            }) {
    let urlSearchParams = new URLSearchParams();
    Object.keys(currentCategoryValue).filter(key => currentCategoryValue[key] !== null)
        .forEach(key => {
            if (currentCategoryValue[key] >= 1) urlSearchParams.append(key.toString(), currentCategoryValue[key])
        });

    urlSearchParams.append("polygon", currentPositionValue.geometry.coordinates[0])
    urlSearchParams.append("inputText", "")
    urlSearchParams.append("page", page.page)
    const res = await fetch('/pois?' + urlSearchParams, {
        method: "get",
        headers: {
            "Content-Type": "application/json",
        }
    })
    if (res.status === 200) {
        const data = await res.json();
        return data.body;
    } else {
        throw new Error('Unable')
    }
}

async function fetchDetailPoi(poiId) {
    const res =  fetch(`/pois/detail/${poiId}`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
    return res
}

async function fetchManagePoiList({success, error}) {
    fetch(`/pois/manage${window.location.search}`, {
        method: "GET"
    }).then(res => res.json())
        .then(res => success)
}

async function deletePois(pois) {
    return fetch('/pois/' + pois.join(','), {
        method: "DELETE",

    });
}

async function updatePois(modifiedPois) {
    fetch('/pois', {
        method: "PUT",
        body: modifiedPois

    }).then(res => res.json());

}