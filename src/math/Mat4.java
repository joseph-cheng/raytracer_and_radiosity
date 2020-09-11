package src.math;

public class Mat4 {
    private double[][] elements;

    public Mat4() {
        elements = new double[4][4];
        for (int ii=0; ii < 4; ii++) {
            elements[ii][ii] = 1.0f;
        }
    }

    public Mat4 mul(Mat4 other) {
        Mat4 result = new Mat4();
        for (int ii=0; ii < 4; ii++) {
            for (int jj=0; jj < 4; jj++) {
                double s = 0;
                for (int kk=0; kk < 4; kk++) {
                    s += this.elements[ii][kk] * other.elements[kk][jj];
                }
                result.elements[ii][jj] = s;
            }
        }
        return result;
    }

    public Vec4 mul(Vec4 vec) {
        double[] result = new double[4];
        for (int ii=0; ii < 4; ii++) {
            result[ii] += elements[ii][0] * vec.get_x();
            result[ii] += elements[ii][1] * vec.get_y();
            result[ii] += elements[ii][2] * vec.get_z();
            result[ii] += elements[ii][3] * vec.get_w();
        }
        return new Vec4(result[0], result[1], result[2], result[3]);

    }

    public Mat4 inverse() {
        double det;
        Mat4 inv = new Mat4();

        double[] m = new double[16];
        for (int ii=0; ii < 4; ii++) {
            for (int jj=0; jj < 4; jj++) {
                m[ii * 4 + jj] = elements[ii][jj];
            }
        }

        inv.elements[0][0] = m[5]  * m[10] * m[15] - 
                             m[5]  * m[11] * m[14] - 
                             m[9]  * m[6]  * m[15] + 
                             m[9]  * m[7]  * m[14] +
                             m[13] * m[6]  * m[11] - 
                             m[13] * m[7]  * m[10];

        inv.elements[1][0] = -m[4]  * m[10] * m[15] + 
                              m[4]  * m[11] * m[14] + 
                              m[8]  * m[6]  * m[15] - 
                              m[8]  * m[7]  * m[14] - 
                              m[12] * m[6]  * m[11] + 
                              m[12] * m[7]  * m[10];

        inv.elements[2][0] = m[4]  * m[9] * m[15] - 
                             m[4]  * m[11] * m[13] - 
                             m[8]  * m[5] * m[15] + 
                             m[8]  * m[7] * m[13] + 
                             m[12] * m[5] * m[11] - 
                             m[12] * m[7] * m[9];

        inv.elements[3][0] = -m[4]  * m[9] * m[14] + 
                              m[4]  * m[10] * m[13] +
                              m[8]  * m[5] * m[14] - 
                              m[8]  * m[6] * m[13] - 
                              m[12] * m[5] * m[10] + 
                              m[12] * m[6] * m[9];
   
        inv.elements[0][1] = -m[1]  * m[10] * m[15] + 
                              m[1]  * m[11] * m[14] + 
                              m[9]  * m[2] * m[15] - 
                              m[9]  * m[3] * m[14] - 
                              m[13] * m[2] * m[11] + 
                              m[13] * m[3] * m[10];

        inv.elements[1][1] = m[0]  * m[10] * m[15] - 
                             m[0]  * m[11] * m[14] - 
                             m[8]  * m[2] * m[15] + 
                             m[8]  * m[3] * m[14] + 
                             m[12] * m[2] * m[11] - 
                             m[12] * m[3] * m[10];

        inv.elements[2][1] = -m[0]  * m[9] * m[15] + 
                              m[0]  * m[11] * m[13] + 
                              m[8]  * m[1] * m[15] - 
                              m[8]  * m[3] * m[13] - 
                              m[12] * m[1] * m[11] + 
                              m[12] * m[3] * m[9];

        inv.elements[3][1] = m[0]  * m[9] * m[14] - 
                             m[0]  * m[10] * m[13] - 
                             m[8]  * m[1] * m[14] + 
                             m[8]  * m[2] * m[13] + 
                             m[12] * m[1] * m[10] - 
                             m[12] * m[2] * m[9];

        inv.elements[0][2] = m[1]  * m[6] * m[15] - 
                             m[1]  * m[7] * m[14] - 
                             m[5]  * m[2] * m[15] + 
                             m[5]  * m[3] * m[14] + 
                             m[13] * m[2] * m[7] - 
                             m[13] * m[3] * m[6];

        inv.elements[1][2] = -m[0]  * m[6] * m[15] + 
                              m[0]  * m[7] * m[14] + 
                              m[4]  * m[2] * m[15] - 
                              m[4]  * m[3] * m[14] - 
                              m[12] * m[2] * m[7] + 
                              m[12] * m[3] * m[6];

        inv.elements[2][2] = m[0]  * m[5] * m[15] - 
                             m[0]  * m[7] * m[13] - 
                             m[4]  * m[1] * m[15] + 
                             m[4]  * m[3] * m[13] + 
                             m[12] * m[1] * m[7] - 
                             m[12] * m[3] * m[5];

        inv.elements[3][2] = -m[0]  * m[5] * m[14] + 
                              m[0]  * m[6] * m[13] + 
                              m[4]  * m[1] * m[14] - 
                              m[4]  * m[2] * m[13] - 
                              m[12] * m[1] * m[6] + 
                              m[12] * m[2] * m[5];

        inv.elements[0][3] = -m[1] * m[6] * m[11] + 
                              m[1] * m[7] * m[10] + 
                              m[5] * m[2] * m[11] - 
                              m[5] * m[3] * m[10] - 
                              m[9] * m[2] * m[7] + 
                              m[9] * m[3] * m[6];

        inv.elements[1][3] = m[0] * m[6] * m[11] - 
                             m[0] * m[7] * m[10] - 
                             m[4] * m[2] * m[11] + 
                             m[4] * m[3] * m[10] + 
                             m[8] * m[2] * m[7] - 
                             m[8] * m[3] * m[6];

        inv.elements[2][3] = -m[0] * m[5] * m[11] + 
                              m[0] * m[7] * m[9] + 
                              m[4] * m[1] * m[11] - 
                              m[4] * m[3] * m[9] - 
                              m[8] * m[1] * m[7] + 
                              m[8] * m[3] * m[5];

        inv.elements[3][3] = m[0] * m[5] * m[10] - 
                             m[0] * m[6] * m[9] - 
                             m[4] * m[1] * m[10] + 
                             m[4] * m[2] * m[9] + 
                             m[8] * m[1] * m[6] - 
                             m[8] * m[2] * m[5];

        det = m[0] * inv.elements[0][0] + m[1] * inv.elements[1][0] + m[2] * inv.elements[2][0] + m[3] * inv.elements[3][0];

        if (det == 0.0f) {
            return null;
        }

        det = 1.0f / det;

        return inv.mul(det);
    }

    public Mat4 mul(double f) {
        Mat4 result = new Mat4();
        for (int ii=0; ii < 4; ii++) {
            for (int jj=0; jj < 4; jj++) {
                result.elements[ii][jj] = this.elements[ii][jj] * f;
            }
        }
        return result;
    }

    public static Mat4 scale_matrix(Vec3 scale) {
        Mat4 result = new Mat4();
        result.elements[0][0] = scale.get_x();
        result.elements[1][1] = scale.get_y();
        result.elements[2][2] = scale.get_z();
        result.elements[3][3] = 1.0f;
        return result;
    }

    public static Mat4 rotation_matrix(double angle, Vec3 axis) {
        Mat4 result = new Mat4();
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Vec3 temp = axis.mul(1-c);

        result.elements[0][0] = c + temp.get_x() * axis.get_x();
        result.elements[0][1] =     temp.get_x() * axis.get_y() + s * axis.get_z();
        result.elements[0][2] =     temp.get_x() * axis.get_z() - s * axis.get_y();

        result.elements[1][0] =     temp.get_y() * axis.get_x() - s * axis.get_z();
        result.elements[1][1] = c + temp.get_y() * axis.get_y();
        result.elements[1][2] =     temp.get_y() * axis.get_z() + s * axis.get_x();

        result.elements[2][0] =     temp.get_z() * axis.get_x() + s * axis.get_y();
        result.elements[2][1] =     temp.get_z() * axis.get_y() - s * axis.get_x();
        result.elements[2][2] = c + temp.get_z() * axis.get_z();

        result.elements[3][3] = 1.0f;

        return result;
    }

    public static Mat4 translation_matrix(Vec3 translation) {
        Mat4 result = new Mat4();
        result.elements[0][3] = translation.get_x();
        result.elements[1][3] = translation.get_y();
        result.elements[2][3] = translation.get_z();
        return result;
    }

}

