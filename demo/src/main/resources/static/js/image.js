function toImageTag(images){
    let imageList = images?.map((image, i) => ` <div class="carousel-item ${i == 0 ? 'active' : ''}">
                                        <img src="/download/${image.storeFilename}" class="" alt="${image.originalFilename}">
                                    </div>`)
    if (images.length <= 0) {
        imageList = '<img src="/images/img.png" alt="순대국이미지">'
    }
    let indicatorList ='';
    for(var i =0 ; i < images.length;i++){
        indicatorList += `<button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="${i}"
                class="${i === 0 ? 'active':''}" aria-current="true" aria-label="Slide ${i}"></button>`

    }
    return {imageList,indicatorList}
}