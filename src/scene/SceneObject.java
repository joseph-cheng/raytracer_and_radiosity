package src.scene;

import src.model.Model;
import src.model.Triangle;
import src.math.Mat4;
import src.math.Vec3;
import src.math.Vec4;
import src.raytracer.Ray;
import src.raytracer.RaycastHit;

import java.util.List;

public class SceneObject {
    private static double DEFAULT_KD = 0.8;
    private static double DEFAULT_KS = 1.2;
    private static double DEFAULT_ALPHA = 10;
    private static double DEFAULT_REFLECTIVITY = 0.1;

    private Model model;
    private Colour object_colour;
    private double K_d;
    private double K_s;
    private double alpha;
    private double reflectivity;

    private Mat4 scale;

    private Mat4 rotate;

    private Mat4 translate;

    public SceneObject(Model model, Colour object_colour) {
        this.model = model;
        this.object_colour = object_colour;
        this.K_d = DEFAULT_KD;
        this.K_s = DEFAULT_KS;
        this.alpha = DEFAULT_ALPHA;
        this.reflectivity = DEFAULT_REFLECTIVITY;

        scale = new Mat4();
        rotate = new Mat4();
        translate = new Mat4();
    }

    public SceneObject(Model model, Colour object_colour, double K_d, double K_s, double alpha, double reflectivity) {
        this.model = model;
        this.object_colour = object_colour;
        this.K_d = K_d;
        this.K_s = K_s;
        this.alpha = alpha;
        this.reflectivity = reflectivity;

        scale = new Mat4();
        rotate = new Mat4();
        translate = new Mat4();
    }

    public void set_scaling(Vec3 scale_vec) {
        scale = Mat4.scale_matrix(scale_vec);
    }

    public void set_scaling(double f) {
        scale = Mat4.scale_matrix(new Vec3(f, f, f));
    }

    public void set_rotation(double angle, Vec3 axis) {
        rotate = Mat4.rotation_matrix(angle, axis);
    }

    public void set_translation(Vec3 translation) {
        translate = Mat4.translation_matrix(translation);
    }

    public RaycastHit intersection_with(Ray ray) {

        RaycastHit rh = model.intersection_with(ray);
        
        //add scene_object_hit
        rh = new RaycastHit(this,
                            rh.get_triangle_hit(),
                            rh.get_distance(),
                            rh.get_location(),
                            rh.get_norm());

        return rh;
    }

    public void transform_model() {
        this.model.transform_model(translate, rotate, scale);
    }

    public Colour get_object_colour() {
        return object_colour;
    }

    public List<Triangle> get_model_triangles() {
        return model.get_triangles();
    }

    public double get_k_d() {
        return K_d;
    }

    public double get_k_s() {
        return K_s;
    }

    public double get_alpha() {
        return alpha;
    }

    public double get_reflectivity() {
        return reflectivity;
    }


}
