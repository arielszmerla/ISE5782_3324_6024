package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.*;
import java.util.stream.IntStream;

import static primitives.Util.*;
import static renderer.Pixel.printInterval;

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
    private double _width;
    /**
     * height of view plane
     */
    private double _height;
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
    private double _apertureFieldRadius=2d;
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

        return new Ray(_p0, Pij.subtract(_p0));
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
            Pixel.initialize(nY, nX, printInterval);
            IntStream.range(0, nY).parallel().forEach(i -> {
                IntStream.range(0, nX).parallel().forEach(j -> {
                            castRays_AntiAliasing(nX, nY, j, i);
                            Pixel.pixelDone();
                            Pixel.printPixel();
                });
            });

        /*   for (int i = 0; i < nY; i++) {

                for (int j = 0; j < nX; j++) {
                    _imageWriter.writePixel(j, i, castRays_AntiAliasing(nY, nX, j, i));
                }
            }*/
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
    /**
     * The function casts a ray from the camera to the focal point, and then casts rays from the focal point to the scene
     *
     * @param nX the x coordinate of the pixel in the image
     * @param nY the y coordinate of the pixel in the image
     * @param j the x coordinate of the pixel in the image
     * @param i the x coordinate of the pixel in the image
     * @return The color of the pixel.
     */
    private Color castRayDepth(int nX, int nY, double j, double i) {
        Ray centerRay=constructRay(nX,nY,(int)j,(int)i);
        Point focalPoint= calcFocalFieldPoint(centerRay);
        return colorSecundaryRays(centerRay,focalPoint);
    }
    /**
     * Given a color and a length, return the average color of the color and the length.
     *
     * @param color The color to average
     * @param len the length of the array
     * @return The average color of the pixels in the image.
     */
    public Color averageColor(Color color, int len){
        return color.reduce(len);
    }

    /**
     * The focal point is the point on the focal plane that is the same distance from the focal plane as the intersection
     * point is from the view plane
     *
     * @param ray the ray that is being traced
     * @return The point on the focal plane that the ray intersects.
     */
    private Point calcFocalFieldPoint(Ray ray) {
        double len= _focusField /_vTo.dotProduct(ray.getDir());
        return ray.getPoint(len);
    }
    /**
     * > Given a ray, calculate the point on the aperture field that the ray would hit
     *
     * @param ray the ray to be traced
     * @return The point on the aperture field that the ray intersects.
     */
    private Point calcApertureFieldPoint(Ray ray) {
        double len= _apertureFieldDistance /_vTo.dotProduct(ray.getDir());
        return ray.getPoint(len);
    }

    /**
     * The function creates a random point on the aperture field, and then creates a ray from that point to the focus plane
     * intersection point. The color of the ray is then added to the color of the primary ray
     *
     * @param ray The ray that hit the focus plane.
     * @param focusPlaneIntersection The point on the focus plane where the primary ray intersects.
     * @return The color of the point in the focus plane.
     */
    private Color colorSecundaryRays(Ray ray,Point focusPlaneIntersection)
    {
        Color color=  _rayTracer.traceRay(ray);
        Point apertureCenter= calcApertureFieldPoint( ray);
        int i=0;
        double d = 360d/5d;
        for (;i<5;i++){
            Vector v=  _vUp.rotateVector(_vRight,d*i).scale(_apertureFieldRadius).rotateVector(_vUp,d*i);
            Point p=apertureCenter.add(v);
            Ray depthRay=new Ray(p,new Vector(focusPlaneIntersection.subtract(p).get_xyz()));
            color=color.add(_rayTracer.traceRay(depthRay));
        }
        return color.scale(1/5d);
    }



    public HashSet <Ray> constructRays(int nX, int nY, int j, int i) {
        double Ry =  _height / nY;
        double Rx =  _width / nX;

        // Image center
        Point Pc = _p0.add(_vTo.scale(_distance));

        Point Pij = Pc;

        double yI = -(i - ((nY - 1) / 2d)) * Ry;
        double xJ = (j - ((nX - 1) / 2d)) * Rx;

        if (xJ != 0)
            Pij = Pij.add(_vRight.scale(xJ));
        if (yI != 0)
            Pij = Pij.add(_vUp.scale(yI));

        //Ry = height / nY : height of a pixel
        double halfRy = alignZero( _height /(2 *nY));
        //Ry = weight / nX : width of a pixel
        double halfRx = alignZero(_width /(2* nX));

        Color c1 = _rayTracer.traceRay(constructRayThroughPixel(nX,nY,Pij.add(_vRight.scale(halfRx)).add(_vUp.scale(halfRy)).subtract(_p0)));;

        Color c2 = _rayTracer.traceRay(constructRayThroughPixel(nX,nY,Pij.add(_vRight.scale(-halfRx)).add(_vUp.scale(-halfRy)).subtract(_p0)));

        Color c3 = _rayTracer.traceRay(constructRayThroughPixel(nX,nY,Pij.add(_vRight.scale(-halfRx)).add(_vUp.scale(-halfRy)).subtract(_p0)));
       Color c4 = _rayTracer.traceRay(constructRayThroughPixel(nX,nY,Pij.add(_vRight.scale(halfRx)).add(_vUp.scale(-halfRy)).subtract(_p0)));
        boolean checkEdges = c1.equals(c2) && c1.equals(c3) && c1.equals(c4);


        HashSet<Ray> myra= new HashSet<>();//to save all the different rays created

        if (!checkEdges||checkEdges){
        //We call the function constructRayThroughPixel like we used to but this time we launch m * n ray in the same pixel
            for (int k = 0; k < 100; k++) {
              Point tmp = Pij;
                Ray ray=constructRayThroughPixel(nX, nY, Pij);
                myra.add(ray);
                Pij = tmp;
            }
        }
        else {
            Ray ray = constructRayThroughPixel(nX, nY, Pij);
            myra.add(ray);
        }

        return myra;
    }



    /**
     * The function casts rays from the camera to the scene, and returns the color of the pixel (anti-aliasing)
     *
     * @param nX the number of pixels in the X axis
     * @param nY the number of pixels in the y direction
     * @param j the x coordinate of the pixel in the image
     * @param i the current pixel's row
     * @return The color of the pixel.
     */
    private Color castRays_AntiAliasing(int nX, int nY, int j, int i) {
        HashSet<Ray> rays = constructRays(nX, nY, j, i);
        Color color = Color.BLACK;
        for(Ray ray: rays){
            color = color.add(_rayTracer.traceRay(ray));
        }
        return averageColor(color,rays.size());
    }


    /**
     * The function gets a pixel and checks if the color of the pixel is the same as the color of the pixel's neighbors
     *
     * @param nX number of pixels in the width
     * @param nY number of pixels in the height
     * @param j the number of pixels in the width
     * @param i the row number of the pixel
     * @return true if all the colors are the same and false otherwise
     */
    private boolean calcFourEdges(int nX, int nY, int j, int i){
        //  double Ry = (double) _height / nY;
        // double Rx = (double) _width / nX;

        // Image center
        Point pC = _p0.add(_vTo.scale(_distance));


        //Ry = height / nY : height of a pixel
        double halfRy = alignZero( _height /(2 *nY));
        //Ry = weight / nX : width of a pixel
        double halfRx = alignZero(_width /(2* nX));

        Color c1 = _rayTracer.traceRay(constructRayThroughPixel(nX,nY,pC.add(_vRight.scale(halfRx)).add(_vUp.scale(halfRy)).subtract(_p0)));
        Color c2 = _rayTracer.traceRay(constructRayThroughPixel(nX,nY,pC.add(_vRight.scale(-halfRx)).add(_vUp.scale(halfRy)).subtract(_p0)));
        Color c3 = _rayTracer.traceRay(constructRayThroughPixel(nX,nY,pC.add(_vRight.scale(-halfRx)).add(_vUp.scale(-halfRy)).subtract(_p0)));
        Color c4 = _rayTracer.traceRay(constructRayThroughPixel(nX,nY,pC.add(_vRight.scale(halfRx)).add(_vUp.scale(-halfRy)).subtract(_p0)));

        return c2.equals(c3) && c1.equals(c2) && c4.equals(c1);
    }
    /**
     * We start from the center of the pixel and move randomly in the pixel to get the point
     *
     * @param nX number of pixels in the width
     * @param nY number of pixels in the height
     * @param pc the center of the pixel
     * @return A ray from the camera to the center of the pixel.
     */
    private Ray constructRayThroughPixel(int nX, int nY, Point pc) {

        Point pIJ = pc;
        //Ry = height / nY : height of a pixel
        double rY =  _height / nY;
        //Ry = weight / nX : width of a pixel
        double rX =  _width / nX;
        //xJ is the value of width we need to move from center to get to the point
        //we get to the bottom/top of the pixel and then we move randomly in the pixel to get the point
        double xJ = random.nextDouble () * (rX/ (random.nextBoolean()?2:-2));
        //yI is the value of height we need to move from center to get to the point
        //we get to the side of the pixel and then we move randomly in the pixel to get the point
        double yI = random.nextDouble ()* (rY/ (random.nextBoolean()?2:-2));

        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }

        //get vector from camera p0 to the point
        Vector vIJ = pIJ.subtract(_p0);
        //return ray to the center of the pixel
        return new Ray(_p0, vIJ);

    }
}



