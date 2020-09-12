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
        //should probably move this scene creation into Scene and load from xml or smth...
        //will implement later

        SceneLoader scene_loader = new SceneLoader("test_scene.xml");
        Scene scene = scene_loader.get_scene();

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
