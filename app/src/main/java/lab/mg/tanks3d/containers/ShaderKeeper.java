package lab.mg.tanks3d.containers;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lab.mg.tanks3d.utils.ShaderUtils;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;

/**
 * Created by kostya on 24.10.2016.
 */
public class ShaderKeeper {
    private static ShaderKeeper currentInstance = new ShaderKeeper();
    private List<Shader> shaders = new ArrayList<>();

    /**
     * Class that keep all shader programs
     * when you create shader you must add it to this class
     * @return instance
     */
    public static ShaderKeeper getInstance() {
        return currentInstance;
    }

    private ShaderKeeper() {}

    public boolean createShader(Context context, String name, int vertexResourceId, int fragmentResourceId) {
        int vertexId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, vertexResourceId);
        int fragmentId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, fragmentResourceId);
        int programId = ShaderUtils.createProgram(vertexId, fragmentId);
        if (programId <= 0) {
            return false;
        }
        Shader shader = new Shader(name, programId);
        if (contain(shader.getName())) {
            if (remove(shader.getName())) {
                shaders.add(shader);
            } else {
                return false;
            }
        } else {
            shaders.add(shader);
        }

        return true;
    }

    public List<Shader> getAll() {
        return shaders;
    }

    @Nullable
    public Shader getByName(String name) {
        for (Shader shader: shaders) {
            if (shader.getName().equals(name)) {
                return shader;
            }
        }
        return null;
    }

    @Nullable
    public Shader getById(int id) {
        for (Shader shader: shaders) {
            if (shader.getProgramId() == id) {
                return shader;
            }
        }
        return null;
    }

    public boolean contain(String name) {
        for (Shader shader: shaders) {
            if (shader.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean remove(String name) {
        Shader forDelete = null;
        for (Shader shader: shaders) {
            if (shader.getName().equals(name)) {
                forDelete = shader;
            }
        }

        if (forDelete != null) {
            shaders.remove(forDelete);
            return true;
        } else {
            return false;
        }
    }
}
