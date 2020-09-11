package src.raytracer;

import src.math.Vec3;
import src.scene.Colour;
import src.scene.Scene;
import src.scene.PointLight;
import src.scene.SceneObject;
import java.awt.image.BufferedImage;
import java.util.List;

public class Raytracer implements Runnable{
    // for shadow rays
    private static double EPSILON = 0.00001;

    private int width;
    private int height;
    private int num_bounces;
    private int start_line;
    private int lines_to_draw;
    private static Colour BACKGROUND_COLOUR = new Colour(0.0);

    private Scene scene_to_render;
    private BufferedImage image_to_draw_to;

    public Raytracer(int width, int height, int num_bounces, Scene scene, BufferedImage out, int start_line, int lines_to_draw) {
        this.width = width;
        this.height = height;
        this.num_bounces = num_bounces;
        this.scene_to_render = scene;
        this.image_to_draw_to = out;
        this.start_line = start_line;
        this.lines_to_draw = lines_to_draw;
    }

    public void run() {

        Camera camera = new Camera(width, height);

        for (int yy=start_line; yy < start_line + lines_to_draw; yy++) {
            for (int xx=0; xx < width; xx++) {
                Ray ray = camera.cast_ray(xx, yy);
                Colour linear_colour = trace(ray, num_bounces);
                Colour corrected_colour = tonemap(linear_colour);
                this.image_to_draw_to.setRGB(xx, yy, corrected_colour.to_rgb());
            }
            System.out.println(yy);
        }


    }

    private Colour trace(Ray ray, int bounces_left) {
        RaycastHit closest_hit = scene_to_render.find_intersection(ray);
        SceneObject scene_object_hit = closest_hit.get_scene_object_hit();

        // ray did not hit any scene objects
        if (scene_object_hit == null) {
            return BACKGROUND_COLOUR;
        }

        Vec3 P = closest_hit.get_location();
        Vec3 N = closest_hit.get_norm();
        Vec3 O = ray.get_start();

        Colour direct_illumination = this.illuminate(scene_object_hit, P, N, O);

        double reflectivity = scene_object_hit.get_reflectivity();

        if (bounces_left == 0 || reflectivity == 0) {
            return direct_illumination;
        }
        else {
            Vec3 R = ray.get_dir().reflect_in(N.norm()).mul(-1.0).norm();

            Ray reflected_ray = new Ray(P.add(R.mul(EPSILON)), R);

            Colour reflected_illumination = this.trace(reflected_ray, bounces_left - 1);

            direct_illumination = direct_illumination.mul(1.0 - reflectivity);
            reflected_illumination = reflected_illumination.mul(reflectivity);
            return direct_illumination.add(reflected_illumination);
        }

    }

    private Colour illuminate(SceneObject obj, Vec3 P, Vec3 N, Vec3 O) {
        Colour colour_to_return = new Colour(0.0);

        Colour I_a = scene_to_render.get_ambient_lighting();
        Colour C_diff = obj.get_object_colour();

        double k_d = obj.get_k_d();
        double k_s = obj.get_k_s();
        double alpha = obj.get_alpha();

        colour_to_return = colour_to_return.add(C_diff.mul(I_a));

        List<PointLight> point_lights = scene_to_render.get_point_lights();
        for (PointLight light : point_lights) {
            double distance_to_light = light.get_pos().sub(P).mag();
            Colour C_spec = light.get_colour();
            Colour I = light.get_intensity_at(distance_to_light);

            Vec3 l = light.get_pos().sub(P).norm();
            Vec3 v = O.sub(P).norm();
            Vec3 r = l.reflect_in(N);

            Colour diffuse = I.mul(C_diff).mul(k_d).mul(Math.max(0.0, N.dot(l)));
            Colour spec = I.mul(C_spec).mul(k_s).mul(Math.pow(Math.max(0.0, r.dot(v)), alpha));


            Ray shadow_ray = new Ray(P.add(l.mul(EPSILON)), l);


            RaycastHit shadow_rh = scene_to_render.find_intersection(shadow_ray);


            if (shadow_rh.get_distance() > distance_to_light) {
                colour_to_return = colour_to_return.add(diffuse.add(spec));
            }
        }
        return colour_to_return;
    }

    private Colour tonemap(Colour linear_colour) {
        double inv_gamma = 1.0 / 2.2;
        double a = 2.0; // brightness
        double b = 1.3; // contrast

        Colour pow = linear_colour.pow(b);
        Colour display = pow.mul(pow.add(Math.pow(0.5/a, b)).inv());

        Colour gamma = display.pow(inv_gamma);

        return gamma;
    }



}
