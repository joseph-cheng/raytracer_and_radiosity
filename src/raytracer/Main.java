package src.raytracer;

import src.scene.Scene;
import src.scene.SceneLoader;

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

        String scene_file;
        String output_file = "out.bmp";

        if (args.length == 1) {
            scene_file = args[0];
        }
        else if (args.length == 2) {
            scene_file = args[0];
            output_file = args[1];
        }
        else {
            System.out.println("Error: invalid arguments\nUsage: java src.raytracer.Main [scene_file.xml] [out_file.bmp (default out.bmp)]");
            return;

        }

        System.out.println("Loading scene from " + scene_file + "...");
        SceneLoader scene_loader = new SceneLoader(scene_file);
        Scene scene = scene_loader.get_scene();

        BufferedImage im = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        ExecutorService es = Executors.newCachedThreadPool();

        System.out.println("Rendering...");

        for (int line=0; line < SCREEN_HEIGHT; line += SCREEN_HEIGHT/NUM_THREADS) {
            Raytracer renderer = new Raytracer(SCREEN_WIDTH, SCREEN_HEIGHT, NUM_BOUNCES, scene, im, line, SCREEN_HEIGHT/NUM_THREADS);
            es.execute(renderer);
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("Writing to " + output_file + "...");

        File image_file = new File(output_file);
        ImageIO.write(im, "bmp", image_file);
        System.out.println("Finished");
    }
}
