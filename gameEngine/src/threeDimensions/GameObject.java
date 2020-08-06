package threeDimensions;

import math.Vec3;

public class GameObject {

    protected Mesh mesh;
    
    protected final Vec3 position;
    
    protected float scale;

    protected final Vec3 rotation;

    public GameObject() {
        position = new Vec3(0, 0, 0);
        scale = 1;
        rotation = new Vec3(0, 0, 0);
    }
    
    public GameObject(Mesh mesh) {
        this();
        this.mesh = mesh;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vec3 getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
    
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}
