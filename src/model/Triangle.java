package src.model;

import src.math.Vec3;

import src.raytracer.Ray;
import src.raytracer.RaycastHit;

public class Triangle {
    private Vec3 norm;
    private Vec3 v1;
    private Vec3 v2;
    private Vec3 v3;
    private Vec3 centroid;
    private double area;

    public Triangle(Vec3 norm, Vec3 v1, Vec3 v2, Vec3 v3) {
        this.norm = norm;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.centroid = v1.add(v2).add(v3).div(3.0);
        this.area = (v2.sub(v1)).cross(v3.sub(v1)).mag() * 0.5;
    }

    public RaycastHit intersection_with(Ray r) {

        //intersect ray with plane that triangle is in
        double a = v1.sub(r.get_start()).dot(norm);
        double b = r.get_dir().dot(norm);

        if (b == 0.0f) {
            return new RaycastHit();
        }

        double ray_param = a/b;

        if (point_in_triangle(r.get_pos_at(ray_param)) && ray_param >= 0.0f) {
            return new RaycastHit(this, ray_param / r.get_dir().mag(), r.get_pos_at(ray_param), this.norm);
        }

        return new RaycastHit();

    }

    private boolean point_in_triangle(Vec3 point) {
        Vec3 edge1 = v3.sub(v1);
        Vec3 edge2 = v2.sub(v1);
        Vec3 p = point.sub(v1);

        double a = edge2.dot(edge2) * p.dot(edge1) - edge2.dot(edge1) * p.dot(edge2);
        double b = edge1.dot(edge1) * p.dot(edge2) - edge1.dot(edge2) * p.dot(edge1);
        double c = edge1.dot(edge1) * edge2.dot(edge2) - edge1.dot(edge2) * edge2.dot(edge1);
        
        //compute barycentric coordinates of point
        double bary1 = a/c;
        double bary2 = b/c;

        if (bary1 < 0.0f || bary2 < 0.0f ||
            bary1 > 1.0f || bary2 > 1.0f ||
            bary1 + bary2 > 1.0f) {
            return false;
        }

        return true;
    }

    public Vec3 get_norm() {
        return norm;
    }

    public Vec3 get_v1() {
        return v1;
    }
    
    public Vec3 get_v2() {
        return v2;
    }

    public Vec3 get_v3() {
        return v3;
    }

    public Vec3 get_centroid() {
        return centroid;
    }

    public double get_area() {
        return area;
    }
}

