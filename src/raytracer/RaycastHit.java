package src.raytracer;

import src.math.Vec3;
import src.scene.SceneObject;
import src.model.Triangle;

public class RaycastHit {
    private double distance;

    private SceneObject scene_object_hit;

    private Triangle triangle_hit;

    private Vec3 location;

    private Vec3 norm;

    public RaycastHit() {
        this.distance = Double.POSITIVE_INFINITY;
    }

    public RaycastHit(SceneObject scene_object_hit, Triangle triangle_hit, double distance, Vec3 location, Vec3 norm) {
        this.scene_object_hit = scene_object_hit;
        this.triangle_hit = triangle_hit;
        this.distance = distance;
        this.location = location;
        this.norm = norm;
    }

    public RaycastHit(Triangle triangle_hit, double distance, Vec3 location, Vec3 norm) {
        this.triangle_hit = triangle_hit;
        this.distance = distance;
        this.location = location;
        this.norm = norm;
    }

    public double get_distance() {
        return this.distance;
    }

    public SceneObject get_scene_object_hit() {
        return this.scene_object_hit;
    }

    public Triangle get_triangle_hit() {
        return this.triangle_hit;
    }


    public Vec3 get_location() {
        return this.location;
    }

    public Vec3 get_norm() {
        return this.norm;
    }


}
