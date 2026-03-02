# FWT XML UI DSL

This document describes the XML DSL used to create FWT UI components and windows in this repo.
The DSL is consumed by `XMLUICreator` (full window inflation) and `XMLUIReader` (single component specs).

## File discovery

- `XMLUICreator.create("someFile")` searches `FWTControlInterface.getOverrideUIFilePath()` first,
  then `FWTControlInterface.getUIFilePath()` for `someFile.xml`.
- Default values are loaded from the file named by
  `FWTControlInterface.getDefaultComponentPropertiesFilename()` (in this repo: `desktop/data/ui/UI_defaults.xml`).
- Defaults are merged with XML attributes, where the XML attributes win.

## Structure

Each XML file is a `<root>` with nested component elements. The element name is the FWT component type.
Nesting determines parent/child relationships (e.g., components inside a `<window>` belong to that window).

```xml
<root>
    <window name="main" width="500" height="500" position="420,140">
        <textbutton name="ok" position="20,20" width="120" height="40" labeltext="OK" />
    </window>
</root>
```

### Required attributes

- `name` is required for all elements (used for lookup by `XMLUIReader`).

### Automatic attributes

`XMLUICreator` populates these after parsing:

- `type` (element name), `depth`, `objectname`, `parentfile`.

## Value formats

### Colors

Parsed via `XMLDataPacket.getColor`:

- `r|g|b|a` or `r|g|b` (floats)
- `FWTColors` and `Color` names (case-insensitive), e.g. `dark_brown`, `black`

### Lists

`a|b|c` (pipe-separated) or `a,b,c` in places that use `StringTokenizer("|,")`.

### Key/value bags

`data="key:value|other:123"` splits into extra attributes on the component.

## Position and size DSL

These are parsed in `FWTComponent.resize(...)` and apply to `position`, `width`, `height`,
`maxwidth`, and `maxheight`.

### position

Two forms are supported:

1) **Explicit X,Y**

```
position="x,y"
```

`x` and `y` can be:

- Absolute: `100`, `20`
- Relative to parent: `width-32`, `height-10`
- Relative to screen: `swidth-32`, `sheight-10`
- Percent of parent: `%50` (50% of parent width/height)
- Keywords: `left`, `right`, `center`, `top`, `bottom`

2) **Relative keyword (single token)**

```
position="topleft" | "topright" | "bottomleft" | "bottomright"
position="topcenter" | "bottomcenter" | "leftcenter" | "rightcenter" | "center"
```

### width / height

Supported values:

- Absolute: `120`
- `max` (fills parent dimension, adjusted for borders)
- Relative to parent: `width-32`, `height-10`
- Relative to parent with no border offset (height only): `pheight-10`
- Percent of parent: `%50`

### maxwidth / maxheight

Same syntax as `width`/`height` except `max` is not meaningful.

## Common attributes (all components)

These come from `FWTComponent.applyDataParameters(...)`.

| Attribute | Type/format | Description |
| --- | --- | --- |
| `name` | string | Unique component name, required for XML lookups. |
| `position` | DSL | X/Y position relative to parent or screen. See Position and size DSL. |
| `width` | DSL | Width in pixels, percent, or relative to parent. |
| `height` | DSL | Height in pixels, percent, or relative to parent. |
| `maxwidth` | DSL | Optional max width clamp. |
| `maxheight` | DSL | Optional max height clamp. |
| `tooltip` | string | Language key for tooltip. Empty disables. |
| `bordercolor` | color | Base border color. |
| `highlightbordercolor` | color | Border color on hover/focus. |
| `backgroundcolor` | color | Base background color. |
| `highlightcolor` | color | Background color on hover/focus. |
| `backgroundtexture` | string | UI texture id for background. |
| `highlighttexture` | string | UI texture id for highlight. |
| `bordertexture` | string | UI texture id for border. |
| `border` | boolean | `false` hides border and clears border texture. |
| `texturefill` | `stretched` or `tiled` | Scaling mode for textures. |
| `data` | `key:value|...` | Extra key/value pairs inserted into the data packet. |

## Standard component types and attributes

The defaults file (`desktop/data/ui/UI_defaults.xml`) lists every supported attribute. The
tables below describe all attributes handled by the code.

### window

| Attribute | Type/format | Description |
| --- | --- | --- |
| `title` | string | Window title text id (resolved via `Language.get`). |
| `closable` | boolean | Adds a close button and allows closing. |
| `extendwidth` | boolean | Allows width extension by the manager. |
| `extendheight` | boolean | Allows height extension by the manager. |
| `borderbar` | boolean | Thick border bar around window. |
| `passivebordercolor` | color | Border color when not active. |
| `movable` | boolean | Allows dragging the window. |
| `locked` | string | Lock to `north`, `south`, `east`, `west` (or empty). |
| `minwidth` | int | Minimum width clamp. |
| `minheight` | int | Minimum height clamp. |

### button

| Attribute | Type/format | Description |
| --- | --- | --- |
| `pressedcolor` | color | Fill color while pressed. |
| `pressedtexture` | string | Texture id while pressed. |
| `icontexture` | string | Icon texture id for button content. |

### textbutton

| Attribute | Type/format | Description |
| --- | --- | --- |
| `fontcolor` | color | Base text color. |
| `highlightfontcolor` | color | Text color on hover/focus. |
| `pressedfontcolor` | color | Text color while pressed. |
| `fontsize` | int | Font size lookup in `Fonts`. |
| `labelid` | string | Language key for label text. |
| `labeltext` | string | Raw label text (overrides `labelid`). |
| `autosize` | boolean | Auto-adjust font size to fit. |
| `wordwrap` | boolean | Wrap text within component bounds. |
| `padding` | int | Inner padding in pixels. |
| `textalignment` | string | `left`, `right`, `up`, `down`, `center`, `bottomleft`, `bottomright`, `topleft`, `topright`. |

### label

| Attribute | Type/format | Description |
| --- | --- | --- |
| `fontcolor` | color | Text color. |
| `fontsize` | int | Font size lookup in `Fonts`. |
| `labelid` | string | Language key for label text. |
| `labeltext` | string | Raw label text (overrides `labelid`). |
| `autosize` | boolean | Auto-adjust font size to fit. |
| `wordwrap` | boolean | Wrap text within component bounds. |
| `markup` | boolean | Enables libGDX font markup. |
| `padding` | int | Inner padding in pixels. |
| `textalignment` | string | `left`, `right`, `up`, `down`, `center`, `bottomleft`, `bottomright`, `topleft`, `topright`. |

### icon

| Attribute | Type/format | Description |
| --- | --- | --- |
| `icontexture` | string | Texture id used as the icon. |

### progressbar

| Attribute | Type/format | Description |
| --- | --- | --- |
| `barcolor` | color | Fill color for the bar. |
| `bartexture` | string | Texture id for the bar fill. |
| `orientation` | string | `horizontal` or `vertical`. |
| `progress` | float | 0..1 progress value. |
| `fontcolor` | color | Text color. |
| `fontsize` | int | Font size lookup. |
| `labelid` | string | Language key for label text. |
| `labeltext` | string | Raw label text (overrides `labelid`). |
| `showpercentage` | boolean | Show percent text overlay. |

### slider

| Attribute | Type/format | Description |
| --- | --- | --- |
| `orientation` | string | `horizontal` or `vertical`. |
| `barsize` | int | Size of the slider handle. |
| `padding` | int | Inner padding in pixels. |
| `keyscrolling` | boolean | Allow keyboard scrolling. |
| `wheelscrolling` | boolean | Allow mouse wheel scrolling. |
| `valuerange` | `min,max` | Range of values. |
| `value` | int | Initial value. |
| `fontsize` | int | Base label font size. |
| `labelfontsize` | int | Label text font size. |
| `numfontsize` | int | Value number font size. |
| `fontcolor` | color | Base font color. |
| `labelfontcolor` | color | Label text color. |
| `numfontcolor` | color | Value number color. |
| `labelid` | string | Language key for label text. |
| `labelstring` | string | Raw label text (overrides `labelid`). |
| `numlabels` | boolean | Show numeric labels along the bar. |
| `shownumbers` | boolean | Show the current value number. |

### textfield

| Attribute | Type/format | Description |
| --- | --- | --- |
| `fontcolor` | color | Base text color. |
| `highlightfontcolor` | color | Text color on focus. |
| `fontsize` | int | Font size lookup. |
| `text` | string | Initial text. |
| `wordwrap` | boolean | Wrap text. |
| `padding` | int | Inner padding in pixels. |
| `maxlength` | int | Maximum character count. |
| `allowedchars` | string | Character whitelist, empty allows all. |
| `msgtext` | string | Placeholder text. |

### togglebutton

| Attribute | Type/format | Description |
| --- | --- | --- |
| `pressedcolor` | color | Fill color when toggled/pressed. |
| `pressedtexture` | string | Texture id when pressed. |
| `icontexture` | string | Base icon texture id. |
| `pressedicontexture` | string | Icon texture id when pressed. |

### scrollable

| Attribute | Type/format | Description |
| --- | --- | --- |
| `verticalscroll` | boolean | Enables vertical scrolling. |
| `horizontalscroll` | boolean | Enables horizontal scrolling. |
| `virtualwidth` | int | Virtual content width. |
| `virtualheight` | int | Virtual content height. |
| `virtualstart` | `x|y` | Initial virtual offset, supports `left|right` and `top|bottom`. |
| `showverticalscroll` | boolean | Shows vertical scrollbar. |
| `showhorizontalscroll` | boolean | Shows horizontal scrollbar. |
| `barsize` | int | Size of scrollbar handle. |
| `keyscrolling` | boolean | Enables key scrolling. |
| `buttonbordercolor` | color | Border color for scrollbar buttons. |
| `buttonhighlightbordercolor` | color | Highlight border for scrollbar buttons. |
| `buttonbackgroundcolor` | color | Background color for scrollbar buttons. |
| `buttonhighlightcolor` | color | Highlight background for scrollbar buttons. |
| `buttonpressedcolor` | color | Pressed background for scrollbar buttons. |
| `buttonbordertexture` | string | Border texture for scrollbar buttons. |
| `buttonbackgroundtexture` | string | Background texture for scrollbar buttons. |
| `buttonhighlighttexture` | string | Highlight texture for scrollbar buttons. |
| `buttonpressedtexture` | string | Pressed texture for scrollbar buttons. |
| `buttontexturefill` | `stretched` or `tiled` | Texture fill for scrollbar buttons. |
| `buttonborder` | boolean | Show border for scrollbar buttons. |
| `barbordercolor` | color | Scrollbar track border color. |
| `barhighlightcolor` | color | Scrollbar track highlight color. |
| `barbackgroundcolor` | color | Scrollbar track background color. |
| `barbordertexture` | string | Scrollbar track border texture. |
| `barbackgroundtexture` | string | Scrollbar track background texture. |
| `barhighlighttexture` | string | Scrollbar track highlight texture. |
| `bartexturefill` | `stretched` or `tiled` | Texture fill for scrollbar track. |
| `barborder` | boolean | Show border for scrollbar track. |

### scrollpanel

| Attribute | Type/format | Description |
| --- | --- | --- |
| `verticalscroll` | boolean | Enables vertical scrolling. |
| `horizontalscroll` | boolean | Enables horizontal scrolling. |
| `virtualwidth` | int | Virtual content width. |
| `virtualheight` | int | Virtual content height. |
| `virtualstart` | `x|y` | Initial virtual offset, supports `left|right` and `top|bottom`. |
| `showverticalscroll` | boolean | Shows vertical scrollbar. |
| `showhorizontalscroll` | boolean | Shows horizontal scrollbar. |
| `showscrollbar` | boolean | Master toggle for the scrollbar. |
| `barsize` | int | Size of scrollbar handle. |
| `keyscrolling` | boolean | Enables key scrolling. |
| `wheelscrolling` | boolean | Enables mouse wheel scrolling. |
| `gapdistance` | int | Gap between child components. |
| `maxcompsize` | int | Optional max size for child components. |
| `orientation` | string | `horizontal` or `vertical` layout. |
| `buttonbordercolor` | color | Border color for scrollbar buttons. |
| `buttonhighlightbordercolor` | color | Highlight border for scrollbar buttons. |
| `buttonbackgroundcolor` | color | Background color for scrollbar buttons. |
| `buttonhighlightcolor` | color | Highlight background for scrollbar buttons. |
| `buttonpressedcolor` | color | Pressed background for scrollbar buttons. |
| `buttonbordertexture` | string | Border texture for scrollbar buttons. |
| `buttonbackgroundtexture` | string | Background texture for scrollbar buttons. |
| `buttonhighlighttexture` | string | Highlight texture for scrollbar buttons. |
| `buttonpressedtexture` | string | Pressed texture for scrollbar buttons. |
| `buttontexturefill` | `stretched` or `tiled` | Texture fill for scrollbar buttons. |
| `buttonborder` | boolean | Show border for scrollbar buttons. |
| `barbordercolor` | color | Scrollbar track border color. |
| `barbackgroundcolor` | color | Scrollbar track background color. |
| `barhighlightcolor` | color | Scrollbar track highlight color. |
| `barbordertexture` | string | Scrollbar track border texture. |
| `barbackgroundtexture` | string | Scrollbar track background texture. |
| `barhighlighttexture` | string | Scrollbar track highlight texture. |
| `bartexturefill` | `stretched` or `tiled` | Texture fill for scrollbar track. |
| `barborder` | boolean | Show border for scrollbar track. |

### listscrollable

List scrollables also respect the scrollable attributes above.

| Attribute | Type/format | Description |
| --- | --- | --- |
| `gapdistance` | int | Gap between list items. |
| `compwidth` | string | Child width (same DSL as `width`). |
| `compheight` | string | Child height (same DSL as `height`). |
| `compsize` | string | Convenience `compwidth|compheight`. |
| `orientation` | string | `horizontal` or `vertical` list layout. |
| `compborder` | boolean | Show borders on child components. |
| `compbordercolor` | color | Child border color. |
| `compbackgroundcolor` | color | Child background color. |
| `comphighlightcolor` | color | Child highlight color. |
| `comppressedcolor` | color | Child pressed color. |
| `compbackgroundtexture` | string | Child background texture id. |
| `comppressedtexture` | string | Child pressed texture id. |
| `comphighlighttexture` | string | Child highlight texture id. |
| `comptexturefill` | `stretched` or `tiled` | Child texture fill mode. |

## Custom component types

Custom element types are supported via `IFWTComponentFactory`. In this repo, the
custom factory (`EWFWTCustomFactory`) adds the types below. These types also read
their defaults from `UI_defaults.xml` under the same element name.

### gameicon

Inherits `icon` attributes and adds:

| Attribute | Type/format | Description |
| --- | --- | --- |
| `icontype` | string | Icon category (`core`, `condition`, `construct`, `creature`, `object`, `plant`, `recipe`, `skill`, `world`). |

### iconbutton

Inherits `button` attributes and adds:

| Attribute | Type/format | Description |
| --- | --- | --- |
| `icontype` | string | Icon category (`core`, `condition`, `construct`, `creature`, `object`, `plant`, `recipe`, `skill`, `world`). |

### itemslot

Inherits `button` attributes. No additional XML attributes are parsed.

### liquidtank

Inherits `button` attributes. No additional XML attributes are parsed.

## Tooltip bar XML

The tooltip bar is configured separately via `desktop/data/ui/tooltipBar.xml` and is read
by `UIToolTipBar`.

| Attribute | Type/format | Description |
| --- | --- | --- |
| `border` | boolean | Draws the border. |
| `bordercolor` | color | Border color. |
| `backgroundcolor` | color | Tooltip background fill. |
| `backgroundtexture` | string | Background texture id. |
| `fontsize` | int | Font size lookup. |

Note: `UIToolTipBar` reads `backgroundtexture` (not `bgtexture`).

## Getting started example

Minimal XML file (`desktop/data/ui/minimalWindow.xml`):

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<root>
    <window
        name="minimalWindow"
        title="Example"
        width="420"
        height="260"
        position="center"
        border="true"
        movable="true">

        <textbutton
            name="okButton"
            position="width-140|20"
            width="120"
            height="40"
            labeltext="OK" />

        <label
            name="desc"
            position="20|height-60"
            width="width-40"
            height="40"
            labeltext="Hello from FWT XML" />
    </window>
</root>
```

Minimal libGDX wiring (create and show the window):

```java
FWTWindowManager windowManager = FWTController.createWindowManager();
FWTWindow window = XMLUICreator.create("minimalWindow");
windowManager.addWindow(window);
```

You still need to call `windowManager.render()` each frame and route input to the
window manager in your `InputProcessor`. FWT expects Y to be in bottom-left origin
space, so flip the Y coordinate before passing it in:

```java
@Override
public boolean touchDown(int x, int y, int pointer, int button) {
    y = Gdx.graphics.getHeight() - y;
    return windowManager.touchDown(x, y, pointer, button);
}

@Override
public boolean touchUp(int x, int y, int pointer, int button) {
    y = Gdx.graphics.getHeight() - y;
    return windowManager.touchUp(x, y, pointer, button);
}

@Override
public boolean touchDragged(int x, int y, int pointer) {
    y = Gdx.graphics.getHeight() - y;
    return windowManager.touchDragged(x, y, pointer);
}

@Override
public boolean mouseMoved(int x, int y) {
    y = Gdx.graphics.getHeight() - y;
    return windowManager.mouseMoved(x, y);
}

@Override
public boolean scrolled(float amountX, float amountY) {
    return windowManager.scrolled(amountX, amountY);
}

@Override
public boolean keyDown(int keycode) {
    return windowManager.keyDown(keycode);
}

@Override
public boolean keyUp(int keycode) {
    return windowManager.keyUp(keycode);
}

@Override
public boolean keyTyped(char character) {
    return windowManager.keyTyped(character);
}
```

## Reference XML

For complete examples:

- `desktop/data/ui/testingWindow1.xml`
- `desktop/data/ui/UI_defaults.xml`
