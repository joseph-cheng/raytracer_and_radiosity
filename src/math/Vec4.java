package src.math;

public class Vec4 {
    private double x;
    private double y;
    private double z;
    private double w;

    public Vec4() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
        w = 1.0;
    }

    public Vec4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4(Vec3 v) {
        this.x = v.get_x();
        this.y = v.get_y();
        this.z = v.get_z();
        this.w = 1.0;
    }

    public Vec4 add(Vec4 other) {
        return new Vec4(this.x/this.w + other.x/other.w,
                        this.y/this.w + other.y/other.w,
                        this.z/this.w + other.z/other.w,
                        1.0);
    }

    public Vec4 sub(Vec4 other) {
        return new Vec4(this.x/this.w - other.x/other.w,
                        this.y/this.w - other.y/other.w,
                        this.z/this.w - other.z/other.w,
                        1.0);
    }

    public Vec4 mul(double f) {
        return new Vec4(this.x * f,
                        this.y * f,
                        this.z * f,
                        this.w);
    }

    public Vec4 div(double f) {
        return new Vec4(this.x / f,
                        this.y / f,
                        this.z / f,
                        this.w);
    }

    public double dot(Vec4 other) {
        return this.x/this.w * other.x/other.w + 
               this.y/this.w * other.y/other.w + 
               this.z/this.w * other.z/other.w;
    }

    public double mag2() {
        return this.dot(this);
    }

    public double mag() {
        return Math.sqrt(this.mag2());
    }

    public Vec4 norm() {
        return this.div(this.mag());
    }

    public double get_x() {
        return x;
    }

    public double get_y() {
        return y;
    }

    public double get_z() {
        return z;
    }

    public double get_w() {
        return w;
    }
}
