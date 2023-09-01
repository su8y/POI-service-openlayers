class PanoElement extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    connectedCallback() {
        this.render();
    }

    adoptCallback() {
    }

    attributeChangedCallback(attrName, oldVal, newVal) {
        this.render();
    }

    static get observedAttributes() {
        return ['coord'];
    }


    // custom element 가 제거될때 호출된다.
    disconnectedCallback() {
        alert('bye bye');
    }

    coord() {
        return this.getAttribute("coord")
            .split(",")
            .map(e => e.trim() * 1);
    }

    movingPosition(coord) {
        this.setAttribute("coord", coord);
    }

    toggleVisible() {
        let pano = this.shadowRoot.querySelector('#pano')
        if (pano.classList.contains('d-none'))
            pano.classList.remove('d-none')
        else
            pano.classList.add('d-none')

    }

    // custom method
    render() {
        let [lon, lat] = this.coord()

        this.shadowRoot.innerHTML = `
                    <style>
                        #pano{
                            width:100vw;
                            height: 80vh;
                            z-index: 1;
                        }
                    </style>
                    <div id="pano" ></div>
         `
        this._pano = new naver.maps.Panorama(this.shadowRoot.querySelector("#pano"), {
            position: new naver.maps.LatLng(lat, lon),
            pov: {
                pan: -135,
                tilt: 29,
                fov: 100
            }
        });

        const observer = new ResizeObserver(entries => {
            for (let entry of entries) {
                const {width, height} = entry.contentRect;
                var newSize = new naver.maps.Size();
                newSize.width = Math.round(width)
                newSize.height = Math.round(height)
                this._pano.setSize(newSize.clone());
            }
        });
        observer.observe(this);

    }

}

export default PanoElement