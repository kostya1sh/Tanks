package lab.mg.tanks3d.containers;

/**
 * Created by kostya on 24.10.2016.
 */

public class Shader {
    private int programId;
    private String name;

    public Shader(String name, int programId) {
        this.name = name;
        this.programId = programId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }
}
