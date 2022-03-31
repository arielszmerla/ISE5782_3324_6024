package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.*;

public class Camera {

    private Vector _vRight;
    private Vector _vTo;
    private Vector _vUp;
    private Point _p0;
    private double _distance;
    private int _width;
    private int _height;

    public Camera(Point p0, Vector vto, Vector vup) {
        if (!isZero(vto.dotProduct(vup))) {
            throw new IllegalArgumentException("vup and vto aren't orthogonal");
        }
        _p0 = p0;
        _vTo = vto.normalize();
        _vUp = vup.normalize();
        _vRight = _vTo.crossProduct(_vUp);
    }

    public Camera setVPDistance(double distance) {
        _distance = distance;
        return this;
    }

    public Camera setVPSize(int width, int height) {
        _width = width;
        _height = height;
        return this;

    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        // Ratio (pixel width & height)
        double Ry = (double) _height / nY;
        double Rx = (double) _width / nX;

        // Image center
        Point Pc = _p0.add(_vTo.scale(_distance));

        Point Pij = Pc;

        double yI = -(i - ((nY - 1) / 2d)) * Ry;
        double xJ = (j - ((nX - 1) / 2d)) * Rx;

        if (xJ != 0)
            Pij = Pij.add(_vRight.scale(xJ));
        if (yI != 0)
            Pij = Pij.add(_vUp.scale(yI));

        return new Ray(_p0, Pij.substract(_p0));
    }


    /***
     * Rotate camera through axis and angle of rotation
     * @param axis Axis of rotation
     * @param theta Angle of rotation (degrees)
     */
    public void rotateCamera(Vector axis, double theta) {
        //rotate all vector's using Vector.rotateVector Method
        if (theta == 0) return; //no rotation
        _vUp = _vUp.rotateVector(axis, theta);
        _vRight= _vRight.rotateVector(axis, theta);
        _vTo = _vTo.rotateVector(axis, theta);
    }

    /***
     * Move camera (move point of view of the camera)
     * @param up Vertical distance
     * @param right Horizontal side distance
     * @param to Horizontal to distance
     */
    public void moveCamera(double up, double right, double to) {
        //move Point0 according to params
        Point myPoint = new Point(_p0.get_xyz());
        if (up == 0 && right == 0 && to == 0) return; //don't create Vector.Zero
        if (up != 0) myPoint = myPoint.add(_vUp.scale(up));
        if (right != 0) myPoint = myPoint.add(_vRight.scale(right));
        if (to != 0) myPoint = myPoint.add(_vTo.scale(to));
        this._p0 = myPoint;
    }

    public Camera setImageWriter(ImageWriter imageWriter) {
        this.setImageWriter(imageWriter);
        return this;
    }

    public Camera setRayTracer(RayTracer rayTracerBasic) {
        this.setRayTracer(rayTracerBasic);
        return this;
    }

    public void writeToImage() {
        imagewriter.writeToImage();
    }
    public void printGrid(int interval, Color color){
        for (int i = 0; i < imageWriter.getNx(); i++)
            for (int j = 0; j < imageWriter.getNy(); j++)
                if (i % 50 == 0 || j % 50 == 0)
                    imageWriter.writePixel(i, j, redColor);
                else
                    imageWriter.writePixel(i, j, yellowColor);
        imageWriter.writeToImage();
    }
}