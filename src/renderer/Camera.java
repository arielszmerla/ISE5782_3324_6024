package renderer;

import primitives.*;
import primitives.Vector;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static primitives.Util.*;
import static renderer.Pixel.printInterval;

public class Camera {
    private int _threads = 0;
    private boolean _print = false;

    /**
     * This function returns the number of beams.
     *
     * @return The number of beams.
     */
    public int getNumOfBeams() {
        return _numOfBeams;
    }

    private int _numOfBeams = 4;
    private final int SPARE_THREADS = 2;
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
    /**
     * Sets the number of beams and returns the camera.
     *
     * @param numOfBeams The number of beams to be emitted from the camera.
     * @return The camera object itself.
     */
    public Camera setNumOfBeams(int numOfBeams) {
        _numOfBeams = numOfBeams;
        return this;
    }
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
     * Set multithreading <br>
     * - if the parameter is 0 - number of cores less 2 is taken
     *
     * @param threads number of threads
     * @return the Render object itself
     */
    public Camera setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
        if (threads != 0)
            _threads = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            if (cores <= 2)
                _threads = 1;
            else
                _threads = cores;
        }
        return this;
    }

    /**
     * Set debug printing on
     *
     * @return the Render object itself
     */
    public Camera setDebugPrint() {
        _print = true;
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

    public void renderImage2() {

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
                            _imageWriter.writePixel(j,i,castRays_AntiAliasing(nX, nY, j, i));
                            Pixel.pixelDone();
                            Pixel.printPixel();
                });
            });
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException("Not implemented yet" + e.getClassName());
        }
    }

    /**
     * Render the image by writing every pixel of the grid
     * Use of AntiAliasing method that is shooting lots of beams in place of only one in the
     * center of the pixel
     */
    public void renderImage() {
        //Render every pixel of the image
        Point pC = _p0.add(_vTo.scale(_distance));
        int nX = _imageWriter.getNx();
        int nY = _imageWriter.getNy();
        Pixel.initialize(nY, nX, printInterval);
        IntStream.range(0, nY).parallel().forEach(i -> {
            IntStream.range(0, nX).parallel().forEach(j -> {
                //construct ray for every pixel
                Ray myRay = constructRayThroughPixel(
                        nX,nY,i,j
                        );
                //Get the color of every pixel
                Color myColor = renderPixel(nX,nY, 3, myRay);
                //write the color on the image
                _imageWriter.writePixel(j, i, myColor);
                Pixel.pixelDone();
                Pixel.printPixel();
            });
        });
    }

    /**
     * Function that gets a pixel's center ray and constructs a list of 5 rays from that ray,
     * one at each corner of the pixel and one in the center
     * @param myRay the center's ray
     * @param nX number of pixel in width
     * @param nY number of pixels in height
     * @return list of rays
     */
    public List<Ray> construct5RaysFromRay(Ray myRay, double nX, double nY) {

        List<Ray> myRays = new LinkedList<>();

        //Ry = h / nY - pixel height ratio
        double rY = alignZero(_height / nY);
        //Rx = h / nX - pixel width ratio
        double rX = alignZero(_width / nX);

        Point center = calcFocalFieldPoint(myRay);

        //[-1/2, -1/2]
        myRays.add( new Ray(_p0, center.add(_vRight.scale(-rX / 2)).add(_vUp.scale(rY / 2)).subtract(_p0)));
        //[1/2, -1/2]
        myRays.add( new Ray(_p0, center.add(_vRight.scale(rX / 2)).add(_vUp.scale(rY / 2)).subtract(_p0)));

        myRays.add(myRay);
        //[-1/2, 1/2]
        myRays.add( new Ray(_p0, center.add(_vRight.scale(-rX / 2)).add(_vUp.scale(-rY / 2)).subtract(_p0)));
        //[1/2, 1/2]
        myRays.add( new Ray(_p0, center.add(_vRight.scale(rX / 2)).add(_vUp.scale(-rY / 2)).subtract(_p0)));
        return myRays;
    }

    /**
     * Function that returns a list of 4 rays in a pixel
     * one on the top upper the center point
     * one on the left of the center
     * one on the right of the center
     * one on the bottom of the center
     * @param ray the center's ray
     * @param nX number of pixel in width
     * @param nY number of pixels in height
     * @return list of rays
     */
    public List<Ray> construct4RaysThroughPixel(Ray ray, double nX, double nY) {

        //Ry = h / nY - pixel height ratio
        double height = alignZero(_height / nY);
        //Rx = h / nX - pixel width ratio
        double width = alignZero(_width / nX);

        List<Ray> myRays = new ArrayList<>();
        Point center = calcFocalFieldPoint(ray);
        Point point1 = center.add(_vUp.scale(height / 2));
        Point point2 = center.add(_vRight.scale(-width / 2));
        Point point3 = center.add(_vRight.scale(width / 2));
        Point point4 = center.add(_vUp.scale(-height / 2));
        myRays.add(new Ray(_p0, point1.subtract(_p0)));
        myRays.add(new Ray(_p0, point2.subtract(_p0)));
        myRays.add(new Ray(_p0, point3.subtract(_p0)));
        myRays.add(new Ray(_p0, point4.subtract(_p0)));
        return myRays;
    }

    /**
     * The function constructs 5 rays into the pixel, traces all the rays and stores the colors with the rays, and renders
     * the pixel in recursive function
     *
     * @param nX the x coordinate of the pixel in the view plane
     * @param nY the y coordinate of the pixel
     * @param depth the depth of the recursion
     * @param firstRay the first ray that was sent to the pixel
     * @return The color of the pixel
     */
    private Color renderPixel(double nX, double nY, int depth, Ray firstRay) {
        List<Ray> myRays = construct5RaysFromRay(firstRay, nX, nY); //construct 5 rays into the pixel
        HashMap<Integer, ColoredRay> rays = new HashMap<>();
        int i = 0;
        //trace all the rays and store the colors with the rays
        for (Ray myRay : myRays) {
            rays.put(++i, new ColoredRay(myRay, _rayTracer.traceRay(myRay)));
        }
        //render the pixel in recursive function
        return renderPixelRecursive(rays, nX, nY, depth);
    }


    /**
     * The function gets a map of 5 rays, the center ray is the ray of the pixel, the other rays are the rays of the 4
     * corners of the pixel. The function checks if the color of the center ray is the same as the color of the other rays,
     * if not, it sends the pixel to recursion with a map of 5 rays of the 4 under pixels
     *
     * @param myRays a map of the rays that are sent to the pixel.
     * @param nX the width of the pixel
     * @param nY the height of the pixel
     * @param depth the number of recursions to perform.
     * @return The color of the pixel.
     */
    private Color renderPixelRecursive(HashMap<Integer, ColoredRay> myRays, double nX, double nY, int depth) {
        boolean flag = false;
        HashMap<Integer, ColoredRay> rays = new HashMap<>();
        //get the center of the pixel ray
        ColoredRay mainRay = myRays.get(3);
        //get center's color
        Color mainColor = mainRay.getColor();
        if (depth >= 1) {
            //get color of all the 4 different points
            //if one differs than center need to send the pixel to compute color in recursion
            for (Integer integer : myRays.keySet()) {
                if (integer != 3) {
                    //Color tmpColor  = myRays.get(integer).getColor();
                    //Color tmpColor = tmpRay.getColor();
                   /* if (tmpColor == null || tmpColor==Color.BLACK) {
                       // tmpColor = _rayTracer.traceRay(tmpRay.getRay());
                        myRays.put(integer, tmpRay);
                    }*/
                    if (!myRays.get(integer).getColor().equals(mainColor)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                //Create a map of Colored rays for the 4 under pixels and send to recursion
                /*   List<ColoredRay> newRays =construct4RaysThroughPixel(myRays.get(3).getRay(), nX, nY).stream().map(
                        x -> new ColoredRay(x, _rayTracer.traceRay(x))
                ).collect(Collectors.toList());*/
                List<ColoredRay> newRays = new LinkedList<>();
                    List<Ray> r=construct4RaysThroughPixel(myRays.get(3).getRay(), nX, nY);
                for (Ray ray:r
                     ) {
                    newRays.add(new ColoredRay(ray,_rayTracer.traceRay(ray)));
                }
                rays.put(1, myRays.get(1));
                rays.put(2, newRays.get(0));
                Ray tempCenter = constructPixelCenterRay(myRays.get(1).getRay(), nX * 2, nY * 2);
                rays.put(3, new ColoredRay(tempCenter, _rayTracer.traceRay(tempCenter)));
                rays.put(4, newRays.get(1));
                rays.put(5, myRays.get(3));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1));
                //rays = new HashMap<>();
                rays.put(1, newRays.get(0));
                rays.put(2, myRays.get(2));
                tempCenter = constructPixelCenterRay(newRays.get(0).getRay(), nX * 2, nY * 2);
                rays.put(3, new ColoredRay(tempCenter, _rayTracer.traceRay(tempCenter)));
                rays.put(4, myRays.get(3));
                rays.put(5, newRays.get(2));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1));
               // rays = new HashMap<>();
                rays.put(1, newRays.get(1));
                rays.put(2, myRays.get(3));
                tempCenter = constructPixelCenterRay(newRays.get(1).getRay(), nX * 2, nY * 2);
                rays.put(3, new ColoredRay(tempCenter, _rayTracer.traceRay(tempCenter)));
                rays.put(4, myRays.get(4));
                rays.put(5, newRays.get(3));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1));
                //rays = new HashMap<>();
                rays.put(1, myRays.get(3));
                rays.put(2, newRays.get(2));
                tempCenter = constructPixelCenterRay(myRays.get(3).getRay(), nX * 2, nY * 2);
                rays.put(3, new ColoredRay(tempCenter, _rayTracer.traceRay(tempCenter)));
                rays.put(4, newRays.get(3));
                rays.put(5, myRays.get(5));
                mainColor = mainColor.add(renderPixelRecursive(rays, nX * 2, nY * 2, depth - 1));
                mainColor = mainColor.reduce(5);
            }
        }
        return mainColor;
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
        return ray.getPoint(_apertureFieldDistance /_vTo.dotProduct(ray.getDir()));
    }


    /**
     * The function calculates the color of the secondary rays by calculating the color of the primary ray and adding the
     * color of the secondary rays
     *
     * @param ray The ray that hit the focus plane.
     * @param focusPlaneIntersection The point where the ray intersects the focus plane.
     * @return The color of the pixel.
     */
    private Color colorSecundaryRays(Ray ray,Point focusPlaneIntersection)
    {
        Color color=_rayTracer.traceRay(ray);
        Point apertureCenter= calcApertureFieldPoint( ray);

        //rotate on the lense
        double d = 360d/5d;
        for (int i=0;i<5;i++){
            Vector v=_vUp.rotateVector(_vRight,d*i).scale(_apertureFieldRadius).rotateVector(_vUp,d*i);
            Point p=apertureCenter.add(v);
            Ray depthRay=new Ray(p,new Vector(focusPlaneIntersection.subtract(p).get_xyz()));
            color=color.add(_rayTracer.traceRay(depthRay));
        }
        return averageColor(color,5);
    }



    public HashSet <Ray> constructRays(int nX, int nY, int j, int i) {
       /* double Ry =  _height / nY;
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




        if (!checkEdges||checkEdges){*/
        HashSet<Ray> myra= new HashSet<>();//to save all the different rays created
        //We call the function constructRayThroughPixel like we used to but this time we launch m * n ray in the same pixel
            for (int k = 0; k < _numOfBeams; k++) {
                Ray ray=constructRayThroughPixel(nX, nY, j,i);
                myra.add(ray);
            }
       /* }
        else {
            Ray ray = constructRayThroughPixel(nX, nY, j,i);
            myra.add(ray);
        }*/

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
    /**
     * Construct ray through the center of a pixel when we have only the bottom right corner's ray
     * @param ray the ray
     * @param nX number of pixel in width
     * @param nY number of pixels in height
     * @return center's ray
     */
    public Ray constructPixelCenterRay(Ray ray, double nX, double nY) {

        //Ry = h / nY - pixel height ratio
        double height = alignZero(_height / nY);
        //Rx = h / nX - pixel width ratio
        double width = alignZero(_width / nX);


        Point point = calcFocalFieldPoint(ray);
        point = point.add(_vRight.scale(width / 2)).add(_vUp.scale(-height / 2));
        return new Ray(_p0, point.subtract(_p0));
    }
    /**
     * The function constructs a ray from Camera location through the center of a
     * pixel (i,j) in the view plane
     *
     * @param nX number of pixels in a row of view plane
     * @param nY number of pixels in a column of view plane
     * @param j  number of the pixel in a row
     * @param i  number of the pixel in a column
     * @return the ray through pixel's center
     */
    public Ray constructRayThroughPixel(int nX, int nY, double j, double i) {

        //Pc = P0 + d * vTo
        Point pc = _p0.add(_vTo.scale(_distance));
        Point pIJ = pc;

        //Ry = height / nY : height of a pixel
        double rY = alignZero(_height / nY);
        //Ry = weight / nX : width of a pixel
        double rX = alignZero(_width / nX);
        //xJ is the value of width we need to move from center to get to the point
        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);
        //yI is the value of height we need to move from center to get to the point
        double yI = alignZero(-(i - ((nY - 1) / 2d)) * rY);

        if (xJ != 0) {
            pIJ = pIJ.add(_vRight.scale(xJ)); // move to the point
        }
        if (yI != 0) {
            pIJ = pIJ.add(_vUp.scale(yI)); // move to the point
        }

        //get vector from camera p0 to the point
        Vector vIJ = pIJ.subtract(_p0);

        //return ray to the center of the pixel
        return new Ray(_p0, vIJ);

    }
}



