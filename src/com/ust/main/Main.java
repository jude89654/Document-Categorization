package com.ust.main;

import java.io.*;
import java.util.ArrayList;


import com.ust.SVM.Test;
import com.ust.SVM.Train;
import com.ust.util.FileUtilities;
import com.ust.util.PictureFileFilter;
import liblinear.InvalidInputDataException;
import org.apache.commons.io.FileUtils;
import org.apache.xmlgraphics.image.codec.util.SeekableOutputStream;

/**
 * com.ust.main.Main class of the system
 */
public class Main {
    /**
     * The root folder of the Categorizer
     */
    static String categorizedFilesPath = "CategorizedDocuments";

    static String projectFolderPath ="project";

    static String tempFolderPath= "tempFolder";


    static String classNameFile = "class_name.txt";

    static String devLabelFile = "dev_label.txt";

    static ArrayList<String> categoryFolderNames = new ArrayList<>();

    /**
     * com.ust.main.Main method of the program
     *
     * @param args
     */
    public static void main(String[] args) {


        // static String[] categories
        try {

            //Method used to create an OCR File of the test and training data
            createOCRFiles();
            //Copy the class_name file
            copyClassFile();

            //Copy the dev_label file
            copyDevFile();

            //get categories
            getTrainingFolderNames();

            //Start Training all data
            Train.startTraining();

            //Start Classifying all the testData
            Test.StartTesting();

            //Delete the Categorized documents folder
            FileUtils.deleteDirectory(new File(categorizedFilesPath));
            ////Create the root folder and the subfolders for the categorized files
            initializeFolders();

            //create an array of the files and move it to its respective folders using the result.txt
            //get all png and jpeg files
            File[] originalFiles = new File("project/dev").listFiles(new PictureFileFilter());
            ArrayList<Integer> results = getResults(new File("result.txt"));

            //copy the files.
            moveCategorizedFiles(originalFiles, results);


        } catch (IOException io) {
            System.out.println("ERROR IN INITIALIZING FILES");
            io.printStackTrace();
        } catch (InvalidInputDataException e) {
            e.printStackTrace();
        }
    }



    /**
     * Method to create OCR Files on the startTraining and dev folder
     */
    public static void createOCRFiles(){
        try {
            System.out.println("CREATING OCR FOR TEST FILES");
            FileUtilities.createOCRFile(new File(projectFolderPath +"/dev" ), new File(tempFolderPath+"/dev"));
            System.out.println("CREATING OCR FOR TRAINING FILES");
            FileUtilities.createOCRFile(new File(projectFolderPath +"/train"), new File(tempFolderPath+"/train"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void  copyDevFile(){
          FileUtilities.copyDevLabelFile(new File(projectFolderPath+File.separator+devLabelFile), new File(tempFolderPath+File.separator+devLabelFile));
    }

    public static void copyClassFile(){
        try {
            FileUtils.copyFile(new File(projectFolderPath+File.separator+classNameFile), new File(tempFolderPath+File.separator+classNameFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method for moving the categorized files based on the results.txt
     *
     * @param categorizedfile Array of files
     * @param results
     */
    public static void moveCategorizedFiles(File[] categorizedfile, ArrayList<Integer> results) {
        for (int index = 0; index < results.size(); index++) {
            try {
                FileUtils.copyFile(categorizedfile[index], new File(categorizedFilesPath + "/" + categoryFolderNames.get(results.get(index)) + categorizedfile[index].getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * create an ArrayList of integers of the result of the categorization of the current file.
     *
     * @param result
     * @return
     */
    public static ArrayList<Integer> getResults(File result) {
        ArrayList<Integer> resultArrayList = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(result));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultArrayList.add(Integer.parseInt(line.split(" ")[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultArrayList;
    }


    /**
     * method to create a folder for creating the root folder and subfolders for the Categorized files.
     */
    public static void initializeFolders() {
        System.out.println("INITIALIZING FOLDERS");
        File mainFolder = new File(categorizedFilesPath);
        mainFolder.mkdir();

        File tempFolder = new File(tempFolderPath);
        tempFolder.mkdir();

        //folders for the categorized documents root directory

        for (String currentFolderName : categoryFolderNames) {
            File file = new File("CategorizedDocuments/" + currentFolderName);
            file.mkdir();
        }

    }

    public static void getTrainingFolderNames(){
        //ArrayList<String> pathNames = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(projectFolderPath+File.separator+classNameFile));
            String line;
            while((line=br.readLine())!=null){
                String path =line.split(" ")[1];
                path=path.replace(".",File.separator);
                path+=File.separator;
                categoryFolderNames.add(path);
            }
            categoryFolderNames.add("others"+File.separator);

        } catch (FileNotFoundException e) {
            System.out.println("CLASS FILE NOT FOUND");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


