package iss.workshop.android_ca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloader {
    protected boolean downloadImage(String imgURL, File destFile) {
        try {
            //open connection
            URL url = new URL(imgURL);
            URLConnection conn = url.openConnection();

            //set input & output Stream
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(destFile);

            //Read Write to file
            byte[] buf = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
