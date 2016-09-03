package com.ust.util;

import java.io.File;

/**
 * Created by jude8 on 9/2/2016.
 */
public class PictureFileFilter implements java.io.FileFilter{

    /**
     * Implemented filter for the train method that accepts jpeg, jpg and png files
     * @param pathname the path where the file
     * @return a boolean if it ends in a correct file type
     */
    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".jpg")
                |pathname.getName().toLowerCase().endsWith(".png")|
                pathname.getName().toLowerCase().endsWith(".jpeg")
                |pathname.getName().toLowerCase().endsWith(".");
    }

}
