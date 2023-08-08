const fetchPoiList = async ({
                                currentPositionValue,
                                currentCategoryValue,
                                page = {
                                    size: 10,
                                    page: 0
                                },
                                success,
                                error
                            }) => {
    let urlSearchParams = new URLSearchParams();
    Object.keys(currentCategoryValue).filter(key => currentCategoryValue[key] !== null)
        .forEach(key => {
            if (currentCategoryValue[key] >= 1) urlSearchParams.append(key.toString(), currentCategoryValue[key])
        });

    urlSearchParams.append("polygon", currentPositionValue.geometry.coordinates[0])
    urlSearchParams.append("inputText", "")
    const res = await fetch('/pois?' + urlSearchParams, {
        method: "get",
        headers: {
            "Content-Type": "application/json"
        }
    })
    if (res.status === 200) {
        const data = await res.json();
        console.log("in function", data)
        return data.body;
    } else {
        throw new Error('Unable')
    }
}