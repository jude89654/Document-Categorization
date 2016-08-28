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
    /**
     * method that uses OCR to return text of the given image
     * @param file any image
     * @return an OCR text of the image
     */
    public static String getOCRText(File file){
        File imageFile = file;
        ITesseract instance = new Tesseract();
        // JNA Interface Mapping
        //ITesseract instance = new Tesseract1(); // JNA Direct Mapping

        //File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build bundles English data
        //instance.setDatapath("tessdata");

        instance.setLanguage("eng");

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
            return result;
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
            return "";
        }

    }



}
