package org.codejive.utils4gl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class that allows transparent reading of files from
 * the current working directory or from the classpath.
 * @author Pepijn Van Eeckhoudt
 * @version $Revision: 101 $
 */
public class ResourceRetriever {
	/** Finds a requested resource by name either on the disk or in a
	 * resource file. Returns an URL object pointing to the requested
	 * resource.
	 * @param filename File name of the requested resource
	 * @throws IOException If the requested resource can't be located
	 * @return URL object pointing to the requested resource
	 */	
    public static URL getResource(final String filename) throws IOException {
        // Try to load resource from jar
        URL url = ClassLoader.getSystemResource(filename);
        // If not found in jar, then load from disk
        if (url == null) {
            return new URL("file", "localhost", filename);
        } else {
            return url;
        }
    }

	/** Finds a requested resource by name either on the disk or in a
	 * resource file. Opens a stream on the requested resource.
	 * @param filename File name of the requested resource
	 * @throws IOException If the requested resource can't be located or opened
	 * @return A InputStream object for the requested resource
	 */	
    public static InputStream getResourceAsStream(final String filename) throws IOException {
        // Try to load resource from jar
        InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
        // If not found in jar, then load from disk
        if (stream == null) {
            return new FileInputStream(filename);
        } else {
            return stream;
        }
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
