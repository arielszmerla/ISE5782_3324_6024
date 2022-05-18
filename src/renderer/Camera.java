package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.*;

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
// A random number generator.
private Random random = new Random();
    // A private variable that is used to write the image to the screen.
    private ImageWriter _imageWriter;
    // A private variable that is used to write the image to the screen.
    private RayTracer _rayTracer;
    /**
     * Distance of the depth of field plane from the view plane
     */
    private double _focusField=1000;

    /**
     * Radius of the aperture
     */
    private double _apertureFieldDistance=250;


    // The radius of the aperture.
    private double _apertureFieldRadius=1d;
    /**
     * Set the aperture field of the camera.
     *
     * @param focusField The aperture field of the camera.
     * @return The camera object itself.
     */

    public Camera setFocusField(double focusField) {
        _focusField = focusField;
        return this;
    }

    /**
     * This function sets the aperture field radius of the camera
     *
     * @param apertureFieldRadius The radius of the aperture field.
     * @return The camera object itself.
     */
    public Camera setApertureFieldRadius(double apertureFieldRadius) {
        _apertureFieldRadius = apertureFieldRadius;
        return this;
    }



    /**
     * Camera constructor based-on point and 2 vectors
     * @param p0 {@link Point} position
     * @param vto {@link Vector}
     * @param vup {@link  Vector}
     */
    // This is the constructor of the camera. It receives 3 vectors: p0, vto and vup. The function checks if the vectors
    // are orthogonal. If they are not, it throws an exception. If they are, it sets the vectors to the camera.
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

    /**
     * This function prints a grid on the image
     *
     * @param interval the interval between the lines.
     * @param color the color of the grid lines.
     */
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
                    _imageWriter.writePixel(j, i, castRayDepth(nY, nX, j, i));
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
        Ray centerRay=constructRay(nX,nY,(int)j,(int)i);

            return _rayTracer.traceRay(centerRay).add();
    }
    private Color castRayDepth(int nX, int nY, double j, double i) {
        Ray centerRay=constructRay(nX,nY,(int)j,(int)i);
        Point focalPoint= calcFocalFieldPoint(centerRay);
        List<Point> aperturePoints= get4PointOnAperture(centerRay);
        List<Ray> apertureRays=contructRays(aperturePoints,focalPoint);
        return averageColor(apertureRays,centerRay);

    }
    public Color averageColor(List<Ray> rays,Ray centerRay){
        Color color=Color.BLACK;
        for( Ray ray:rays){
            color=color.add(_rayTracer.traceRay(ray));
        }
        color= color.add(_rayTracer.traceRay(centerRay));
        return color.reduce(Double.valueOf(rays.size()+1));
    }

    private Point calcFocalFieldPoint(Ray ray) {
        double len= _focusField /_vTo.dotProduct(ray.getDir());
        return ray.getPoint(len);
    }
    private Point calcApertureFieldPoint(Ray ray) {
        double len= _apertureFieldDistance /_vTo.dotProduct(ray.getDir());
        return ray.getPoint(len);
    }

    private List<Point> get4PointOnAperture(Ray ray)
    {
        List<Point> points=new LinkedList<>() ;
            Point apertureCenter= calcApertureFieldPoint( ray);

        for (int i=0 ;i<10;i++){
            Point p = apertureCenter.add(_vUp.scale(random(-_apertureFieldRadius,_apertureFieldRadius))).add(_vRight.scale(random(-_apertureFieldRadius,_apertureFieldRadius)));
            points.add(p);
        }
        return points;
    }
    private List<Ray> contructRays(List<Point> aperturePoints ,Point focusPlaneIntersection){
        List<Ray> rays = new LinkedList<>();
        for (Point point : aperturePoints){
            rays.add( new Ray(point, new Vector(focusPlaneIntersection.substract(point).get_xyz())));
        }
        return rays;
    }
}



