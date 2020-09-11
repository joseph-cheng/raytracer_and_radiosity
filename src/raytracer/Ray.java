package src.raytracer;

import src.math.Vec3;

public class Ray {
    private Vec3 start;
    private Vec3 dir;

    public Ray(Vec3 start, Vec3 dir) {
        this.start = start;
        this.dir = dir;
    }

    public Vec3 get_pos_at(double parameter) {
        return start.add(dir.mul(parameter));
    }

    public Vec3 get_start() {
        return this.start;
    }

    public Vec3 get_dir() {
        return this.dir;
    }
}
