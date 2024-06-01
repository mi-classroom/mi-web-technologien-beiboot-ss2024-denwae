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


    function toggleSelected(frameNumber){
        if($selectedImages.includes(frameNumber)){
            $selectedImages = $selectedImages.filter(e => e !== frameNumber)
        }else
            $selectedImages = [...$selectedImages, frameNumber]
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
        {#each $images as image}
            <div class:border-primary={$selectedImages.includes(image.currentFrame)} class="card card-bordered border-2 hover:border-4 flex overflow-hidden" on:click={() => toggleSelected(image.currentFrame)}>
                <div class="card-body p-0">
                    <img src={image.imagePath} alt="frame" loading="lazy"/>
                </div>
            </div>
        {/each}
    </div>
</div>

