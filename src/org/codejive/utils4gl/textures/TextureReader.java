package org.codejive.utils4gl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Image loading class that converts BufferedImages into a data
 * structure that can be easily passed to OpenGL.
 * @author Pepijn Van Eeckhoudt
 * @version $Revision: 101 $
 */
public class TextureReader {
	/** Returns a Texture object initialized with the image data
	 * (but without the alpha channel) as stored in the given file.
	 * @param filename File name of the requested image
	 * @throws IOException If the requested image can't be located or read
	 * @return A Texture object witout alpha information
	 */	
    public static Texture readTexture(String filename) throws IOException {
        return readTexture(filename, false);
    }

	 /** Returns a Texture object initialized with the image data
	  * and optionally the alpha channel as stored in the given file.
	  * @param filename File name of the requested image
	  * @param storeAlphaChannel A boolean indicating if alpha channel information should
	  * be read and stored in the Texture object as well
	  * @throws IOException If the requested image can't be located or read
	  * @return A Texture object witout alpha information
	  */	 
    public static Texture readTexture(String filename, boolean storeAlphaChannel) throws IOException {
        BufferedImage bufferedImage;
        if (filename.endsWith(".bmp")) {
            bufferedImage = BitmapLoader.loadBitmap(filename);
        } else {
            bufferedImage = readImage(filename);
        }
        return readPixels(bufferedImage, storeAlphaChannel);
    }

    private static BufferedImage readImage(String resourceName) throws IOException {
        URL url = ResourceRetriever.getResource(resourceName);
        if (url == null) {
            throw new RuntimeException("Error reading resource " + resourceName);
        }
        return ImageIO.read(url);
    }

    private static Texture readPixels(BufferedImage img, boolean storeAlphaChannel) {
        int[] packedPixels = new int[img.getWidth() * img.getHeight()];

        PixelGrabber pixelgrabber = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(), packedPixels, 0, img.getWidth());
        try {
            pixelgrabber.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        int bytesPerPixel = storeAlphaChannel ? 4 : 3;
        ByteBuffer unpackedPixels = ByteBuffer.allocateDirect(packedPixels.length * bytesPerPixel);

        for (int row = img.getHeight() - 1; row >= 0; row--) {
            for (int col = 0; col < img.getWidth(); col++) {
                int packedPixel = packedPixels[row * img.getWidth() + col];
                unpackedPixels.put((byte) ((packedPixel >> 16) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 8) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 0) & 0xFF));
                if (storeAlphaChannel) {
                    unpackedPixels.put((byte) ((packedPixel >> 24) & 0xFF));
                }
            }
        }

        return new Texture(unpackedPixels, img.getWidth(), img.getHeight());
    }
}

/*
 * $Log$
 * Revision 1.3  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
