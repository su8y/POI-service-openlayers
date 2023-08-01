async function fetchCategory() {
    const res = await fetch("http://localhost:8080/category", {
        method: "GET",
    })
    return res.json();
}

async function fetchCategorys(currentValue) {
    console.log(currentValue)
    const res = await fetch("http://localhost:8080/category?"
        + new URLSearchParams({
            largeClassId: currentValue.largeClassId
        })
    )
    return res.json();
}