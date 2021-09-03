package de.nimble.engine;

import de.nimble.entities.Camera;
import de.nimble.entities.Entity;
import de.nimble.entities.Light;
import de.nimble.models.TexturedModel;
import de.nimble.shaders.StaticShader;
import de.nimble.shaders.TerrainShader;
import de.nimble.terrains.Terrain;
import de.nimble.utils.Constants;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;
import org.lwjgl.util.vector.Matrix4f;

import java.util.*;

public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private StaticShader shader;
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader;

    private Map<TexturedModel, List<Entity>> entities;

    private Matrix4f projectionMatrix;

    private List<Terrain> terrains;

    public MasterRenderer() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        createProjectionMatrix();
        this.shader = new StaticShader();
        this.renderer = new EntityRenderer(shader, projectionMatrix);
        this.entities = new HashMap<>();

        this.terrainShader = new TerrainShader();
        this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        this.terrains = new ArrayList<>();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
        GL33.glClearColor(
                0.3f,
                0.3f,
                0.3f,
                1.0f
        );
    }

    public void render(Light sun, Camera camera) {
        prepare();
        shader.start();
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);

        renderer.render(this.entities);
        shader.stop();

        terrainShader.start();
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        terrains.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        Optional.ofNullable(batch)
                .ifPresentOrElse(test -> {
                    batch.add(entity);
                }, () -> {
                    List<Entity> newBatch = new ArrayList<>();
                    newBatch.add(entity);
                    entities.put(entityModel, newBatch);
                });
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Constants.WINDOW_WIDTH / (float) Constants.WINDOW_HEIGHT;
        float yScale = (float) ((1.0f / Math.tan(Math.toRadians(FOV / 2.0f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32  = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;
    }

    public void cleanUp() {
        this.shader.cleanUp();
        this.terrainShader.cleanUp();
    }

}
