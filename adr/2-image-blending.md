# Image blending - ImageJ

* Status: accepted
* Deciders: Dennis Wäckerle <!-- optional -->
* Date: 25.04.2024 <!-- optional -->

Technical Story: [Kernfunktion im Backend erstellen](https://github.com/mi-classroom/mi-master-wt-beiboot-2024/issues/1) <!-- optional -->

## Context and Problem Statement

The extracted frames must be blended. FFmpeg is not suitable for this use case therefore another library/tool must be used.

## Decision Drivers <!-- optional -->

* Ease of installation
* Ease of use
* Maintainability
* Library/tool feature set

## Considered Options

* ImageMagick + Java Binding
* ImageJ
* OpenIMAJ
* AWT Graphics library

## Decision Outcome

Chosen option: "ImageJ", because ImageMagick Java Bindings and OpenIMAJ are not maintained. AWT Graphics functions are designed for
UI not image processing.
