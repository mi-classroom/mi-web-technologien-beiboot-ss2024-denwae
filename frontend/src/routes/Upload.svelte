<script>
    import {project, images, selectedImages, blendedImage} from "./stores.js";
    import {registered, started, finished, frame} from "./sseConnection.js";

    let video;
    let splitting = false;
    let maxFrame = 100
    let currentFrame = 0
    let downSample = true;


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
        formData.append("emitterId", $registered)
        formData.append("downSample", downSample.toString())
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
        $blendedImage = null
        const response = await fetch("api/blend-images", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                project: $project,
                images: selectedImagesToFrameArray()
            })
        })
        await response.text().then(value => {
            $blendedImage = value
        })
        blending = false
    }

    function selectedImagesToFrameArray() {
        return $selectedImages.flatMap(value =>
            Array(value.weight).fill(value.currentFrame)
        )
    }

    function selectAllFrames() {
        $selectedImages = $images.map(value => value)
    }

    function deselectAllFrames() {
        $selectedImages = []
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
            <div class="tooltip" data-tip="Die Horizontale Auflösung wird auf 1280p reduziert. Das Seitenverhältnis wird beibehalten.">
                <div class="form-control">
                    <label class="label cursor-pointer">
                        <span class="label-text">Mit verringerter Auflösung verarbeiten</span>
                        <span class="badge badge-primary badge-outline mr-2">?</span>
                        <input type="checkbox" class="checkbox checkbox-primary" bind:checked={downSample}/>
                    </label>
                </div>
            </div>
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
                {#if $selectedImages.length < $images.length}
                    <button class="btn btn-primary" on:click={selectAllFrames}>Alle Frames auswählen</button>
                {:else }
                    <button class="btn btn-primary" on:click={deselectAllFrames}>Alle Frames abwählen</button>
                {/if}
            {/if}
            <div class="hero bg-base-200">
                <div class="hero-content text-center">
                    <div class="max-w-md">
                        <h1 class="text-3xl font-bold">Anleitung</h1>
                        <p class="py-2">
                            Dieses Tool erlaubt es Videos hochzuladen und daraus <a href="https://de.wikipedia.org/wiki/Langzeitbelichtung">Langzeitbelichtungsbilder</a> zu erstellen.
                        </p>
                        <p class="py-2">
                            Zum Auswählen von Bildern kann der Knopf oben genutzt werden, um alle Bilder aus- oder abzuwählen. Mit einem Klick auf ein Bild können einzelne Bilder ausgewählt werden.
                        </p>
                        <p class="py-2">
                            Wenn <kbd class="kbd">Shift</kbd> gedrückt gehalten wird dann kann eine Serie von Bildern ausgewählt werden. Die Serie beginnt immer beim nächsten bereits ausgewähltem Bild und endet beim angeklickten Bild.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>