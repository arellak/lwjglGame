package de.nimble.entities;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {
        this.position = new Vector3f(3, 0, -150f);
    }

    public void move(long window) {
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            position.x -= 2;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            position.x += 2;
        }

        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            position.z += 2;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            position.z -= 2f;
        }

        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            yaw += 2f;
        }

        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            yaw -= 2f;
        }

        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            pitch -= 2f;
        }

        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
            pitch += 2f;
        }

        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            roll += 2;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) {
            roll -= 2;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

}