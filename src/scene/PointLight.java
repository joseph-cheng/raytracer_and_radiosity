package src.scene;

import src.math.Vec3;


public class PointLight {
    private Vec3 pos;
    private Colour colour;
    private double intensity;

    public PointLight(Vec3 pos, Colour colour, double intensity) {
        this.pos = pos;
        this.colour = colour;
        this.intensity = intensity;
    }

    public Colour get_intensity_at(double distance) {
        return this.colour.mul(intensity / (Math.PI * 4 * Math.pow(distance, 2)));
    }

    public Vec3 get_pos() {
        return pos;
    }

    public Colour get_colour() {
        return colour;
    }

}
