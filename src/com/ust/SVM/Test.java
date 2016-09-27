package com.ust.SVM;

import java.io.File;
import java.io.IOException;

import com.ust.main.Main;
import liblinear.InvalidInputDataException;
import liblinear.Linear;
import liblinear.Predict;

/**
 *
 */
public class Test {
    /**
     * A String to the path of the dev_label.txt which consists of the correct categories of the test documents
     */
    static String pathOfFirstLevelClassName = Main.PROJECT_FOLDER_PATH
            +File.separator+Main.DEV_LABEL_FILE_NAME;

    /**
     * A String of the path to the testData
     */
    static String pathOfFirstLevelTestData = Main.TEMPORARY_FOLDER_PATH
            +File.separator+Main.DEV_FOLDER_NAME;

    static String pathOfSecondLevelTestData = Main.TEMPORARY_FOLDER_PATH
            +File.separator+Main.DEV_FOLDER_NAME+2;

    /**
     * A method used to start the classification of the System
     *
     * @throws IOException
     * @throws InvalidInputDataException
     */
    public static void StartFirstLevelTesting() throws IOException, InvalidInputDataException {

        long start = System.currentTimeMillis();
        FirstLevelClassifier firstLevelClassifier = new FirstLevelClassifier();
        firstLevelClassifier.buildTestFile(pathOfFirstLevelClassName, pathOfFirstLevelTestData);
        long end = System.currentTimeMillis();
        System.out.println("Time taken to build StartFirstLevelTesting file= " + (end - start) + " msec");

        start = System.currentTimeMillis();
        Linear.disableDebugOutput();
        String[] param = new String[3];
        param[0] = firstLevelClassifier.OUTPUT_TEST_FILE;
        param[1] = firstLevelClassifier.OUTPUT_MODEL_FILE;
        param[2] = FirstLevelClassifier.OUTPUT_RESULT_FILE;
        Linear.enableDebugOutput();
        Predict.main(param);
        end = System.currentTimeMillis();
        System.out.println("Time taken to classify = " + (end - start) + " msec");
        firstLevelClassifier.writeResultFile();
    }

    //TODO
    public static void StartSecondLevelTesting() throws IOException, InvalidInputDataException {
        long start = System.currentTimeMillis();
        SecondLevelClassifier secondLevelClassifier = new SecondLevelClassifier();
        secondLevelClassifier.buildTestFile(pathOfFirstLevelClassName, pathOfSecondLevelTestData);
        long end = System.currentTimeMillis();
        System.out.println("Time taken to build StartFirstLevelTesting file= " + (end - start) + " msec");

        start = System.currentTimeMillis();
        Linear.disableDebugOutput();
        String[] param = new String[3];
        param[0] = secondLevelClassifier.OUTPUT_TEST_FILE;
        param[1] = secondLevelClassifier.OUTPUT_MODEL_FILE;
        param[2] = secondLevelClassifier.OUTPUT_RESULT_FILE;
        Linear.enableDebugOutput();
        Predict.main(param);
        end = System.currentTimeMillis();
        System.out.println("Time taken to classify = " + (end - start) + " msec");
        secondLevelClassifier.writeResultFile();
    }
}
