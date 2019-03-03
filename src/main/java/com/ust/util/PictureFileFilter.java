package com.ust.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by jude8 on 9/2/2016.
 */
public class PictureFileFilter implements FilenameFilter{

    /**
     * Implemented filter for the train method that accepts jpeg, jpg and png files
     * @param pathname the path where the file
     * @return a boolean if it ends in a correct file type
     */
    @Override
    public boolean accept(File dir, String pathname) {
         return pathname.toLowerCase().endsWith(".jpg")
                |pathname.toLowerCase().endsWith(".png")|
                pathname.toLowerCase().endsWith(".jpeg");
    }
}
