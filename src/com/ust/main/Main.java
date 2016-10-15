package com.ust.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


import com.ust.SVM.FirstLevelClassifier;
import com.ust.SVM.SecondLevelClassifier;
import com.ust.SVM.Test;
import com.ust.SVM.Train;
import com.ust.ocr.OCR;
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

    public static String SECOND_DEV_FOLDER_NAME = "dev2";

    public static String TRAINING_FOLDER_NAME = "train";

    public static Map<Integer, String> FIRST_LEVEL_FOLDER_NAMES = new HashMap<>();

    public static Map<Integer, String> SECOND_LEVEL_FOLDER_NAMES = new HashMap<>();


    public static void start(String sourceFolderPath, String outputFolderPath, boolean train, boolean test, boolean OCRTest, boolean OCRTrain) {
        PROJECT_FOLDER_PATH = sourceFolderPath;
        CATEGORIZED_FILES_PATH = outputFolderPath + File.separator + CATEGORIZED_FILES_PATH;

        if (train) {
            if (OCRTrain) {
                createFirstLevelTrainingOCRFiles();
                createSecondLevelTrainingOCRFiles();

            }

            try {
                Train.firstLevelTraining();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidInputDataException e) {
                e.printStackTrace();
            }
            try {
                Train.secondLevelTraining();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidInputDataException e) {
                e.printStackTrace();
            }

        }



        if (test) {

            createFirstLevelTestOCRFiles();
            getFirstLevelTrainingFolderNames();
            getSecondLevelTrainingFolderNames();

            try {
                FileUtils.cleanDirectory(new File(PROJECT_FOLDER_PATH + File.separator + SECOND_DEV_FOLDER_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Test.StartFirstLevelTesting();
            } catch (IOException | InvalidInputDataException e) {
                e.printStackTrace();
            }
            File[] originalFiles = new File(PROJECT_FOLDER_PATH + File.separator + DEV_FOLDER_NAME).listFiles(new PictureFileFilter());

            ArrayList<Integer> results = getResults(new File(FirstLevelClassifier.OUTPUT_RESULT_FILE));

            moveCategorizedFiles(originalFiles, results);



            // createSecondLevelTestOCRFiles();

            try {
                Test.StartSecondLevelTesting();
            } catch (IOException | InvalidInputDataException e) {
                e.printStackTrace();
            }

            results = getResults(new File(SecondLevelClassifier.OUTPUT_RESULT_FILE));

            originalFiles = new File(PROJECT_FOLDER_PATH + File.separator + SECOND_DEV_FOLDER_NAME).listFiles(new PictureFileFilter());

            moveLetterFiles(originalFiles, results);


            try {
                FileUtils.cleanDirectory(new File(PROJECT_FOLDER_PATH + File.separator + SECOND_DEV_FOLDER_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Method to create OCR Files on the firstLevelTraining and dev folder
     */
    public static void createFirstLevelTestOCRFiles() {
        System.out.println("====CREATING OCR FOR TEST FILES====");
        //FileUtilities.createOCRFile(new File(PROJECT_FOLDER_PATH + "/" + DEV_FOLDER_NAME), new File(TEMPORARY_FOLDER_PATH + File.separator + DEV_FOLDER_NAME));
        File folderForTesting = new File(PROJECT_FOLDER_PATH + File.separator + DEV_FOLDER_NAME);

        //method to create a thread for each OCR
        Arrays.stream(folderForTesting
                .listFiles(new PictureFileFilter())).parallel()
                .forEach(file -> OCR.createTextFile(file, DEV_FOLDER_NAME));
    }



    /**
     * method to copy text files for training
     */
    public static void createFirstLevelTrainingOCRFiles() {
        System.out.println("CREATING OCR FOR FIRST LEVEL TRAINING FILES");

        //OCR FOR LETTERS
             File firstLevelTrainingFolder = new File(PROJECT_FOLDER_PATH
                     +File.separator+ TRAINING_FOLDER_NAME
                     +File.separator + FIRST_LEVEL_FOLDER_NAME);

        AtomicInteger atomicInteger = new AtomicInteger(0);
       //System.out/.println
        for (File folder : firstLevelTrainingFolder.listFiles(File::isDirectory)) {
            System.out.println(folder.getPath());
            Arrays.stream(folder
                .listFiles(new PictureFileFilter())).parallel()
                .forEach(file ->{
                    final int x = atomicInteger.incrementAndGet();
                    System.out.println("PROCESSING IMAGE: "+x+ " OF " +folder.listFiles().length);
                    OCR.createTextFile(file, TRAINING_FOLDER_NAME+File.separator+ firstLevelTrainingFolder.getName()+File.separator+folder.getName());
                    System.out.println("OCR IMAGE: " + x + " OF "+folder.listFiles().length+" FINISHED");
                });

        }
        System.out.println("FIRST LEVEL OCR FINISHED");

    }


     public static void createSecondLevelTrainingOCRFiles() {
        System.out.println("CREATING OCR FOR SECOND LEVEL TRAINING FILES");

        //OCR FOR LETTERS
             File secondLevelTrainingFolder = new File(PROJECT_FOLDER_PATH
                      +File.separator+TRAINING_FOLDER_NAME
                     +File.separator + SECOND_LEVEL_FOLDER_NAME);

         AtomicInteger atomicInteger = new AtomicInteger(0);
        for (File folder:
             secondLevelTrainingFolder.listFiles(File::isDirectory)) {
            Arrays.stream(folder
                .listFiles(new PictureFileFilter())).parallel()

                .forEach(file ->{
                    final int x = atomicInteger.incrementAndGet();
                    System.out.println("PROCESSING IMAGE: "+x+ " OF " +folder.listFiles().length);OCR.createTextFile(file,  TRAINING_FOLDER_NAME+File.separator+secondLevelTrainingFolder.getName()+File.separator+folder.getName());
                    System.out.println("OCR IMAGE: " + x + " OF "+folder.listFiles().length+" FINISHED");
                });

        }
        System.out.println("SECOND LEVEL OCR FINISHED");


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
     * method to move the files after getting the results from the 2nd level svm
     *
     * @param categorizedFile list of files to be categorized and moved
     * @param results         the result from the 2nd level svm
     */
    public static void moveLetterFiles(File[] categorizedFile, ArrayList<Integer> results) {

        new File(CATEGORIZED_FILES_PATH+File.separator+"letters").mkdirs();

        for (int index = 0; index < results.size(); index++) {

            try {
                System.out.println("MOVING FILE: " + categorizedFile[index].getName() +" ---> "+SECOND_LEVEL_FOLDER_NAMES.get(results.get(index)) );
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
     * @param results         the results from the 1st level svm
     */
    public static void moveCategorizedFiles(File[] categorizedFile, ArrayList<Integer> results) {


        File[] OCRFILES = new File(TEMPORARY_FOLDER_PATH + File.separator + DEV_FOLDER_NAME).listFiles((dir) -> dir.getName().toLowerCase().endsWith(".txt"));


        for (int index = 0; index < results.size(); index++) {

            try {
                System.out.println("MOVING FILE:"+categorizedFile[index].getName() + "---->" + FIRST_LEVEL_FOLDER_NAMES.get(results.get(index)));


                //if the file is letters
                if (results.get(index) == 0) {

                    FileUtils.copyFile(categorizedFile[index].getAbsoluteFile(),
                            new File(PROJECT_FOLDER_PATH + File.separator + SECOND_DEV_FOLDER_NAME
                                    + File.separator + categorizedFile[index].getName()));


                    FileUtils.copyFile(OCRFILES[index],
                            new File(TEMPORARY_FOLDER_PATH
                                    + File.separator + SECOND_DEV_FOLDER_NAME
                                    + File.separator + OCRFILES[index].getName()));

                    //skip to next file
                    continue;

                } else {

                    FileUtils.copyFile(categorizedFile[index],
                            new File(CATEGORIZED_FILES_PATH + File.separator
                                    + FIRST_LEVEL_FOLDER_NAMES.get(results.get(index))
                                    + File.separator + categorizedFile[index].getName()));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * create an ArrayList of integers of the result of the categorization of the current file.
     *
     * @param result the text file given by the svm
     * @return an arraylist of the results :D
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

    /**
     * get the folder names that are the classes of the 2nd level svm
     */
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


    public static void create2ndLevelOCR(){

    }

}


