package de.nimble.shaders;

import de.nimble.entities.Camera;
import de.nimble.entities.Light;
import de.nimble.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class TerrainShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/main/java/de/nimble/shaders/terrainVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/main/java/de/nimble/shaders/terrainFragmentShader.txt";

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;

    private int locationLightPosition;
    private int locationLightColor;

    private int locationShineDamper;
    private int locationReflectivity;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = getUniformLocation("transformationMatrix");
        locationProjectionMatrix = getUniformLocation("projectionMatrix");
        locationViewMatrix = getUniformLocation("viewMatrix");
        locationLightPosition = getUniformLocation("lightPosition");
        locationLightColor = getUniformLocation("lightColor");
        locationShineDamper = getUniformLocation("shineDamper");
        locationReflectivity = getUniformLocation("reflectivity");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        loadMatrix(locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(locationViewMatrix, viewMatrix);
    }

    public void loadLight(Light light) {
        loadVector(locationLightPosition, light.getPosition());
        loadVector(locationLightColor, light.getColor());
    }

    public void loadShineVariables(float damper, float reflectivity) {
        loadFloat(locationShineDamper, damper);
        loadFloat(locationReflectivity, reflectivity);
    }

}
