export function toImageTag(images) {
    let imageList = images?.map((image, i) => ` <div class="carousel-item ${i == 0 ? 'active' : ''}">
                                        <img src="/download/${image.storeFilename}" class="" alt="${image.originalFilename}" onerror="this.src='/images/img.png';">
                                    </div>`)
    if (!Array.isArray(images) || images.length <= 0) {
        imageList = '<img src="/images/no-image.png" alt="순대국이미지">'
        return {imageList: imageList}
    }
    const indicatorList = IndicatorTemplate(images.length);
    return {imageList, indicatorList}
}

const IndicatorTemplate = (length) => {
    const numbers = Array.from({length: length}, (_, index) => index);
    let indicatorList = numbers.map(number => `<button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="${number}"
                class="${number === 0 ? 'active' : ''}" aria-current="true" aria-label="Slide ${number}"></button>`)
    return indicatorList.join("");
}

