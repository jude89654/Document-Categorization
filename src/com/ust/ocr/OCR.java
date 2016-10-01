package com.ust.ocr;

import com.ust.main.Main;
import com.ust.util.FileUtilities;
import com.ust.util.PictureFileFilter;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jude8 on 8/28/2016.
 * A class that is used for OCR
 */
public class OCR {

///    public static Tesseract instance = new Tesseract();

    final static String tempPictureFolderName="tempPicFolder";


    public static void main(String args[]){
        new File(tempPictureFolderName).mkdir();
        Arrays.stream(new File(Main.PROJECT_FOLDER_PATH+File.separator+Main.DEV_FOLDER_NAME)
                .listFiles(new PictureFileFilter()))
                .parallel()
                .forEach(file->OCR.createTextFile(file,Main.DEV_FOLDER_NAME));


        System.out.println("FINISHED NA");
    }

    public static void createTextFile(File file,String outputFolder){
        OCR ocr = new OCR();
        String line =ocr.getOCRText(file);
        //FileUtils.deleteQuietly(file);

        try {
            File outputFile = new File(Main.TEMPORARY_FOLDER_PATH+File.separator+outputFolder+File.separator+file.getName());

            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(line);
            fileWriter.close();
            FileUtilities.renameFileExtension(outputFile.getPath(), "txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * method that uses OCR to return text of the given image
     * @param file any image
     * @return an OCR text of the image
     */
    public  String getOCRText(File file){

        Tesseract1 instance = new Tesseract1();
        instance.setLanguage("eng");
        try {
            System.out.println("NOW READING IMAGE:"+file.getName());
            File convertedImage = convertImage(file);
            System.out.println("NOW CREATING OCR TEXT FILE FOR:\t"+file.getName());
            String result = instance.doOCR(convertedImage);
            System.out.println("OCR TEXT CREATED FOR:\t"+file.getName());
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
    public  File convertImage(File file){
        System.out.println("CONVERTING IMAGE:"+file.getName());
        try{
            FileInputStream inputStream = new FileInputStream(file);

            BufferedImage image = ImageIO.read(inputStream);
            File tempFile = new File(tempPictureFolderName+File.separator+file.getName()+".png");

            ImageIO.write(image,"png",tempFile);
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
