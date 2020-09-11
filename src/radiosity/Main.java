package src.radiosity;

import src.model.Model;
import src.scene.SceneObject;
import src.scene.Colour;
import src.math.Vec3;

import java.util.ArrayList;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.awt.image.BufferedImage;

public class Main {
    public static int SCREEN_WIDTH = 1920;
    public static int SCREEN_HEIGHT = 1080;

    public static void main(String[] args) throws IOException, FileNotFoundException {
        //consequence of transforming models instead of rays means
        //we need to load in the same model multiple times
        //not ideal... maybe try and get shadow rays working with
        //transforming them
        Model first_torus_model = new Model("assets/torus.stl");
        Model second_torus_model = new Model("assets/torus.stl");

        Model first_light_model = new Model("assets/cube.stl");
        Model second_light_model = new Model("assets/cube.stl");


        SceneObject first_torus = new SceneObject(first_torus_model, new Colour(0.2, 0.2, 0.8));
        first_torus.set_scaling(0.3);
        first_torus.set_rotation(-0.2, new Vec3(0.0, 1.0, 0.1).norm());
        first_torus.set_translation(new Vec3(0.0, -0.1, 1.0));
        first_torus.transform_model();

        SceneObject second_torus = new SceneObject(second_torus_model, new Colour(0.8, 0.2, 0.2));
        second_torus.set_scaling(0.3);
        second_torus.set_rotation(-1.0, new Vec3(1.0, 0.0, 0.1).norm());
        second_torus.set_translation(new Vec3(0.3, 0.0, 1.0));
        second_torus.transform_model();

        SceneObject first_light = new SceneObject(first_light_model, new Colour(1.0, 1.0, 1.0));
        first_light.set_translation(new Vec3(2.0, 2.0, 0.0));
        first_light.transform_model();

        SceneObject second_light = new SceneObject(second_light_model, new Colour(1.0, 1.0, 1.0));
        second_light.set_translation(new Vec3(0.0, 3.0, 0.0));
        second_light.transform_model();

        ArrayList<SceneObject> objs = new ArrayList<>();
        objs.add(first_torus);
        objs.add(second_torus);

        ArrayList<SceneObject> lights = new ArrayList<>();
        lights.add(first_light);
        lights.add(second_light);

        RadiosityEngine renderer = new RadiosityEngine(objs, lights, SCREEN_WIDTH, SCREEN_HEIGHT);
        System.out.println("Computing form factors...");
        renderer.compute_form_factors();
        System.out.println("Computing radiosity...");
        renderer.compute_radiosity();
        System.out.println("Rendering...");
        BufferedImage out = renderer.render();

        File image_file = new File("radiosity_out.bmp");
        ImageIO.write(out, "bmp", image_file);
    }
}
