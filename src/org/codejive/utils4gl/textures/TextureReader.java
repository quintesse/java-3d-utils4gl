package org.codejive.utils4gl;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Image loading class that converts BufferedImages into a data
 * structure that can be easily passed to OpenGL.
 * @author Pepijn Van Eeckhoudt
 * @version $Revision: 206 $
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
        return readTexture(filename, storeAlphaChannel, null);
    }

	/** Returns a Texture object initialized with the image data
	 * and optionally a mask color that will be used to fill the
	 * texture's alpha channel: pixels having the specified color
	 * will be fully transparant.
	 * @param filename File name of the requested image
	 * @param Color the color used to determine transparancy
	 * @throws IOException If the requested image can't be located or read
	 * @return A Texture object witout alpha information
	 */	 
    public static Texture readTexture(String filename, Color _maskColor) throws IOException {
    	return readTexture(filename, true, _maskColor);
    }
    
	private static Texture readTexture(String filename, boolean storeAlphaChannel, Color _maskColor) throws IOException {
		BufferedImage bufferedImage;
		if (filename.endsWith(".bmp")) {
			bufferedImage = BitmapLoader.loadBitmap(filename);
		} else {
			bufferedImage = readImage(filename);
		}
		return readPixels(bufferedImage, storeAlphaChannel, _maskColor);
	}

	private static BufferedImage readImage(String resourceName) throws IOException {
        URL url = ResourceRetriever.getResource(resourceName);
        if (url == null) {
            throw new RuntimeException("Error reading resource " + resourceName);
        }
        return ImageIO.read(url);
    }

	private static Texture readPixels(BufferedImage img, boolean storeAlphaChannel, Color _maskColor) {
		int[] packedPixels = new int[img.getWidth() * img.getHeight()];

		PixelGrabber pixelgrabber = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(), packedPixels, 0, img.getWidth());
		try {
			pixelgrabber.grabPixels();
		} catch (InterruptedException e) {
			throw new RuntimeException("Could not grab pixels", e);
		}

		int bytesPerPixel = storeAlphaChannel ? 4 : 3;
		ByteBuffer unpackedPixels = ByteBuffer.allocateDirect(packedPixels.length * bytesPerPixel);

		for (int row = img.getHeight() - 1; row >= 0; row--) {
			for (int col = 0; col < img.getWidth(); col++) {
				int packedPixel = packedPixels[row * img.getWidth() + col];
				byte nRed = ((byte) ((packedPixel >> 0) & 0xFF));
				byte nGreen = ((byte) ((packedPixel >> 8) & 0xFF));
				byte nBlue = ((byte) ((packedPixel >> 16) & 0xFF));
				unpackedPixels.put(nBlue).put(nGreen).put(nRed);
				if (storeAlphaChannel) {
					if (_maskColor != null) {
						// if the color matches clear the alpha byte (transparent)
						if ((nBlue == _maskColor.getBlue()) 
							&& (nGreen == _maskColor.getGreen()) 
							&& (nRed == _maskColor.getRed())) {
							unpackedPixels.put((byte) ((0x00)));	
						} else {
							unpackedPixels.put((byte) ((0xFF)));	                		
						}
					} else {
						// get alpha from 4th byte
						unpackedPixels.put((byte) ((packedPixel >> 24) & 0xFF));
					}
				}
			}
		}
		return new Texture(unpackedPixels, img.getWidth(), img.getHeight());
	}
}

/*
 * $Log$
 * Revision 1.4  2003/12/15 13:59:02  tako
 * Now supports color maskinig to indicatie which parts of the texture are
 * (100%) transparant. Original patch by Gertjan Assies.
 *
 * Revision 1.3  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
