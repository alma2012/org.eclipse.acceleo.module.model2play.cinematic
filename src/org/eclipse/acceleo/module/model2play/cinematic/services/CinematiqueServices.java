package org.eclipse.acceleo.module.model2play.cinematic.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.eclipse.acceleo.module.model2play.cinematic.*;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

public class CinematiqueServices {

	/**
	 * Unzip a file given its url into a folder
	 * 
	 * @param zipURL
	 *            URL of the zip file
	 * @param folder
	 *            Folder where to unzip
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void unzip(String path) throws IOException {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL fileURL = FileLocator.find(bundle, new Path(
				"bootstrap/bootstrap.zip"), null);
		File file = new File(path+"/public/asset");
		try {
			unzip(fileURL, file);
		} catch (Exception e) {
			final IStatus message = new Status(Status.ERROR,
					Activator.PLUGIN_ID, e.getMessage());
			Activator.getDefault().getLog().log(message);
	
		}
	}

	public void unzip(URL zipURL, File folder) throws FileNotFoundException,
			IOException {

		ZipInputStream zis = new ZipInputStream(zipURL.openStream());

		ZipEntry ze = null;
		try {
			while ((ze = zis.getNextEntry()) != null) {

				File f = new File(folder.getCanonicalPath(), ze.getName());
				if (ze.isDirectory()) {
					f.mkdirs();
					continue;
				}
				f.getParentFile().mkdirs();
				OutputStream fos = new BufferedOutputStream(
						new FileOutputStream(f));
				try {
					try {
						final byte[] buf = new byte[8192];
						int bytesRead;
						while (-1 != (bytesRead = zis.read(buf)))
							fos.write(buf, 0, bytesRead);
					} finally {
						fos.close();
					}
				} catch (final IOException ioe) {
					f.delete();
					throw ioe;
				}
			}
		} finally {
			zis.close();
		}
	}
}
