function setup()
{
    let img = document.getElementById('img');
    let photos = [];
    let photoNumber = 0;
    let prevButton = document.getElementById("previousButton");
    prevButton.disabled = true;
    let nextButton = document.getElementById("nextButton");
    let photoList = document.getElementById("photos");
    for (let photo of photoList.options)
    {
        photos.push("img/books/" + photo.value);
    }

    if (photos.length < 2) { nextButton.disabled = true; }

    nextButton.onclick = () => {
        prevButton.disabled = false;
        photoNumber++;
        if (photoNumber === (photos.length - 1)) {
            nextButton.disabled = true;
        }
        img.src=photos[photoNumber];
    }

    prevButton.onclick = () => {
        nextButton.disabled = false;
        photoNumber--;
        if(photoNumber === 0) { prevButton.disabled = true; }
        img.src=photos[photoNumber];
    }
}

setup();