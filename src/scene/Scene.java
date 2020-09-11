package src.scene;

import java.util.List;
import java.util.ArrayList;
import src.raytracer.Ray;
import src.raytracer.RaycastHit;

public class Scene {
    private List<SceneObject> objects;
    private List<PointLight> lights;
    private Colour ambient_lighting = new Colour(0.02);;

    public Scene(List<SceneObject> objects, List<PointLight> lights) {
        //we should probably do a deep copy here...
        //will implement later
        this.objects = objects;
        this.lights = lights;
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
