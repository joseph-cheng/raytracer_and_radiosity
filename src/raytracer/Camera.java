package src.raytracer;

import src.math.Vec3;

public class Camera {
    //screen size
    private int width_px;
    private int height_px;

    private double aspect_ratio;

    //world size
    private double width_m;
    private double height_m;

    //distance in world between adjacent pixels
    private double x_step_m;
    private double y_step_m;

    //half fov in degrees
    private double fov = 45;

    public Camera(int w, int h) {
        this.width_px = w;
        this.height_px = h;

        this.aspect_ratio = ((double)width_px) / ((double)height_px);

        this.width_m = 2.0 * Math.tan(Math.toRadians(this.fov));
        this.height_m = this.width_m / this.aspect_ratio;

        this.x_step_m = this.width_m / ((double)this.width_px);
        this.y_step_m = this.height_m / ((double)this.height_px);
    }

    public Ray cast_ray(int x, int y) {
        double x_pos = (x_step_m - width_m) / 2.0 + x * x_step_m;
        double y_pos = (y_step_m + height_m) / 2.0 - y * y_step_m;
        return new Ray(new Vec3(), new Vec3(x_pos, y_pos, 1).norm());
    }
}
