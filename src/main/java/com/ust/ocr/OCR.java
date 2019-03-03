package com.ust.ocr;

import com.ust.main.Main;
import com.ust.util.FileUtilities;
import com.ust.util.PictureFileFilter;
import net.coobird.thumbnailator.Thumbnails;
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


/**
 * Created by jude8 on 8/28/2016.
 * A class that is used for OCR
 */
public class OCR {

///    public static Tesseract instance = new Tesseract();

    final static String tempPictureFolderName="tempPicFolder";


    public static void main(String args[]){
        new File(tempPictureFolderName).mkdir();
        for(File file: new File(Main.PROJECT_FOLDER_PATH+File.separator+Main.DEV_FOLDER_NAME).listFiles(new PictureFileFilter())){
            OCR.createTextFile(file,Main.DEV_FOLDER_NAME);
        }

//        Arrays.stream(new File(Main.PROJECT_FOLDER_PATH+File.separator+Main.DEV_FOLDER_NAME)
//                .listFiles(new PictureFileFilter()))
//                .parallel()
//                .forEach(new Consumer<File>() {
//                    @Override
//                    public void accept(File file) {
//                        OCR.createTextFile(file, Main.DEV_FOLDER_NAME);
//                    }
//                });


        System.out.println("FINISHED NA");
    }

    /**
     * Method to create a textfile, and it will be put on the directory.
     * @param file
     * @param outputFolder
     */
    public static void createTextFile(File file,String outputFolder){
        OCR ocr = new OCR();
        String line =ocr.getOCRText(file);
        //FileUtils.deleteQuietly(file);

        try {
            //create directory
            new File(Main.TEMPORARY_FOLDER_PATH+File.separator+outputFolder).mkdirs();


            File outputFile = new File(Main.TEMPORARY_FOLDER_PATH+File.separator+outputFolder+File.separator+file.getName());
            FileUtilities.renameFileExtension(outputFile.getCanonicalPath(),"txt");
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(line);
            fileWriter.close();
            FileUtilities.renameFileExtension(outputFile.getCanonicalPath(), "txt");
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
        instance.setDatapath("C:\\Users\\user\\IdeaProjects\\Document-Categorization\\tessdata");
        try {
            System.out.println("READING IMAGE: "+file.getPath());
            File convertedImage = convertImage(file);
            System.out.println("CREATING OCR TEXT FILE FOR: "+file.getPath());
            String result = instance.doOCR(convertedImage);
            System.out.println("TEXT FILE CREATED: "+file.getPath());
            //FileUtils.deleteQuietly(convertedImage);
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
        System.out.println("CONVERTING IMAGE:"+file.getPath());
        try{
            FileInputStream inputStream = new FileInputStream(file);

            BufferedImage image = ImageIO.read(inputStream);
            File tempFile = new File(tempPictureFolderName+File.separator+file.getName()+".png");

            ImageIO.write(image,"png",tempFile);
            System.out.println("IMAGE CONVERTED:"+file.getPath());

            inputStream.close();

            Thumbnails.of(tempFile).size(3300,2550).toFile(tempFile.getPath());

            return tempFile;

        }catch(IOException e){
            System.out.print("ERROR IN CONVERTING IMAGE");
            e.printStackTrace();
            return null;
        }

    }
}
