import {writable} from "svelte/store";
import {browser} from "$app/environment";


export const project = writable(
    browser && (localStorage.getItem("projectId") || null)
)

project.subscribe((val) => browser && (localStorage.projectId = val))

export const images = writable(
    browser && (JSON.parse(localStorage.getItem("images") || "[]"))
)
images.subscribe((val) => {
    browser && (localStorage.images = JSON.stringify(val))
})

export const selectedImages = writable([])

export const blendedImage = writable()