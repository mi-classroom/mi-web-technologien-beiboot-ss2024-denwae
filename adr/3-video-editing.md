# Video Editing Technology + FFmpeg + FFmpeg CLI Wrapper for Java
* Status: accepted <!-- optional -->
* Deciders: Dennis Wäckerle <!-- optional -->
* Date: 2024-04-23 <!-- optional -->

Technical Story: [Kernfunktion im Backend erstellen](https://github.com/mi-classroom/mi-master-wt-beiboot-2024/issues/1) <!-- optional -->

## Context and Problem Statement

How do you split a video in multiple frames?

## Decision Drivers <!-- optional -->

* How up to date
* Ease of availability

## Considered Options

* FFmpeg + [FFmpeg CLI Wrapper for Java](https://github.com/bramp/ffmpeg-cli-wrapper)
* OpenCV + Java Bindings
* GStreamer + Java Bindings

## Decision Outcome

Chosen option: "FFmpeg + FFmpeg CLI Wrapper for Java", because the CLI wrapper is the most developed project and easy to acces project.
GStreamer Java bindings are not maintained: latest commit [2023-06-01](https://github.com/gstreamer-java/gst1-java-core) and [06-09-2023](https://github.com/gstreamer-java/gstreamer-java).
OpenCV bindings are not available through Maven Central.
