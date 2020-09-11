package src.radiosity;

import src.scene.SceneObject;
import src.scene.Colour;
import src.model.Triangle;
import src.raytracer.Camera;
import src.raytracer.Ray;
import src.raytracer.RaycastHit;
import src.math.Vec3;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.awt.image.BufferedImage;

public class RadiosityEngine {
    private int width;
    private int height;
    private static int HEMICUBE_LENGTH = 16;
    private static double CONVERGENCE_EPSILON = 0.0001;
    private static double EPSILON = 0.00001;
    private static Colour BACKGROUND_COLOUR = new Colour(0.0);
    List<Triangle> patches;
    //indices correspond to indices in patches
    Map<Triangle, Map<Triangle, Double>> form_factors;

    //brightness, emissiveness, and reflectivity for each rgb wavelength of each patch
    //i know how unbelievably horrible this is, but i just really needed this working
    Map<Triangle, Double> B_r;
    Map<Triangle, Double> E_r;
    Map<Triangle, Double> R_r;

    Map<Triangle, Double> B_g;
    Map<Triangle, Double> E_g;
    Map<Triangle, Double> R_g;
 
    Map<Triangle, Double> B_b;
    Map<Triangle, Double> E_b;
    Map<Triangle, Double> R_b;

    //need to pass in models/lights separately for emmissivness
    public RadiosityEngine(List<SceneObject> objects, List<SceneObject> lights, int width, int height) {
        this.width = width;
        this.height = height;

        patches = new ArrayList<>();
        form_factors = new HashMap<>();
        B_r = new HashMap<>();
        E_r = new HashMap<>();
        R_r = new HashMap<>();

        B_g = new HashMap<>();
        E_g = new HashMap<>();
        R_g = new HashMap<>();

        B_b = new HashMap<>();
        E_b = new HashMap<>();
        R_b = new HashMap<>();

        for (SceneObject obj : objects) {
            patches.addAll(obj.get_model_triangles());
        }

        for (SceneObject light : lights) {
            patches.addAll(light.get_model_triangles());
        }

        //initialize formfactors to all 0
        for (Triangle first_patch : patches) {
            form_factors.put(first_patch, new HashMap<>());
            for (Triangle second_patch : patches) {
                form_factors.get(first_patch).put(second_patch, 0.0);
            }
        }

        // initialize brightnesses
        for (Triangle patch : patches) {
            B_r.put(patch, 0.0);
            B_g.put(patch, 0.0);
            B_b.put(patch, 0.0);
        }

        // set emmisivinesses of regular patches to 0
        for (SceneObject obj : objects) {
            for (Triangle patch : obj.get_model_triangles()) {
                E_r.put(patch, 0.0);
                E_g.put(patch, 0.0);
                E_b.put(patch, 0.0);
            }
        }

        // set emmisivinesses of lights to colour of light
        for (SceneObject light : lights) {
            for (Triangle patch : light.get_model_triangles()) {
                E_r.put(patch, light.get_object_colour().r);
                E_g.put(patch, light.get_object_colour().g);
                E_b.put(patch, light.get_object_colour().b);
            }
        }

        //set reflectiveness of objects and lights to their colour
        for (SceneObject obj : objects) {
            for (Triangle patch : obj.get_model_triangles()) {
                R_r.put(patch, obj.get_object_colour().r);
                R_g.put(patch, obj.get_object_colour().g);
                R_b.put(patch, obj.get_object_colour().b);
            }
        }
        for (SceneObject light : lights) {
            for (Triangle patch : light.get_model_triangles()) {
                R_r.put(patch, light.get_object_colour().r);
                R_g.put(patch, light.get_object_colour().g);
                R_b.put(patch, light.get_object_colour().b);
            }
        }
    }

    public void compute_form_factors() {
        //Hemicube method

        int patches_done = 0;
        for (Triangle patch : patches) {
            Hemicube hemicube = new Hemicube(HEMICUBE_LENGTH, patch.get_centroid(), patch.get_norm(), patch.get_v1().sub(patch.get_v2()));
            List<HemicubePixel> pixels = hemicube.get_pixels();


            for (HemicubePixel pixel : pixels) {
                Vec3 dir = pixel.get_pos().sub(patch.get_centroid());
                Vec3 start = patch.get_centroid().add(dir.mul(EPSILON));
                Ray ray_through_pixel = new Ray(start, dir);
                Triangle patch_hit = get_triangle_hit(ray_through_pixel);
                if (patch_hit == null) {
                    continue;
                }

                double original_form_factor = form_factors.get(patch).get(patch_hit);
                form_factors.get(patch).put(patch_hit, original_form_factor + pixel.get_form_factor());
            }
            patches_done++;

            if (patches_done % 10 == 0) {
                System.out.println(String.format("patches computed: %d/%d", patches_done, patches.size()));
            }
        }

        


    }

    public void compute_radiosity() {
        //gauss-seidel
        Map<Triangle, Map<Triangle, Double>> M_r = new HashMap<>();;
        Map<Triangle, Map<Triangle, Double>> M_g = new HashMap<>();;
        Map<Triangle, Map<Triangle, Double>> M_b = new HashMap<>();;

        //set up M, and initalize B
        for (Triangle first_patch : patches) {
            M_r.put(first_patch, new HashMap<>());
            M_g.put(first_patch, new HashMap<>());
            M_b.put(first_patch, new HashMap<>());
            for (Triangle second_patch : patches) {
                M_r.get(first_patch).put(second_patch, R_r.get(first_patch) * form_factors.get(first_patch).get(second_patch));
                M_g.get(first_patch).put(second_patch, R_g.get(first_patch) * form_factors.get(first_patch).get(second_patch));
                M_b.get(first_patch).put(second_patch, R_b.get(first_patch) * form_factors.get(first_patch).get(second_patch));
            }

            B_r.put(first_patch, E_r.get(first_patch));
            B_g.put(first_patch, E_g.get(first_patch));
            B_b.put(first_patch, E_b.get(first_patch));
        }

        double max_diff = Double.POSITIVE_INFINITY;
        int bounces = 0;

        while (max_diff > CONVERGENCE_EPSILON) {
            max_diff = 0.0;
            for (Triangle first_patch : patches) {
                double old_B_r = B_r.get(first_patch);
                double old_B_g = B_g.get(first_patch);
                double old_B_b = B_b.get(first_patch);

                double s_r = E_r.get(first_patch);
                double s_g = E_g.get(first_patch);
                double s_b = E_b.get(first_patch);

                for (Triangle second_patch : patches) {
                    s_r += M_r.get(first_patch).get(second_patch) * B_r.get(second_patch);
                    s_g += M_g.get(first_patch).get(second_patch) * B_g.get(second_patch);
                    s_b += M_b.get(first_patch).get(second_patch) * B_b.get(second_patch);
                }

                B_r.put(first_patch, s_r);
                B_g.put(first_patch, s_g);
                B_b.put(first_patch, s_b);

                max_diff = Math.max(max_diff, s_r - old_B_r);
                max_diff = Math.max(max_diff, s_g - old_B_g);
                max_diff = Math.max(max_diff, s_b - old_B_b);
            }
            bounces++;
            System.out.println(String.format("Bounces: %d", bounces));
        }


    }

    public BufferedImage render() {
        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Camera camera = new Camera(width, height);

        for (int yy=0; yy < height; yy++) {
            for (int xx=0; xx < width; xx++) {
                Ray ray = camera.cast_ray(xx, yy);
                Colour linear_colour = trace(ray);
                Colour corrected_colour = tonemap(linear_colour);
                im.setRGB(xx, yy, corrected_colour.to_rgb());
            }
            if (yy % 10 == 0) {
                System.out.println(yy);
            }
        }
        return im;
        
    }

    private Triangle get_triangle_hit(Ray ray) {
        RaycastHit closest_hit = new RaycastHit();
        RaycastHit rh;
        for (Triangle patch : patches) {
            rh = patch.intersection_with(ray);
            if (rh.get_distance() < closest_hit.get_distance()) {
                closest_hit = rh;
            }
        }
        return closest_hit.get_triangle_hit();
    }

    private Colour trace(Ray ray) {
        Triangle triangle_hit = get_triangle_hit(ray);
        if (triangle_hit == null) {
            return BACKGROUND_COLOUR;
        }
        return new Colour(B_r.get(triangle_hit),
                          B_g.get(triangle_hit),
                          B_b.get(triangle_hit));
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
