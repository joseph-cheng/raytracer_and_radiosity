package src.math;

public class Vec3 {
    private double x;
    private double y;
    private double z;

    public Vec3() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(Vec4 v) {
        this.x = v.get_x() / v.get_w();
        this.y = v.get_y() / v.get_w();
        this.z = v.get_z() / v.get_w();
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(this.x + other.x,
                        this.y + other.y,
                        this.z + other.z);
    }

    public Vec3 add(double val) {
        return new Vec3(this.x + val,
                        this.y + val,
                        this.z + val);
    }

    public Vec3 sub(Vec3 other) {
        return new Vec3(this.x - other.x,
                        this.y - other.y,
                        this.z - other.z);
    }

    public Vec3 mul(double f) {
        return new Vec3(this.x * f,
                        this.y * f,
                        this.z * f);
    }

    //element wise mul
    public Vec3 mul(Vec3 other) { 
        return new Vec3(this.x * other.x,
                        this.y * other.y,
                        this.z * other.z);
    }

    public Vec3 div(double f) {
        return new Vec3(this.x / f,
                        this.y / f,
                        this.z / f);
    }

    public double dot(Vec3 other) {
        return this.x * other.x + 
               this.y * other.y + 
               this.z * other.z;
    }

    public double mag2() {
        return this.dot(this);
    }

    public double mag() {
        return Math.sqrt(this.mag2());
    }

    public Vec3 norm() {
        return this.div(this.mag());
    }

    public Vec3 pow(double e) {
        return new Vec3(
                Math.pow(x, e),
                Math.pow(y, e),
                Math.pow(z, e)
                );
    }

    public Vec3 inv() {
        return new Vec3(
                1.0f / this.x,
                1.0f / this.y,
                1.0f / this.z
                );
    }

    public Vec3 cross(Vec3 other) {
        return new Vec3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
                );
    }

    //mirror-like reflection
    public Vec3 reflect_in(Vec3 other) {
        return other.norm().mul(2 * this.dot(other.norm())).sub(this);
    }

    public double get_x() {
        return this.x;
    }

    public double get_y() {
        return this.y;
    }

    public double get_z() {
        return this.z;
    }

    public String toString() {
        return String.format("%f %f %f", x, y, z);
    }
}
