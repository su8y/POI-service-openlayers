import PanoElement from './PanoElement.js'

(function () {
    const panoElement = document.querySelector('pano-element')
    const panoPoiList = document.querySelector('.pano-footer')
    const panoCloseBtn = document.querySelector('.js-pano-close');
    window.customElements.define('pano-element', PanoElement);

    panoCloseBtn.addEventListener('click',
        event => {
            const panoBox = document.querySelector('#pano-box')
            panoBox.classList.add('d-none')
        })

    panoPoiList.addEventListener('click', event => {
        if (event.target.tagName === "LI") {
            let target = event.target;
            const coord = target.getAttribute("coord");
            if(!coord) {
                alert("해당 위치로 이동할수 없습니다.")
                throw new Error("coord가 없는 LI")
            }
            panoElement.movingPosition(coord)
        }

    })
}())
