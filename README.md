# FWT (Flexible Windowing Toolkit)
Flexible Windowing Toolkit for use with <a href="http://www.libgdx.com">LibGDX</a> game engine.

FWT is a lightweight UI toolkit built on top of libGDX. It provides a window manager, XML-driven UI layouts,
and a set of reusable UI components intended for game UIs.

* FWT started development in 2012, when libGDX's UI system was not particularly great.  The current Scene2D system
    is quite capable these days.  However, if you want to develop a complex game with lots of windows and UI elements,
    you may find that FWT provides a robust windowing system for more complex UI development.

## Features

- Window manager with focus, layering, and input routing
- XML-driven UI definitions and default component styling
- Built-in components (window, button, label, slider, scrollable, text field, toggle, etc.)
- Texture atlas and font support for UI rendering

## Requirements

- Java 17
- libGDX (core)
- libGDX freetype (for TTF font generation)

## Install

This repository does not currently publish a standalone artifact. If you split FWT into its own module, add
the dependency using your chosen coordinates. Example:

```gradle
dependencies {
    implementation "com.arboreantears:fwt:<version>"
}
```

## Quick Start

1. Implement `FWTControlInterface` to connect FWT to your game (logging, rendering, assets, input).
2. Initialize FWT:

```java
FWTController.initialize(fwtControlInterface, customFactory);
FWTWindowManager manager = FWTController.createWindowManager();
```

3. Load UI definitions from XML and create components using `XMLUIReader` and `FWTComponentFactory`.

Check out the <a href="https://github.com/thes33/FWT/blob/main/FWT_XML_Schema.xsd">XML Schema</a> for XML definitions.
More information about the <a href="https://github.com/thes33/FWT/blob/main/fwt-xml-dsl.md">XML DSL Specifications</a> can be found.

## Assets and Layout

FWT reads XML UI definitions, fonts, and a UI texture atlas via the paths provided by
`FWTControlInterface`. A typical layout looks like:

```
assets/
  ui/
    UI_defaults.xml
    tooltipBar.xml
  font/
    <font>.ttf
  image/
    uiimage.atlas
```

### Required Atlas Regions (default UI)

The default UI definitions reference these atlas regions:

- `button`, `buttonh`, `buttonp`
- `toggle`, `toggleh`, `togglechk`
- `closebtn`
- `cursor`

If you provide custom UI XML, include any additional regions referenced by those files.

## Build

If FWT is still in this repo, you can build with:

```bash
./gradlew build
```

## License

TBD. Add your chosen license file and update this section.
