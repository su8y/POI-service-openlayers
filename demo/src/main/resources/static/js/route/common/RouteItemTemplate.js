export const RouteItemTemplate = ({routeTypeIndex,guideString,summary,json})=>{
    const startpoint = document.querySelector("#start-point").value
    const goalpoint = document.querySelector("#end-point").value
    return  `
        <form>
            <details class="w-100" data-bbox0="${summary.bbox[0]}" data-bbox1="${summary.bbox[1]}">
                <summary>
                    <div class="text-black-50 fs-6 js-optionCode">${routeTypeIndex}</div>
                    <input name="optionCode" value="${routeTypeIndex}" hidden />
                    <h3>
                        ${Math.floor(summary.duration / 1000 / 60)}분  ${Math.floor(summary.distance / 1000)}KM
                    </h3>
                </summary>
                <div class="list-header fw-bold">${startpoint} 에서<br/>
                            ${goalpoint} 가는 경로
                </div>
                <div class="item-content">
                    <ul class="timeline">${guideString}</ul>
                </div>
                <div class="item-bottom d-flex justify-content-between">
                    <div>
                        <div class="text-black-50">
                            택시요금 : ${summary.taxiFare}원
                        </div>
                        <div class="text-black-50">
                            톨비 : ${summary.tollFare}원
                        </div>
                    </div>
                    <div>
                        <button type="button" class="btn btn-primary open-route-save-form" data-bs-toggle="modal" data-bs-target="#exampleModalCenter"> 저장하기</button>
                    </div>
                </div>
                <textarea hidden name="json" >
                ${json}
                </textarea>
            </details>
        </form>
    `;
}