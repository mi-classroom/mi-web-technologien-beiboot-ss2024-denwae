<script>
    import {project, images, selectedImages, blendedImage} from "./stores.js";
    import {registered, started, finished, frame} from "./sseConnection.js";

    let video;
    let splitting = false;
    let maxFrame = 100
    let currentFrame = 0


    frame.subscribe(value => {
        if(value)
            currentFrame = value.currentFrame
    })

    started.subscribe(value => {
        if (value){
            images.set([])
            splitting = true
            maxFrame = value.maxFrame
        }
    })

    finished.subscribe(value => {
        if (value) splitting = false
    })

    async function uploadVideo() {
        const file = video.files[0]
        let formData = new FormData()
        console.log($registered)
        formData.append("emitterId", $registered)
        formData.append("file", file)
        const response = await fetch("api/split-video", {
            method: "POST",
            body: formData,
        })
        const projectId = await response.json();
        project.set(projectId)
    }

    let blending = false
    async function longExpose() {
        blending = true
        const response = await fetch("api/blend-images", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                project: $project,
                images: $selectedImages
            })
        })
        await response.text().then(value => {
            $blendedImage = value
        })
        blending = false
    }

    function selectAllFrames() {
        $selectedImages = $images.map(value => value.currentFrame)
    }
</script>

<div class="drawer drawer-open">
    <input id="my-drawer-2" type="checkbox" class="drawer-toggle" />
    <div class="drawer-side">
        <label for="my-drawer-2" aria-label="close sidebar" class="drawer-overlay"></label>
        <div class="menu p-4 w-80 min-h-full bg-base-200 text-base-content space-y-2">
            <!-- Sidebar content here -->
            <label for="video">Video hochladen</label>
            <input id="video" name="file" type="file" class="file-input w-full max-w-xs" bind:this={video}/>
            <div>
                {#if !splitting}
                    <button class="btn btn-primary" on:click={uploadVideo}>Video aufteilen</button>
                {:else }
                    <progress class="progress" value={currentFrame} max={maxFrame}>Video wird aufgeteilt</progress>
                {/if}
            </div>
            {#if !$images.isEmpty}
                {#if blending}
                    <progress class="progress"></progress>
                {:else }
                    <button class="btn btn-primary" on:click={longExpose}>Langzeitbelichtungsbild erstellen</button>
                {/if}
                <button class="btn btn-primary" on:click={selectAllFrames}>Alle Frames auswählen</button>
            {/if}
        </div>
    </div>
</div>