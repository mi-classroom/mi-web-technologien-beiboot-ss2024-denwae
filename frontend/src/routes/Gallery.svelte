<script>
    import { images, project, selectedImages, blendedImage } from "./stores.js";
    import {frame} from "./sseConnection.js";

    let projectId
    project.subscribe(val => projectId = val)

    frame.subscribe((value) => {
        if (value) {
            $selectedImages= [...$selectedImages, value.currentFrame]
            images.update((store) => [...store, value])
        }
    })

    const weights = []

    function updateWeights(frame, weight) {
        const frameWeight = {
            frame: frame,
            weight: weight
        }
        if (weights.find((value) => value.frame === frame)) {
            weights[weights.findIndex((value) => value.frame === frame)] = frameWeight;
        }else{
            weights.push(frameWeight)
        }
        if($selectedImages.includes(frame)){
            $selectedImages = $selectedImages.filter((value) => value !== frame)
            addFrames(frame)
        }
    }

    function toggleSelected(frameNumber){
        if($selectedImages.includes(frameNumber)){
            $selectedImages = $selectedImages.filter(e => e !== frameNumber)
        }else{
            addFrames(frameNumber)
        }
    }

    function addFrames(frame) {
        const res = weights.find(value => value.frame === frame);
        const frameWeight = res ? res.weight : 1
        const newFrames = new Array(frameWeight).fill(frame, 0)
        $selectedImages = [...$selectedImages, ...newFrames]
        console.log($selectedImages)
    }
</script>

<div class="grid-rows-2">
    {#if $blendedImage}
        <div class="hero">
            <div class="hero-content">
                <img src={$blendedImage} alt="blended image" loading="lazy"/>
            </div>
        </div>
    {/if}
    <div class="grid grid-cols-6 gap-2 overflow-auto max-h-screen">
        {#each $images as image, index}
            <div class:border-primary={$selectedImages.includes(image.currentFrame)} class="card card-bordered border-2 hover:border-4 flex overflow-hidden">
                <div class="card-body p-0">
                    <img src={image.imagePath} alt="frame" loading="lazy" on:click={() => toggleSelected(image.currentFrame)}/>
                    <label for="weight{index}">Gewichtung</label>
                    <input min="0" max={image.maxFrames} id="weight{index}" class="input" type="number" value="1" name="weighting" on:input={weight => updateWeights(image.currentFrame, parseInt(weight.target.value))}/>
                </div>
            </div>
        {/each}
    </div>
</div>

