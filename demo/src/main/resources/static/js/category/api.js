export async function fetchCategory() {
    const res = await fetch("http://localhost:8080/category", {
        method: "GET",
    })
    return res.json();
}

export async function fetchCategorys(currentValue) {
    let params = new URLSearchParams();
    if (currentValue.largeClassId >= 0)
        params.append("largeClassId", currentValue.largeClassId.toString());
    if (currentValue.middleClassId >= 0)
        params.append("middleClassId", currentValue.middleClassId.toString());
    if (currentValue.smallClassId >= 0)
        params.append("smallClassId", currentValue.smallClassId.toString());
    if (currentValue.detailClassId >= 0)
        params.append("detailClassId", currentValue.detailClassId.toString());
    if (currentValue.bottomClassId >= 0)
        params.append("bottomClassId", currentValue.bottomClassId.toString());


    const res = await fetch("http://localhost:8080/category?" +
        params
    )
    return res.json();
}
