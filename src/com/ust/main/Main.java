package com.ust.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import com.ust.SVM.FirstLevelClassifier;
import com.ust.SVM.SecondLevelClassifier;
import com.ust.SVM.Test;
import com.ust.SVM.Train;
import com.ust.util.FileUtilities;
import com.ust.util.PictureFileFilter;
import liblinear.InvalidInputDataException;
import org.apache.commons.io.FileUtils;

/**
 * com.ust.categorize.Main class of the system
 */
public class Main {
    /**
     * The root folder of the Categorizer
     */
    public static String CATEGORIZED_FILES_PATH = "CategorizedDocuments";

    public static String PROJECT_FOLDER_PATH = "project";

    public static String TEMPORARY_FOLDER_PATH = "tempFolder";

    public static String FIRST_LEVEL_FOLDER_NAME = "firstLevel";

    public static String SECOND_LEVEL_FOLDER_NAME = "secondLevel";

    public static String CLASS_NAME_FILE_NAME = "class_name.txt";

    public static String DEV_LABEL_FILE_NAME = "dev_label.txt";

    public static String DEV_FOLDER_NAME = "dev";

    public static String TRAINING_FOLDER_NAME = "train";

    public static Map<Integer, String> FIRST_LEVEL_FOLDER_NAMES = new HashMap<>();

    public static Map<Integer, String> SECOND_LEVEL_FOLDER_NAMES = new HashMap<>();


    public static void main(String args[]) {

        try {

            // createFirstLevelTestOCRFiles();


            Train.firstLevelTraining();
            Train.secondLevelTraining();


            getFirstLevelTrainingFolderNames();
            getSecondLevelTrainingFolderNames();

            Test.StartFirstLevelTesting();


            File[] originalFiles = new File(PROJECT_FOLDER_PATH + File.separator + DEV_FOLDER_NAME).listFiles(new PictureFileFilter());


            ArrayList<Integer> results = getResults(new File(FirstLevelClassifier.OUTPUT_RESULT_FILE));

            moveCategorizedFiles(originalFiles, results);


            // createSecondLevelTestOCRFiles();

            Test.StartSecondLevelTesting();

            results = getResults(new File(SecondLevelClassifier.OUTPUT_RESULT_FILE));

            originalFiles = new File(PROJECT_FOLDER_PATH + File.separator + DEV_FOLDER_NAME + 2).listFiles(new PictureFileFilter());


            moveLetterFiles(originalFiles, results);

            FileUtils.cleanDirectory(new File("project/dev2"));

        } catch (IOException | InvalidInputDataException e) {
            e.printStackTrace();
        }
    }


    /**
     * com.ust.categorize Main method of the program
     *
     * @param args
     */
    public static void categorize(String[] args, boolean train, boolean test, boolean trainOCR, boolean testOCR) {


        if (args.length == 2) {
            PROJECT_FOLDER_PATH = args[0];
            CATEGORIZED_FILES_PATH = args[1] + File.separator + CATEGORIZED_FILES_PATH;
        }

        //get categories
        getFirstLevelTrainingFolderNames();
        try {
            if (train) {
                //start copying training files
                createTrainingOCRFiles();
                //Start Training all data
                Train.firstLevelTraining();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            //Start Classifying all the testData
            if (test) {
                //Method used to create an OCR File of the test and training data
                createFirstLevelTestOCRFiles();

                Test.StartFirstLevelTesting();

                //Delete the Categorized documents folder
                //FileUtils.deleteDirectory(new File(CATEGORIZED_FILES_PATH));
                ////Create the root folder and the subfolders for the categorized files
                initializeFolders();

                //create an array of the files and move it to its respective folders using the result.txt
                //get all png and jpeg files
                File[] originalFiles = new File("project/dev").listFiles(new PictureFileFilter());
                ArrayList<Integer> results = getResults(new File("result.txt"));

                //copy the files.
                moveLetterFiles(originalFiles, results);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * Method to create OCR Files on the firstLevelTraining and dev folder
     */
    public static void createFirstLevelTestOCRFiles() {
        try {
            System.out.println("====CREATING OCR FOR TEST FILES====");
            FileUtilities.createOCRFile(new File(PROJECT_FOLDER_PATH + "/" + DEV_FOLDER_NAME), new File(TEMPORARY_FOLDER_PATH + File.separator + DEV_FOLDER_NAME));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createSecondLevelTestOCRFiles() {
        try {
            System.out.println("====CREATING OCR FOR TEST FILES====");
            FileUtilities.createOCRFile(new File(PROJECT_FOLDER_PATH + "/" + DEV_FOLDER_NAME + 2), new File(TEMPORARY_FOLDER_PATH + File.separator + DEV_FOLDER_NAME + 2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to copy text files for training
     */
    public static void createTrainingOCRFiles() {
        System.out.println("CREATING OCR FOR TRAINING FILES");
        try {
            FileUtilities.createOCRFile(new File(PROJECT_FOLDER_PATH + "/" + TRAINING_FOLDER_NAME), new File(TEMPORARY_FOLDER_PATH + "/" + TRAINING_FOLDER_NAME));
        } catch (IOException e) {
            System.out.println("ERROR IN COPYING TRAINING FILES");
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public static void copyDevFile() {
        System.out.println("COPYING DEV FILE");
        FileUtilities.copyDevLabelFile(new File(PROJECT_FOLDER_PATH + File.separator + DEV_LABEL_FILE_NAME), new File(TEMPORARY_FOLDER_PATH + File.separator + DEV_LABEL_FILE_NAME));
        System.out.println("DEV FILE COPIED");
    }

    /**
     *
     */
    public static void copyClassFile() {
        try {
            System.out.println("COPYING CLASS NAMES FILE");
            FileUtils.copyFile(new File(PROJECT_FOLDER_PATH + File.separator + CLASS_NAME_FILE_NAME), new File(TEMPORARY_FOLDER_PATH + File.separator + CLASS_NAME_FILE_NAME));
            System.out.println("CLASS NAME FILE COPIED");
        } catch (IOException e) {
            System.out.println("ERROR IN COPYING CLASS FILE");
            e.printStackTrace();
        }
    }


    public static void moveLetterFiles(File[] categorizedFile, ArrayList<Integer> results) {
        for (int index = 0; index < results.size(); index++) {


            try {
                //System.out.println("MOVING FILE" + categorizedFile[index].getName() +" to "+FIRST_LEVEL_FOLDER_NAMES.get(results.get(index)) );
                FileUtils.copyFile(categorizedFile[index],
                        new File(CATEGORIZED_FILES_PATH
                                + File.separator + "letters"
                                + File.separator + SECOND_LEVEL_FOLDER_NAMES.get(results.get(index))
                                + File.separator + categorizedFile[index].getName()));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for moving the categorized files based on the results.txt
     *
     * @param categorizedFile Array of files
     * @param results
     */
    public static void moveCategorizedFiles(File[] categorizedFile, ArrayList<Integer> results) {
        System.out.println("MOVING FILES");

        File[] OCRFILES = new File(TEMPORARY_FOLDER_PATH + File.separator + DEV_FOLDER_NAME).listFiles((dir) -> dir.getName().toLowerCase().endsWith(".txt"));


        for (int index = 0; index < results.size(); index++) {


            try {


                System.out.println(categorizedFile[index].getName() + "---->" + FIRST_LEVEL_FOLDER_NAMES.get(results.get(index)));
                //if letters
                if (results.get(index) == 0) {

                    FileUtils.copyFile(categorizedFile[index].getAbsoluteFile(),
                            new File(PROJECT_FOLDER_PATH + File.separator + "dev2"
                                    + File.separator + categorizedFile[index].getName()));


                    FileUtils.copyFile(OCRFILES[index],
                            new File(TEMPORARY_FOLDER_PATH
                                    + File.separator + "dev2"
                                    + File.separator + OCRFILES[index].getName()));
                    continue;

                }


                //System.out.println("MOVING FILE" + categorizedFile[index].getName() +" to "+FIRST_LEVEL_FOLDER_NAMES.get(results.get(index)) );
                FileUtils.copyFile(categorizedFile[index],
                        new File(CATEGORIZED_FILES_PATH + File.separator
                                + FIRST_LEVEL_FOLDER_NAMES.get(results.get(index)) + File.separator
                                + categorizedFile[index].getName()));


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
                //kukunin ang fileName
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
        File mainFolder = new File(CATEGORIZED_FILES_PATH);
        mainFolder.mkdir();

        File tempFolder = new File(TEMPORARY_FOLDER_PATH);
        tempFolder.mkdir();

        //folders for the categorized documents root directory

        for (String currentFolderName : FIRST_LEVEL_FOLDER_NAMES.values()) {
            File file = new File(CATEGORIZED_FILES_PATH + File.separator + currentFolderName);
            file.mkdir();
        }
        System.out.println("FOLDERS CREATED");

    }

    /**
     * get the categories of the folder for training
     */
    public static void getFirstLevelTrainingFolderNames() {
        //ArrayList<String> pathNames = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(PROJECT_FOLDER_PATH
                    + File.separator + TRAINING_FOLDER_NAME
                    + File.separator + FIRST_LEVEL_FOLDER_NAME
                    + File.separator + CLASS_NAME_FILE_NAME));

            String line;
            while ((line = br.readLine()) != null) {
                String path = line.split(" ")[1];
                path = path.replace(".", File.separator);
                path += File.separator;

                FIRST_LEVEL_FOLDER_NAMES.put(Integer.parseInt(line.split(" ")[0]), path);
            }

            FIRST_LEVEL_FOLDER_NAMES.put(FIRST_LEVEL_FOLDER_NAMES.size(), "others" + File.separator);

        } catch (FileNotFoundException e) {
            System.out.println("CLASS FILE NOT FOUND");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR IN CLASS FILE");
            e.printStackTrace();
        }

    }

    public static void getSecondLevelTrainingFolderNames() {
        //ArrayList<String> pathNames = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(PROJECT_FOLDER_PATH
                    + File.separator + TRAINING_FOLDER_NAME
                    + File.separator + SECOND_LEVEL_FOLDER_NAME
                    + File.separator + CLASS_NAME_FILE_NAME));
            String line;
            while ((line = br.readLine()) != null) {
                String path = line.split(" ")[1];
                path = path.replace(".", File.separator);
                path += File.separator;

                SECOND_LEVEL_FOLDER_NAMES.put(Integer.parseInt(line.split(" ")[0]), path);
            }
            SECOND_LEVEL_FOLDER_NAMES.put(SECOND_LEVEL_FOLDER_NAMES.size(), "others" + File.separator);

        } catch (FileNotFoundException e) {
            System.out.println("CLASS FILE NOT FOUND");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR IN CLASS FILE");
            e.printStackTrace();
        }

    }


}


