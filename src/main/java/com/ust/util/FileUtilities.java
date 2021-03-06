package com.ust.util;

import com.ust.ocr.OCR;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtilities {

    public static boolean renameFileExtension (String source, String newExtension) {
        String target;
        String currentExtension = getFileExtension(source);

        if (currentExtension.equals("")) {
            target = source + "." + newExtension;
        } else {
            target = source.replaceFirst(Pattern.quote("." +
                    currentExtension) + "$", Matcher.quoteReplacement("." + newExtension));

        }
        return new File(source).renameTo(new File(target));
    }

    public static String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            ext = f.substring(i + 1);
        }
        return ext;
    }

    public static void main(String args[]) throws Exception {
        System.out.println(
                FileUtilities.renameFileExtension("C:/temp/capture.pdf", "pdfa")
        );
    }

    public static void createOCRFile(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    private static void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }


        //TODO
        for (String f : source.list()) {
            createOCRFile(new File(source, f), new File(target, f));
        }
    }

    private static void copyFile(File source, File target) throws IOException {

        if(source.getName().toLowerCase().endsWith(".png")
                |source.getName().toLowerCase().endsWith(".jpeg")
                |source.getName().toLowerCase().endsWith(".jpg")) {

            FileUtilities.renameFileExtension(target.getAbsolutePath(), "txt");
            OCR ocr = new OCR();
            String content = ocr.getOCRText(source);
            FileWriter fileWriter = new FileWriter(target, false);
            fileWriter.append(content);
            fileWriter.close();
            FileUtilities.renameFileExtension(target.getAbsolutePath(), "txt");

        }else if(source.getName().toLowerCase().endsWith(".txt")){
            FileUtils.copyFile(source,target);
        }
        FileUtilities.renameFileExtension(target.getAbsolutePath(), "txt");
    }


    public static void copyDevLabelFile(File SourceDebFile, File destinationDebFile){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(SourceDebFile));
            FileWriter fileWriter = new FileWriter(destinationDebFile);
            String line;
            while((line=bufferedReader.readLine())!=null){
                String[] content = line.split(" ");
                fileWriter.append(content[0]).append(".txt ").append(content[1]).append("\n");
            }
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}