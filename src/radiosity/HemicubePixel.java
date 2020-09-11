package src.radiosity;

import src.math.Vec3;

class HemicubePixel {
    private Vec3 pos;
    private double form_factor;

    public HemicubePixel(Vec3 pos, double form_factor) {
        this.pos = pos;
        this.form_factor = form_factor;
    }

    public Vec3 get_pos() {
        return pos;
    }

    public double get_form_factor() {
        return form_factor;
    }
}
