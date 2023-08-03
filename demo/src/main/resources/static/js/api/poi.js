const fetchPoiList = async ({
                                currentPositionValue,
                                currentCategoryValue,
                                page = {
                                    size: 10,
                                    page: 0
                                }
                            }) => {
    return fetch('/pois', {
        method: "POST",
        body: {
            currentPosition: JSON.stringify(currentPositionValue.geometry),
            selectedCategory: currentCategoryValue,
            pageable: page
        },
        headers: {
            "Content-Type": "application/json"
        }
    })
}