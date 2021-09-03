package de.nimble.engine;

import de.nimble.entities.Entity;
import de.nimble.models.RawModel;
import de.nimble.models.TexturedModel;
import de.nimble.shaders.StaticShader;
import de.nimble.textures.ModelTexture;
import de.nimble.utils.Constants;
import de.nimble.utils.Maths;
import de.nimble.window.Window;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        entities.forEach((key, value) -> value.forEach(entity -> {
            prepareTexturedModel(entity.getModel());
            List<Entity> batch = entities.get(entity.getModel());
            batch.forEach(ent -> {
                prepareInstance(ent);
                GL11.glDrawElements(
                        GL11.GL_TRIANGLES,
                        entity.getModel().getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT,
                        0
                );
            });

            unbindTexturedModel();
        }));

    }

    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();

        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);


        ModelTexture texture = model.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    private void unbindTexturedModel() {
        // disable
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotX(),
                entity.getRotY(),
                entity.getRotZ(),
                entity.getScale()
        );

        shader.loadTransformationMatrix(transformationMatrix);
    }

}
