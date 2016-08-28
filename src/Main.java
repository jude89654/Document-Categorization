
import java.io.File;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.LoadLibs;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "lib/win32-x86" : "lib/win32-x86-64");

        File imageFile = getImagePath();//"eurotext.tif");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping

         //File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build bundles English data
        //instance.setDatapath("tessdata");
        instance.setLanguage("eng");

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }



}


