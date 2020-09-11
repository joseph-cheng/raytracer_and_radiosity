package src.raytracer;

import src.scene.Scene;
import src.scene.SceneObject;
import src.scene.Colour;
import src.scene.PointLight;
import src.model.Model;
import src.math.Vec3;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static int SCREEN_WIDTH = 1920;
    public static int SCREEN_HEIGHT = 1080;
    public static int NUM_BOUNCES = 5;
    public static int NUM_THREADS = 12;

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {
        //should probably move this scene creation into Scene and load from xml or smth...
        //will implement later
        Model first_torus_model = new Model("assets/low_poly_torus.stl");
        Model second_torus_model = new Model("assets/low_poly_torus.stl");

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
        
        PointLight first_light = new PointLight(new Vec3(2.0, 2.0, 0.0), new Colour(1.0, 1.0, 1.0), 5.0);
        PointLight second_light = new PointLight(new Vec3(0.0, 3.0, 0.0), new Colour(1.0, 1.0, 1.0), 5.0);

        ArrayList<SceneObject> objs = new ArrayList<>();
        ArrayList<PointLight> lights = new ArrayList<>();
        objs.add(first_torus);
        objs.add(second_torus);
        lights.add(first_light);
        lights.add(second_light);

        Scene scene = new Scene(objs, lights);

        BufferedImage im = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        ExecutorService es = Executors.newCachedThreadPool();

        for (int line=0; line < SCREEN_HEIGHT; line += SCREEN_HEIGHT/NUM_THREADS) {
            Raytracer renderer = new Raytracer(SCREEN_WIDTH, SCREEN_HEIGHT, NUM_BOUNCES, scene, im, line, SCREEN_HEIGHT/NUM_THREADS);
            es.execute(renderer);
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.HOURS);

        File image_file = new File("out.bmp");
        ImageIO.write(im, "bmp", image_file);
    }
}
