package src.scene;

import java.util.List;
import java.util.ArrayList;
import src.raytracer.Ray;
import src.raytracer.RaycastHit;


public class Scene {
    private List<SceneObject> objects;
    private List<PointLight> lights;
    private Colour ambient_lighting = new Colour(0.02);;
    private double ambient_intensity = 1.0;

    public Scene(List<SceneObject> objects, List<PointLight> lights) {
        //we should probably do a deep copy here...
        //will implement later
        this.objects = objects;
        this.lights = lights;
    }

    public Scene() {
        this.objects = new ArrayList<>();
        this.lights = new ArrayList<>();
    }

    public void add_object(SceneObject obj) {
        objects.add(obj);
    }

    public void add_light(PointLight light) {
        lights.add(light);
    }

    public void set_ambient_light(Colour light, double intensity) {
        this.ambient_lightin = light;
        this.ambient_intensity = intensity;
    }


    public RaycastHit find_intersection(Ray ray) {
        RaycastHit closest_hit = new RaycastHit();
        RaycastHit rh;

        for (SceneObject obj : objects) {
            rh = obj.intersection_with(ray);
            if (rh.get_distance() < closest_hit.get_distance()) {
                closest_hit = rh;
            }
        }

        return closest_hit;

    }

    public Colour get_ambient_lighting() {
        return ambient_lighting;
    }

    public List<PointLight> get_point_lights() {
        return lights;
    }
}
