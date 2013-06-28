package com.witmob.bookopen;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import static android.opengl.GLES20.*;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 13-4-11
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
public class Shader {
    private int program;
    private final HashMap<String, Integer> shaderHandleMap = new HashMap<String, Integer>();
    String vertexShader;

    public int getHandle(String name) {
        if (shaderHandleMap.containsKey(name)) {
            return shaderHandleMap.get(name);
        }
        int handle = glGetAttribLocation(program, name);
        if (handle == -1) {
            handle = glGetUniformLocation(program, name);
        }
        if (handle == -1) {
            throw new RuntimeException("get handle error, handle name:" + name + "\nshader:" + this.vertexShader);
        } else {
            shaderHandleMap.put(name, handle);
        }
        return handle;
    }

    private int loadShader(int shaderType, String source) {
        int shader = glCreateShader(shaderType);
        if (shader == 0) {
            throw new RuntimeException("can not load shader");
        }
        glShaderSource(shader, source);
        glCompileShader(shader);
        int[] compiled = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String error = glGetShaderInfoLog(shader);
            glDeleteShader(shader);
            throw new RuntimeException(error);
        }
        return shader;
    }

    public void setProgram(String vertexSource, String fragmentSource) {
        this.vertexShader = vertexSource;

        int vertexShader = loadShader(GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = loadShader(GL_FRAGMENT_SHADER,
                fragmentSource);
        int program = glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("can not create shader program.");
        }
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        int[] linkStatus = new int[1];
        glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GL_TRUE) {
            String error = glGetProgramInfoLog(program);
            glDeleteProgram(program);
            throw new RuntimeException(error);
        }
        this.program = program;
        shaderHandleMap.clear();
    }

    public void setProgram(Context context, int vertexResourceId, int fragmentResourceId) {
        this.setProgram(readTextFileFromRawResourceId(context, vertexResourceId),
                readTextFileFromRawResourceId(context, fragmentResourceId));
    }

    private static String readTextFileFromRawResourceId(Context context, int resourceId) {
        StringBuilder builder = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(resourceId)));

        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                builder.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return builder.toString();
    }

    public void useProgram() {
        glUseProgram(program);
    }
}
