package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.*;

public class Camera {

    /**
     * right direction vector from camera
     */
    private Vector _vRight;
    /**
     * forward direction vector from camera
     */
    private Vector _vTo;
    /**
     * up direction vector from camera
     */
    private Vector _vUp;
    /**
     * Camera Position
     */
    private Point _p0;
    /**
     * distance from camera to the view pane
     */
    private double _distance;
    /**
     * width of view plane
     */
    private int _width;
    /**
     * height of view plane
     */
    private int _height;

    private ImageWriter _imageWriter;
    private RayTracer _rayTracer;

    /**
     * Camera constructor based-on point and 2 vectors
     * @param p0 {@link Point} position
     * @param vto {@link java.util.Vector}
     * @param vup {@link  java.util.Vector}
     */
    public Camera(Point p0, Vector vto, Vector vup) {
        if (!isZero(vto.dotProduct(vup))) {
            throw new IllegalArgumentException("vup and vto aren't orthogonal");
        }
        _p0 = p0;
        _vTo = vto.normalize();
        _vUp = vup.normalize();
        _vRight = _vTo.crossProduct(_vUp);
    }

    /**
     * set distance from camera to viewplane
     * @param distance
     * @return updated Camera {@link Camera}
     */
    public Camera setVPDistance(double distance) {
        _distance = distance;
        return this;
    }

    /**
     * set view plane size
     * @param width
     * @param height
     * @return updated Camera {@link Camera}
     */
    public Camera setVPSize(int width, int height) {
        _width = width;
        _height = height;
        return this;

    }

    public Ray constructRay(int nX, int nY, int j, int i) {
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


    /**
     * Rotate camera through axis and angle of rotation
     * @param axis Axis of rotation
     * @param theta Angle of rotation (degrees)
     */
    public void rotateCamera(Vector axis, double theta) {
        //rotate all vector's using Vector.rotateVector Method
        if (theta == 0) return; //no rotation
        _vUp = _vUp.rotateVector(axis, theta);
        _vRight = _vRight.rotateVector(axis, theta);
        _vTo = _vTo.rotateVector(axis, theta);
    }

    /**
     * Move camera (move point of view of the camera)
     * @param move {@link Vector} Vertical distance
     */
    public void moveCamera(Vector move) {
        //move Point0 according to params
        Point myPoint = new Point(_p0.get_xyz());
        myPoint=  myPoint.add(move);
        _p0 = myPoint;
    }

    /**
     * set imageWriter
     * @param imageWriter {@link ImageWriter}
     * @return updated camera {@link Camera}
     */
    public Camera setImageWriter(ImageWriter imageWriter) {
        _imageWriter = imageWriter;
        return this;
    }

    /**
     * set ray tracer
     * @param rayTracerBasic {@link RayTracerBasic}
     * @return updated camera {@link Camera}
     */
    public Camera setRayTracer(RayTracer rayTracerBasic) {
        _rayTracer = rayTracerBasic;
        return this;
    }

    /**
     * get built camera
     * @return built camera {@link Camera}
     */
    public Camera build() {
        return this;
    }

    public void writeToImage() {
        if (_imageWriter == null)
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        _imageWriter.writeToImage();
    }

    public void printGrid(int interval, Color color) {
        if (_imageWriter == null) {
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        }
        for (int i = 0; i < _imageWriter.getNx(); i++)
            for (int j = 0; j < _imageWriter.getNy(); j++)
                if (i % interval == 0 || j % interval == 0)
                    _imageWriter.writePixel(j, i, color);
        _imageWriter.writeToImage();
    }

    public void renderImage() {

        try {
            if (_imageWriter == null) {
                throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
            }
            if (_rayTracer == null) {
                throw new MissingResourceException("missing resource", RayTracer.class.getName(), "");
            }

            //rendering the image
            int nX = _imageWriter.getNx();
            int nY = _imageWriter.getNy();
            for (int i = 0; i < nY; i++) {
                for (int j = 0; j < nX; j++) {
                    _imageWriter.writePixel(j, i, castRay(nY, nX, j, i));
                }
            }
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException("Not implemented yet" + e.getClassName());
        }
    }

    /**
     * The function constructs a ray from Camera location through the center of a
     * pixel (i,j) in the view plane
     *
     * @param nX number of pixels in a row of view plane
     * @param nY number of pixels in a column of view plane
     * @param j  number of the pixel in a row
     * @param i  number of the pixel in a column
     * @return the {@link  Color} through pixel's center
     */
    private Color castRay(int nX, int nY, double j, double i) {
            return _rayTracer.traceRay(constructRay(nX,nY,(int)j,(int)i));
        }

    }



