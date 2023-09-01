export const ROUTE_BASE_LOCATION = "/route/list";
export async function getRouteListByLoginUser({
                                                  page = {size: 10, page: 0}
                                              }
) {
    const searchParams = new URLSearchParams();
    searchParams.append("page", page.page);
    const res = fetch('/routes?' + searchParams.toString(), {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        }
    })
    return res;
}

export function deleteRoute(routeIds) {
    return fetch('/routes/' + routeIds.join(','), {
        method: "DELETE",

    });
}

export function getDetail(routeId) {
    return fetch(`/routes/${routeId}`).then(res => res.json())
}
export function getRoute(searchParam) {
    const res = fetch('/api/route/find?' + searchParam, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",

        }
    })
    return res
}

export function fetchRoute(searchParam) {
    const res = fetch('/api/route/find?' + searchParam, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",

        }
    })
    return res
}
export function fetchCoordToAddr(query) {
    return fetch("to-addr?coordinates=" + query).then(res => res.json());
}

export function fetchAddrToCoords(addr) {
    return fetch("to-coords?address=" + addr).then(res => res.json())
}
