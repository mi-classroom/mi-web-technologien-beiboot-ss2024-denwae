<script>
    import { images, project, selectedImages, blendedImage } from "./stores.js";
    import {frame} from "./sseConnection.js";

    let projectId
    project.subscribe(val => projectId = val)

    frame.subscribe((value) => {
        if (value) {
            let image = {
                type: value.type,
                maxFrame: value.maxFrame,
                currentFrame: value.currentFrame,
                imagePath: value.imagePath,
                weight: 1
            }
            $images = [...$images, image]
            $selectedImages= [...$selectedImages, image]
        }
    })

    function updateWeights(image, weight) {
        let index = $images.findIndex((value) => value === image)
        image.weight = weight
        $images[index] = image;
        if($selectedImages.includes(frame)){
            $selectedImages = $selectedImages.filter((value) => value !== frame)
            $selectedImages = [...$selectedImages, image]
        }
    }

    function toggleSelected(image, event){
        if($selectedImages.includes(image)){
            $selectedImages = $selectedImages.filter(e => e !== image)
        }else if(event.shiftKey){
            let closest = findClosest(image.currentFrame, $selectedImages)
            range(closest, image.currentFrame).forEach(frame => {
                $selectedImages = [...$selectedImages, $images.find(img => img.currentFrame === frame)]
            })

        }
        else{
            $selectedImages = [...$selectedImages, image]
        }
    }

    function findClosest(number, array){
        return  array.map(image => image.currentFrame).sort( (a, b) => Math.abs(number - a) - Math.abs(number - b))[0]
    }

    function range(num1, num2){
        if(num1 > num2)
            return [...Array(num1 - num2).keys()].map(i => i + num2);
        else
            return [...Array(num2 - num1).keys()].map(i => i + num1 + 1)
    }
</script>

<div class="grid-rows-2">
    <div class="overflow-auto max-h-screen">
        {#if $blendedImage}
            <div class="hero">
                <div class="hero-content">
                    <img src={$blendedImage} alt="blended image" loading="lazy"/>
                </div>
            </div>
        {/if}
        <div class="grid grid-cols-6 gap-2">
            {#each $images as image, index}
                <div class:border-primary={$selectedImages.includes(image)} class="card card-bordered border-2 hover:border-4 flex overflow-hidden">
                    <div class="card-body p-0">
                        <img src={image.imagePath} alt="frame" loading="lazy" on:click={event => toggleSelected(image, event)}/>
                        <label for="weight{index}">Gewichtung</label>
                        <input min="0" max={image.maxFrames} id="weight{index}" class="input" type="number" value="1" name="weighting" on:input={weight => updateWeights(image, parseInt(weight.target.value))}/>
                    </div>
                </div>
            {/each}
        </div>
    </div>
</div>

