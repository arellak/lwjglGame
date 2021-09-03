package de.nimble.window;

import de.nimble.utils.Color;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Vector2f;

import java.nio.IntBuffer;

public class Window {

    private String title;
    private Vector2f dimensions;
    private long glfwWindow;
    private Color backgroundColor;

    private boolean resizable;
    private boolean alwaysOnTop;
    private boolean fullScreen;
    private boolean doubleBufferEnabled;
    
    public Window(final String title, final Vector2f dimensions, final Color backgroundColor, final boolean resizable) {
        setTitle(title);
        setDimensions(dimensions);
        setBackgroundColor(backgroundColor);
        setResizable(resizable);
    }

    public void create() {
        this.glfwWindow = GLFW.glfwCreateWindow(
                (int) dimensions.getX(),
                (int) dimensions.getY(),
                this.title,
                fullScreen ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL,
                MemoryUtil.NULL
        );

        if(glfwWindow == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window!");

        centerWindow();

        GLFW.glfwMakeContextCurrent(glfwWindow);
        GLFW.glfwSwapInterval(1);
    }

    public void show() {
        GLFW.glfwShowWindow(getGLFWWindow());
    }

    public void centerWindow() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(glfwWindow, pWidth, pHeight);

            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                    glfwWindow,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDimensions(final Vector2f dimensions) {
        this.dimensions = dimensions;
    }

    public void setDimensions(final float x, final float y) {
        this.dimensions = new Vector2f(x, y);
    }

    public Vector2f getDimensions() {
        return this.dimensions;
    }

    public long getGLFWWindow() {
        return this.glfwWindow;
    }

    public void setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setResizable(final boolean resizable) {
        this.resizable = resizable;
        GLFW.glfwWindowHint(
                GLFW.GLFW_RESIZABLE,
                resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE
        );
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public void setAlwaysOnTop(final boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
        GLFW.glfwWindowHint(
                GLFW.GLFW_FLOATING,
                alwaysOnTop ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE
        );
    }

    public boolean isAlwaysOnTop() {
        return this.alwaysOnTop;
    }

    public void setFullScreen(final boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isFullScreen() {
        return this.fullScreen;
    }

    public void setDoubleBufferEnabled(final boolean doubleBufferEnabled) {
        this.doubleBufferEnabled = doubleBufferEnabled;
        GLFW.glfwWindowHint(
                GLFW.GLFW_DOUBLEBUFFER,
                this.doubleBufferEnabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE
        );
    }

    public boolean isDoubleBufferEnabled() {
        return this.doubleBufferEnabled;
    }

}