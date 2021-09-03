package de.nimble;

import de.nimble.engine.MasterRenderer;
import de.nimble.engine.OBJLoader;
import de.nimble.entities.Camera;
import de.nimble.entities.Entity;
import de.nimble.engine.Loader;
import de.nimble.entities.Light;
import de.nimble.models.RawModel;
import de.nimble.models.TexturedModel;
import de.nimble.terrains.Terrain;
import de.nimble.textures.ModelTexture;
import de.nimble.utils.Color;
import de.nimble.utils.Constants;
import de.nimble.window.Window;
import de.nimble.window.WindowBuilder;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game {

    private Window window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        Callbacks.glfwFreeCallbacks(window.getGLFWWindow());
        GLFW.glfwDestroyWindow(window.getGLFWWindow());

        GLFW.glfwTerminate();
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW!");

        window = new WindowBuilder()
                .title(Constants.WINDOW_TITLE)
                .dimensions(new Vector2f(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT))
                .backgroundColor(new Color(0.4f, 0.4f, 0.3f, 0.0f))
                .resizable(false)
                .doubleBufferEnabled(true)
                .build();

        GLFW.glfwSetKeyCallback(window.getGLFWWindow(), (window, key, scancode, action, mods) -> {
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });

        window.show();
    }

    private void loop() {
        GL.createCapabilities();

        Loader loader = new Loader();

        RawModel firstModel = OBJLoader.loadObjModel("tree", loader);

        ModelTexture texture = new ModelTexture(loader.loadTexture("tree.png", true));
        texture.setShineDamper(2);
        texture.setReflectivity(1);

        TexturedModel texturedModel = new TexturedModel(firstModel, texture);

        Entity entity = new Entity(texturedModel, new Vector3f(0, 2, -200), 0, 0, 0, 1);

        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass.png", false)));
        Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass.png", false)));


        MasterRenderer renderer = new MasterRenderer();

        while(!GLFW.glfwWindowShouldClose(window.getGLFWWindow())) {

            // input
            camera.move(window.getGLFWWindow());

            // update
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);

            // render
            renderer.render(light, camera);

            GLFW.glfwSwapBuffers(window.getGLFWWindow());
            GLFW.glfwPollEvents();
        }

        renderer.cleanUp();
        loader.cleanUp();

    }

    public static void main(String[] args) {
        new Game().run();
    }

}
