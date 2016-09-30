package com.ust.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by jude8 on 8/28/2016.
 * A class that is used for OCR
 */
public class OCR {

    public static Tesseract instance = new Tesseract();


    /**
     * method that uses OCR to return text of the given image
     * @param file any image
     * @return an OCR text of the image
     */
    public static String getOCRText(File file){
        instance.setLanguage("eng");
        try {
            System.out.println("NOW READING IMAGE:"+file.getName());
            File convertedImage = convertImage(file);
            System.out.println("NOW CREATING OCR TEXT FILE");
            String result = instance.doOCR(convertedImage);
            System.out.println("OCR FILE CREATED");
            return result;
        } catch (TesseractException e) {
            System.out.println("ERROR ON CREATING OCR ON FILE:"+file.getName());
           // e.printStackTrace();
            //System.err.println(e.getMessage());
            return "";
        }

    }


    /**
     * Method that converts a file to a jpeg
     * @param file the file to be converted
     * @return the jpeg file :D
     */
    public static File convertImage(File file){
        System.out.println("CONVERTING IMAGE:"+file.getName());
        try{
            FileInputStream inputStream = new FileInputStream(file);

            BufferedImage image = ImageIO.read(inputStream);
            File tempFile = new File("temp.jpg");

            ImageIO.write(image,"jpg",new File("temp.jpg"));
            System.out.println("IMAGE CONVERTED");

            inputStream.close();
            return tempFile;

        }catch(IOException e){
            System.out.print("ERROR IN CONVERTING IMAGE");
            e.printStackTrace();
            return null;
        }

    }
}
