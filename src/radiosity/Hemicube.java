package src.radiosity;

import src.math.Vec3;
import java.util.List;
import java.util.ArrayList;

public class Hemicube {
    private static double HEMICUBE_SIZE = 1.0;
    //number of pixels in one HEMICUBE_SIZE
    private int length_px;
    private double pixel_stride;
    private double pixel_area;

    private Vec3 centre;
    private Vec3 normal;
    private Vec3 down;
    private Vec3 left;

    public Hemicube(int length_px, Vec3 centre, Vec3 normal, Vec3 down) {
        this.length_px = length_px;
        this.pixel_stride = HEMICUBE_SIZE / ((double)length_px);
        this.pixel_area = this.pixel_stride * this.pixel_stride;

        this.centre = centre;
        this.normal = normal.norm();
        this.down = down.norm(); // any vector planar to the patch
        this.left = this.normal.cross(this.down);
    }

    public List<HemicubePixel> get_pixels() {
        List<HemicubePixel> pixels = new ArrayList<>();
        Vec3 bottom_left = centre.add(normal).add(left.mul(pixel_stride * ((double)length_px - 0.5))).add(down.mul(pixel_stride * ((double)length_px - 0.5)));
        Vec3 top_right = centre.add(normal).sub(left.mul(pixel_stride * ((double)length_px - 0.5))).sub(down.mul(pixel_stride * ((double)length_px - 0.5)));

        //top face
        for (int ii=0; ii < length_px * 2; ii++) {
            for (int jj=0; jj < length_px * 2; jj++) {
                Vec3 px_pos = bottom_left.sub(left.mul(pixel_stride * ii)).sub(down.mul(pixel_stride * jj));

                double x = ((double)(ii - length_px) - 0.5) * pixel_stride;
                double y = ((double)(jj - length_px) - 0.5) * pixel_stride;
                double d_sqrd = x*x + y*y + 1;
                pixels.add(new HemicubePixel(px_pos, pixel_area/(Math.PI * d_sqrd * d_sqrd)));
            }
        }

        //left face
        for (int ii=0; ii < length_px * 2; ii++) {
            for (int jj=0; jj < length_px; jj++) {
                Vec3 px_pos = bottom_left.sub(down.mul(pixel_stride * ii)).sub(normal.mul(pixel_stride * jj));

                double y = ((double)(ii - length_px) - 0.5) * pixel_stride;
                double z = ((double)(length_px - jj) - 0.5) * pixel_stride;
                double d_sqrd = y*y + z*z + 1;
                pixels.add(new HemicubePixel(px_pos, z * pixel_area/(Math.PI * d_sqrd * d_sqrd)));
            }
        }

        //back face
        for (int ii=0; ii < length_px * 2; ii++) {
            for (int jj=0; jj < length_px; jj++) {
                Vec3 px_pos = bottom_left.sub(left.mul(pixel_stride * ii)).sub(normal.mul(pixel_stride * jj));

                double y = ((double)(ii - length_px) - 0.5) * pixel_stride;
                double z = ((double)(length_px - jj) - 0.5) * pixel_stride;
                double d_sqrd = y*y + z*z + 1;
                pixels.add(new HemicubePixel(px_pos, z * pixel_area/(Math.PI * d_sqrd * d_sqrd)));
            }
        }

        //front face
        for (int ii=0; ii < length_px * 2; ii++) {
            for (int jj=0; jj < length_px; jj++) {
                Vec3 px_pos = top_right.add(left.mul(pixel_stride * ii)).sub(normal.mul(pixel_stride * jj));
                double y = ((double)(ii - length_px) - 0.5) * pixel_stride;
                double z = ((double)(length_px - jj) - 0.5) * pixel_stride;
                double d_sqrd = y*y + z*z + 1;
                pixels.add(new HemicubePixel(px_pos, z * pixel_area/(Math.PI * d_sqrd * d_sqrd)));
            }
        }

        //right face
        for (int ii=0; ii < length_px * 2; ii++) {
            for (int jj=0; jj < length_px; jj++) {
                Vec3 px_pos = top_right.add(down.mul(pixel_stride * ii)).sub(normal.mul(pixel_stride * jj));
                double y = ((double)(ii - length_px) - 0.5) * pixel_stride;
                double z = ((double)(length_px - jj) - 0.5) * pixel_stride;
                double d_sqrd = y*y + z*z + 1;
                pixels.add(new HemicubePixel(px_pos, z * pixel_area/(Math.PI * d_sqrd * d_sqrd)));
            }
        }

        return pixels;
    }
}
