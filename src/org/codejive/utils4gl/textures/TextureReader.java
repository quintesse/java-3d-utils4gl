package org.codejive.utils4gl.textures;

import javax.imageio.ImageIO;

import org.codejive.utils4gl.BitmapLoader;
import org.codejive.utils4gl.RenderContext;
import org.codejive.utils4gl.ResourceRetriever;

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
 * @version $Revision: 214 $
 */
public class TextureReader {
	/** Returns a Texture object initialized with the image data
	 * (but without the alpha channel) as stored in the given file.
	 * @param _context Render context
	 * @param _filename File name of the requested image
	 * @throws IOException If the requested image can't be located or read
	 * @return A Texture object witout alpha information
	 */	
    public static Texture readTexture(RenderContext _context, String _filename) throws IOException {
        return readTexture(_context, _filename, false);
    }

    /** Returns a TextureBuffer object initialized with the image data
     * (but without the alpha channel) as stored in the given file.
     * @param _context Render context
     * @param _filename File name of the requested image
     * @throws IOException If the requested image can't be located or read
     * @return A TextureBuffer object witout alpha information
     */	
    public static TextureBuffer readTextureBuffer(RenderContext _context, String _filename) throws IOException {
    	return readTextureBuffer(_context, _filename, false);
    }
    
	/** Returns a Texture object initialized with the image data
	 * and optionally the alpha channel as stored in the given file.
	 * @param _context Render context
	 * @param _filename File name of the requested image
	 * @param storeAlphaChannel A boolean indicating if alpha channel information should
	 * be read and stored in the Texture object as well
	 * @throws IOException If the requested image can't be located or read
	 * @return A Texture object witout alpha information
	 */	 
    public static Texture readTexture(RenderContext _context, String _filename, boolean storeAlphaChannel) throws IOException {
        return readTexture(_context, _filename, storeAlphaChannel, null);
    }

    /** Returns a TextureBuffer object initialized with the image data
     * and optionally the alpha channel as stored in the given file.
     * @param _context Render context
     * @param _filename File name of the requested image
     * @param storeAlphaChannel A boolean indicating if alpha channel information should
     * be read and stored in the TextureBuffer object as well
     * @throws IOException If the requested image can't be located or read
     * @return A TextureBuffer object witout alpha information
     */	 
    public static TextureBuffer readTextureBuffer(RenderContext _context, String _filename, boolean storeAlphaChannel) throws IOException {
    	return readTextureBuffer(_context, _filename, storeAlphaChannel, null);
    }

	/** Returns a Texture object initialized with the image data
	 * and optionally a mask color that will be used to fill the
	 * texture's alpha channel: pixels having the specified color
	 * will be fully transparant.
	 * @param _context Render context
	 * @param _filename File name of the requested image
	 * @param _maskColor The color used to determine transparancy
	 * @throws IOException If the requested image can't be located or read
	 * @return A Texture object witout alpha information
	 */	 
    public static Texture readTexture(RenderContext _context, String _filename, Color _maskColor) throws IOException {
    	return readTexture(_context, _filename, true, _maskColor);
    }
    
    /** Returns a TextureBuffer object initialized with the image data
     * and optionally a mask color that will be used to fill the
     * texture's alpha channel: pixels having the specified color
     * will be fully transparant.
     * @param _context Render context
     * @param _filename File name of the requested image
     * @param _maskColor The color used to determine transparancy
     * @throws IOException If the requested image can't be located or read
     * @return A TextureBuffer object witout alpha information
     */	 
    public static TextureBuffer readTextureBuffer(RenderContext _context, String _filename, Color _maskColor) throws IOException {
    	return readTextureBuffer(_context, _filename, true, _maskColor);
    }
    
    private static Texture readTexture(RenderContext _context, String _filename, boolean _storeAlphaChannel, Color _maskColor) throws IOException {
		TextureBuffer buf = readTextureBuffer(_context, _filename, _storeAlphaChannel, _maskColor);
		return new SimpleTexture(buf);
	}

    private static TextureBuffer readTextureBuffer(RenderContext _context, String _filename, boolean _storeAlphaChannel, Color _maskColor) throws IOException {
    	BufferedImage bufferedImage;
    	if (_filename.endsWith(".bmp")) {
    		bufferedImage = BitmapLoader.loadBitmap(_filename);
    	} else {
    		bufferedImage = readImage(_filename);
    	}
    	TextureBuffer buf = readTextureBuffer(_context, bufferedImage, _storeAlphaChannel, _maskColor);
    	return buf;
    }

    private static BufferedImage readImage(String resourceName) throws IOException {
        URL url = ResourceRetriever.getResource(resourceName);
        if (url == null) {
            throw new RuntimeException("Error reading resource " + resourceName);
        }
        return ImageIO.read(url);
    }

	private static TextureBuffer readTextureBuffer(RenderContext _context, BufferedImage img, boolean storeAlphaChannel, Color _maskColor) {
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
		TextureBuffer buffer = new TextureBuffer(_context, unpackedPixels, img.getWidth(), img.getHeight());
		return buffer;
	}
}

/*
 * $Log$
 * Revision 1.5  2004/03/07 16:26:38  tako
 * Texture has been turned into an interface and textures itself are split into
 * TextureBuffers that hold the binary data while Textures determine which
 * part of the buffer to use. All existing code has been adjusted accordingly.
 *
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
