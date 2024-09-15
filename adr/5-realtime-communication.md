# Realtime Communication - Server Sent Events

* Status: accepted
* Deciders: Dennis
* Date: 2024-05-26

Technical Story: [Bildauswahl ermöglichen](https://github.com/mi-classroom/mi-master-wt-beiboot-2024/issues/2)

## Context and Problem Statement

Extracting images and converting them from a byte array is slow. In order to give the user some feedback realtime communication is necessary. This would allow the frontend to receive images once they are available.

## Decision Drivers <!-- optional -->

* Simplicity
* Image support

## Considered Options

* Server Sent Events
* Polling
* Websockets

## Decision Outcome

Chosen option: "Server Sent Events", because it is the simplest to implement, while still being realtime.
Polling is not actually realtime, but has essentially the same feature set.
Websockets are too complex, but allow sending actual images.

### Positive Consequences
- Easy to implement: Spring has native support and there is a library available for SvelteKit
- Actually realtime

### Negative Consequences
- Can't send images: requires workaround either base64 encoded strings or saving images to local file system
