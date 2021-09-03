package de.nimble.window;

import de.nimble.utils.Color;
import org.lwjgl.util.vector.Vector2f;

public class WindowBuilder {

    private String title;
    private Vector2f dimensions;
    private Color backgroundColor;
    private boolean resizable;
    private boolean alwaysOnTop;
    private boolean fullScreen;
    private boolean doubleBufferEnabled;

    public WindowBuilder title(final String title) {
        this.title = title;
        return this;
    }

    public WindowBuilder dimensions(final Vector2f dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public WindowBuilder backgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public WindowBuilder resizable(final boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public WindowBuilder alwaysOnTop(final boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
        return this;
    }

    public WindowBuilder fullScreen(final boolean fullScreen) {
        this.fullScreen = fullScreen;
        return this;
    }

    public WindowBuilder doubleBufferEnabled(final boolean doubleBufferEnabled) {
        this.doubleBufferEnabled = doubleBufferEnabled;
        return this;
    }

    public Window build() {
        Window window = new Window(title, dimensions, backgroundColor, resizable);

        window.setAlwaysOnTop(alwaysOnTop);
        window.setFullScreen(fullScreen);
        window.setDoubleBufferEnabled(doubleBufferEnabled);

        window.create();
        return window;
    }

}
