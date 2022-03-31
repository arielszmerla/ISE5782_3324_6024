package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {


    @Test
    void testWriteToImage() {
        int nX = 800;
        int nY = 500;
        Color redColor = new Color(255d, 0d, 0d);//red color
        Color yellowColor = new Color(255d, 255d, 0d);//yellow color
        ImageWriter imageWriter = new ImageWriter("yellow submarine", nX, nY);
        for (int i = 0; i < nX; i++)
            for (int j = 0; j < nY; j++)
                if (i % 50 == 0 || j % 50 == 0)
                    imageWriter.writePixel(i, j, redColor);
                else
                    imageWriter.writePixel(i, j, yellowColor);
        imageWriter.writeToImage();
    }

    @Test
    void testWritePixel() {

    }
}