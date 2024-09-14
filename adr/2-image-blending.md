# Image blending - ImageJ

* Status: accepted
* Deciders: Dennis Wäckerle <!-- optional -->
* Date: 25.04.2024 <!-- optional -->

Technical Story: [Kernfunktion im Backend erstellen](https://github.com/mi-classroom/mi-master-wt-beiboot-2024/issues/1) <!-- optional -->

## Context and Problem Statement

The extracted frames must be blended. FFmpeg is not suitable for this use case therefore another library/tool must be used.

## Decision Drivers <!-- optional -->

* Ease of installation
* Maintenance of tool
* Ease of use
* Documentation
* Maintainability
* Library/tool feature set

## Considered Options

* ImageMagick + Java Binding
* ImageJ
* OpenIMAJ
* AWT Graphics library

## Decision Outcome

Chosen option: "ImageJ", because it supports the necessary image blending with `ZProjector` and is still maintained.

ImageMagick Java and Kotlin Bindings are not maintained ([JMagick](http://www.jmagick.org/) website is offline; [KMagick](https://github.com/MolotovCherry/kmagick) is on hold; [im4java](https://im4java.sourceforge.net/) last update 2012-27-12)
OpenIMAJ is also not maintained ([Last commit](https://github.com/openimaj/openimaj) on 2022-02-09). AWT Graphics functions are designed for UI, not image processing.

### Positive Consequences

- No need to implement custom image blending
- Better documentation than other options
- Better maintained than other options

### Negative Consequences

- Spotty documentation
- Few customization options