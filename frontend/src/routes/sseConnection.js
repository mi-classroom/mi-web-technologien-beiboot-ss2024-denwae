import {source} from "sveltekit-sse";

const sseConnection = source("/api/split-video/events");

export const started = sseConnection.select("STARTED").json(function or({error, raw, previous}){
    if(raw !== "")
        console.error(`Could not parse "${raw}" as json.`, error)
    return previous
})

export const frame = sseConnection.select("SPLIT").json(
    function or({error, raw, previous}){
        if(raw !== "")
            console.error(`Could not parse "${raw}" as json.`, error)
        return previous
    }
)

export const finished = sseConnection.select("FINISHED").json(function or({error, raw, previous}){
    if(raw !== "")
        console.error(`Could not parse "${raw}" as json.`, error)
    return previous
})

export const registered = sseConnection.select("registered")