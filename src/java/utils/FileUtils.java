/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;

/**
 *
 * @author Mokok
 */
public class FileUtils {

	public static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				delete(c);
			}
		} else {
			f.delete();
		}
	}

	public static void resetFolder(File d) throws IOException {
		if (d.isDirectory()) {
			delete(d);
			d.mkdirs();
		} else {
			throw new NotDirectoryException(d.getAbsolutePath());
		}
	}
}
