class SavePoiComponent extends HTMLElement {
    constructor() {
        super();
        this.render();
    }

    connectedCallback() {
    }

    adoptCallback() {
    }

    attributeChangedCallback(attrName, oldVal, newVal) {
    }

    static get observedAttributes() {
    }


    // custom element 가 제거될때 호출된다.
    disconnectedCallback() {
    }
    initFromData(data){
        this.render(data)
    }
    onSubmit() {
        const form = this.querySelector('form')
        form.addEventListener('submit', event => {
            event.preventDefault()
            let data = new FormData(event.target);
            let pathString = data.get('path')
            // pathString = pathString.slice(1, pathString.length - 1);
            let waypoints = data.getAll('waypoints');
            console.log("waypoints1",waypoints)
            const pointsList= waypoints.map(e=>{
                let coord = e.split(',')
                return {x:coord[0],y:coord[1]}
            })
            let jsondata = data.get('json')
            console.log("jsondata",jsondata)
            let request = new FormData();
            let json = {
                title: data.get('title'),
                description: data.get('description'),
                path: JSON.parse(pathString),
                waypoints: pointsList,
                json:jsondata
            }
            console.log(json)
            request.append("request",new Blob([JSON.stringify(json)],{type:"application/json"}))
            request.append("files",data.get('files'))
            fetch("/routes",{
                method:"POST",
                // headers:{
                //     "Content-Type":"multipart/form-data"
                // },form-data
                body:request
            })
        })

    }


    // custom method
    render(data) {
        console.log(data)
        const id = this.getAttribute("key")

        this.innerHTML = `
           <div class="modal fade" id="${id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
              <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLongTitle">경로 저장 Modal Form</h5>
                    <button type="button" class="btn-close close " data-bs-dismiss="modal" aria-label="Close"> </button>
                  </div>
                  <form >
                      <div class="modal-body">
                          <label for="saveRoutetitle" class="fw-bold">제목</label>
                          <input id="saveRoutetitle" name="title" class="form-control-lg" type="text" value="${data?.startName}에서 ${data?.goalName}가는 경로"/>
                          
                          
                          <label for="saveRouteDescription" class="fw-bold">설명</label>
                          
                          <input name="startPosition" hidden value="${data?.startPosition || ''}"/>
                          <input name="goalPosition" hidden value="${data?.goalPosition || ''}"/>
                          ${data?.waypoints.map(e => `<input name="waypoints" value="${e.location}" hidden />`)}
                          
                          <input name="path" hidden value="${JSON.stringify(data?.path) || ''}"/>
                          <input name="bbox" hidden value="${JSON.stringify(data?.bbox) || ''}"/>
                          <input name="taxifare" hidden value="${JSON.stringify(data?.taxifare) || ''}"/>
                          <input name="tollfare" hidden value="${JSON.stringify(data?.tollfare) || ''}"/>
                          <textarea name="json" hidden>${data ?JSON.stringify(data) : ''}</textarea>
                          <label for="formFileMultiple" class="form-label">이미지 추가하기</label>
                          <input name="files" class="form-control" type="file" id="formFileMultiple" multiple>
                          <textarea id="saveRouteDescription" name="description"></textarea>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save changes</button>
                      </div>
                  </form>
                </div>
              </div>
            </div>
         `;
        this.onSubmit()
    }
}

export default SavePoiComponent