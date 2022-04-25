package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.*;

public class Camera {

    private Vector _vRight;
    private Vector _vTo;
    private Vector _vUp;
    private Point _p0;
    private double _distance;
    private int _width;
    private int _height;
    private ImageWriter _imageWriter;
    private RayTracer _rayTracer;

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
        _vRight = _vRight.rotateVector(axis, theta);
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
        _imageWriter = imageWriter;
        return this;
    }

    public Camera setRayTracer(RayTracer rayTracerBasic) {
        _rayTracer = rayTracerBasic;
        return this;
    }

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

//        //Pc = P0 + d * vTo
//        Point pc = _p0.add(_vTo.scale(_distance));
//        Point pIJ = pc;
//
//        //Ry = height / nY : height of a pixel
//        double rY = alignZero(_height / nY);
//        //Ry = weight / nX : width of a pixel
//        double rX = alignZero(_width / nX);
//        //xJ is the value of width we need to move from center to get to the point
//        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);
//        //yI is the value of height we need to move from center to get to the point
//        double yI = alignZero(-(i - ((nY - 1) / 2d)) * rY);
//
//        if (xJ != 0) {
//            pIJ = pIJ.add(_vRight.scale(xJ)); // move to the point
//        }
//        if (yI != 0) {
//            pIJ = pIJ.add(_vUp.scale(yI)); // move to the point
//        }
//
//        //get vector from camera p0 to the point
//        Vector vIJ = pIJ.substract(_p0);
//
//        //return ray to the center of the pixel
//        Ray myRay = new Ray(_p0, vIJ);

            return _rayTracer.traceRay(constructRay(nX,nY,(int)j,(int)i));
        }

    }



