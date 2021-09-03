package de.nimble.engine;

import de.nimble.models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();

        bindIndicesBuffer(indices);

        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);

        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    public int loadTexture(String fileName, boolean isRGBA) {
        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        // set parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);


        IntBuffer pWidth = BufferUtils.createIntBuffer(1);
        IntBuffer pHeight = BufferUtils.createIntBuffer(1);
        IntBuffer pNrChannels = BufferUtils.createIntBuffer(1);

        ByteBuffer texture = STBImage.stbi_load("src/main/resources/" + fileName, pWidth, pHeight, pNrChannels, 0);

        Optional.ofNullable(texture)
                .ifPresentOrElse(tex -> {

                    if(isRGBA) {
                        GL11.glTexImage2D(
                                GL11.GL_TEXTURE_2D,
                                0,
                                GL11.GL_RGBA,
                                pWidth.get(),
                                pHeight.get(),
                                0,
                                GL11.GL_RGBA,
                                GL11.GL_UNSIGNED_BYTE,
                                texture
                        );
                    } else {
                        GL11.glTexImage2D(
                                GL11.GL_TEXTURE_2D,
                                0,
                                GL11.GL_RGB,
                                pWidth.get(),
                                pHeight.get(),
                                0,
                                GL11.GL_RGB,
                                GL11.GL_UNSIGNED_BYTE,
                                texture
                        );
                    }

                    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
                }, () -> {
                    throw new NullPointerException("Failed to load texture");
                });
        return textureID;
    }

    public void cleanUp() {
        vaos.forEach(GL30::glDeleteVertexArrays);
        vbos.forEach(GL30::glDeleteBuffers);
        textures.forEach(GL30::glDeleteTextures);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL30.glBufferData(GL20.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL30.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
