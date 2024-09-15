# Video Editing Technology - JavaCV FFmpeg
* Status: accepted <!-- optional -->
* Deciders: Dennis Wäckerle <!-- optional -->
* Date: 2024-04-27 <!-- optional -->

Technical Story: [Kernfunktion im Backend erstellen](https://github.com/mi-classroom/mi-master-wt-beiboot-2024/issues/1) <!-- optional -->

## Context and Problem Statement

How do you split a video in multiple frames?

## Decision Drivers <!-- optional -->

* How up to date
* Portability
* Ease of availability
* Compatability
* Ease of use

## Considered Options

* JavaCV FFmpeg
* FFmpeg + [FFmpeg CLI Wrapper for Java](https://github.com/bramp/ffmpeg-cli-wrapper)
* OpenCV + Java Bindings
* GStreamer + Java Bindings

## Decision Outcome

Chosen option: "JavaCV FFmpeg", because it has the most convenient API. It also can access the FFmpeg CLI. It is not necessary to install FFmpeg locally to use JavaCV FFmpeg.
CLI wrapper needs to work with the local file system and requires local FFmpeg installation.
GStreamer Java bindings are not maintained: latest commit [2023-06-01](https://github.com/gstreamer-java/gst1-java-core) and [06-09-2023](https://github.com/gstreamer-java/gstreamer-java).
OpenCV bindings are not compatible with Java 21.

### Positive Consequences
- Java/Kotlin API
- No FFmpeg required locally -> more portable, easier to set up

### Negative Consequences
- Slower than CLI usage
- More complex code required
- Bad documentation