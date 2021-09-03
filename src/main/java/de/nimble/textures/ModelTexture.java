package de.nimble.textures;

public class ModelTexture {

    private int textureID;

    private float shineDamper;
    private float reflectivity;

    public ModelTexture(int textureID) {
        this.textureID = textureID;
        this.shineDamper = 1;
        this.reflectivity = 0;
    }

    public int getTextureID() {
        return this.textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
