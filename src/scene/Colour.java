package src.scene;

import src.math.Vec3;

public class Colour {
    private Vec3 v;
    public final double r;
    public final double g;
    public final double b;

    public Colour(double u) {
        this.v = new Vec3(u, u, u);
        this.r = u;
        this.g = u; 
        this.b = u;
    }

    public Colour(double r, double g, double b) {
        this.v = new Vec3(r, g, b);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Colour(Vec3 v) {
        this.v = v;
        this.r = v.get_x();
        this.g = v.get_y();
        this.b = v.get_z();
    }

    public Colour add(Colour other) {
        return new Colour(this.v.add(other.v));
    }

    public Colour add(double val) {
        return new Colour(this.v.add(val));
    }

    public Colour mul(double f) {
        return new Colour(this.v.mul(f));
    }

    public Colour mul(Colour other) {
        return new Colour(this.v.mul(other.v));
    }

    public Colour pow(double e) {
        return new Colour(this.v.pow(e));
    }

    public Colour inv() {
        return new Colour(this.v.inv());
    }

    private static int to_byte(double x) {
        return (int)(255.0 * Math.max(0, Math.min(1, x)));
    }

    public int to_rgb() {
        return to_byte(r) << 16 | to_byte(g) << 8 | to_byte(b);
    }

    public String toString() {
        return String.format("%f %f %f", r, g, b);
    }

}
