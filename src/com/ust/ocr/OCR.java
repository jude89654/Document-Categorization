package com.ust.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.swing.*;
import java.io.File;

/**
 * Created by jude8 on 8/28/2016.
 * A class that is used for OCR
 */
public class OCR {

    public static ITesseract instance = new Tesseract();

    


    /**
     * method that uses OCR to return text of the given image
     * @param file any image
     * @return an OCR text of the image
     */
    public static String getOCRText(File file){
        instance.setLanguage("eng");
        try {
            System.out.println("CREATING OCR TEXTFILE FOR:"+file.getName());
            //System.out.println(result);
            return instance.doOCR(file);
        } catch (TesseractException e) {
            System.out.println("ERROR ON CREATING OCR ON FILE:"+file.getName());
           // e.printStackTrace();
            //System.err.println(e.getMessage());
            return "";
        }

    }
}
