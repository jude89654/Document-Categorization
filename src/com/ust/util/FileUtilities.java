package com.ust.util;

import com.ust.ocr.OCR;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        for (String f : source.list()) {
            createOCRFile(new File(source, f), new File(target, f));
        }
    }

    private static void copyFile(File source, File target) throws IOException {

        String content = OCR.getOCRText(source);
        FileWriter fileWriter = new FileWriter(target,false);
        fileWriter.append(content);
        fileWriter.close();
        FileUtilities.renameFileExtension(target.getAbsolutePath(), "txt");
    }

}