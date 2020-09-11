package src.model;

import src.math.Vec3;
import src.math.Vec4;
import src.math.Mat4;
import src.raytracer.Ray;
import src.raytracer.RaycastHit;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Model {

    private List<Triangle> triangles;

    public Model(String file_loc) throws IOException, FileNotFoundException {
        if (file_loc.length() < 4 ||
            !file_loc.substring(file_loc.length() - 4).equals(".stl")) {

            throw new IllegalArgumentException("File is not an .stl file");

        }
        this.triangles = new ArrayList<>();

        File model_file = new File(file_loc);
        FileInputStream fin = new FileInputStream(model_file);

        //skip header
        fin.skip(80);

        byte[] buf = new byte[4];
        fin.read(buf);
        int num_triangles = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getInt();

        byte[] triangle_bytes = new byte[48];
        for (int ii=0; ii < num_triangles; ii++) {
            fin.read(triangle_bytes);
            FloatBuffer fb = ByteBuffer.wrap(triangle_bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();

            Vec3 norm_vec = new Vec3(fb.get(), fb.get(), fb.get());
            Vec3 v1 = new Vec3(fb.get(), fb.get(), fb.get());
            Vec3 v2 = new Vec3(fb.get(), fb.get(), fb.get());
            Vec3 v3 = new Vec3(fb.get(), fb.get(), fb.get());

            this.triangles.add(new Triangle(norm_vec, v1, v2, v3));

            //skip attribute byte count
            fin.skip(2);
        }
    }

    public RaycastHit intersection_with(Ray ray) {
        RaycastHit closest_intersection = new RaycastHit();

        for (Triangle triangle : triangles) {
            RaycastHit rh = triangle.intersection_with(ray);
            if (rh.get_distance() < closest_intersection.get_distance()) {
                closest_intersection = rh;
            }
        }

        return closest_intersection;
    }

    public void transform_model(Mat4 translate, Mat4 rotate, Mat4 scale) {
        ArrayList<Triangle> new_triangles = new ArrayList<>();
        Mat4 scale_inverse = scale.inverse();

        for (Triangle triangle : this.triangles) {
            new_triangles.add(new Triangle(
                        new Vec3(rotate.mul(scale_inverse.mul(new Vec4(triangle.get_norm())))),
                        new Vec3(translate.mul(rotate.mul(scale.mul(new Vec4(triangle.get_v1()))))),
                        new Vec3(translate.mul(rotate.mul(scale.mul(new Vec4(triangle.get_v2()))))),
                        new Vec3(translate.mul(rotate.mul(scale.mul(new Vec4(triangle.get_v3())))))
                        ));
        }

        this.triangles = new_triangles;
    }

    public void invert_normals() {
        ArrayList<Triangle> new_triangles = new ArrayList<>();

        for (Triangle triangle : this.triangles) {
            new_triangles.add(new Triangle(
                        triangle.get_norm().mul(-1),
                        triangle.get_v1(),
                        triangle.get_v2(),
                        triangle.get_v3()));
        }

        this.triangles = new_triangles;
    }

    public List<Triangle> get_triangles() {
        return this.triangles;
    }

};
