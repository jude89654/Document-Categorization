package com.ust.SVM;

import java.io.File;
import java.io.IOException;

import com.ust.main.Main;
import liblinear.InvalidInputDataException;
import liblinear.Linear;
import liblinear.Model;
import liblinear.Parameter;
import liblinear.Problem;
import liblinear.SolverType;

/**
 * A Class that will be used in creating a model file for each of the document categories
 */
public class Train
{
	/**
	 * Method used to create a model based on the training data of each category
	 * @throws IOException
	 * @throws InvalidInputDataException
	 */
	public static void firstLevelTraining() throws IOException, InvalidInputDataException
	{

	    String pathToClassName= Main.PROJECT_FOLDER_PATH+File.separator+Main.TRAINING_FOLDER_NAME+ File.separator+Main.FIRST_LEVEL_FOLDER_NAME +File.separator+Main.CLASS_NAME_FILE_NAME;
        String pathToTrainingFolder = Main.TEMPORARY_FOLDER_PATH +File.separator+Main.TRAINING_FOLDER_NAME+File.separator+Main.FIRST_LEVEL_FOLDER_NAME;

		long start = System.currentTimeMillis();
		FirstLevelClassifier firstLevelClassifier = new FirstLevelClassifier();
		firstLevelClassifier.buildTrainData( pathToClassName, pathToTrainingFolder);

		long end = System.currentTimeMillis();
		System.out.println( "Time taken to build firstLevelTraining file = " + (end - start) + " msec" );
		Linear.disableDebugOutput();
		start = System.currentTimeMillis();
		File file = new File( firstLevelClassifier.OUTPUT_TRAIN_FILE );
		Problem prob = Problem.readFromFile( file, 1 );

		SolverType solver = SolverType.L2R_L2LOSS_SVC; // -s 0
		double C = 0.006;// cost of constraints violation
		double eps = 0.001; // stopping criteria

		Parameter parameter = new Parameter( solver, C, eps );
		Model model = Linear.train( prob, parameter );
		File modelFile = new File( firstLevelClassifier.OUTPUT_MODEL_FILE );
		model.save( modelFile );
		System.out.println( "Finished writing model file - " + firstLevelClassifier.OUTPUT_MODEL_FILE );
		end = System.currentTimeMillis();
		System.out.println( "Time taken to create the firstLevelTraining SVM = " + (end - start) + " msec" );
	}

	public static void secondLevelTraining() throws IOException, InvalidInputDataException
	{

	    String pathToClassName= Main.PROJECT_FOLDER_PATH+File.separator+Main.TRAINING_FOLDER_NAME+ File.separator+Main.SECOND_LEVEL_FOLDER_NAME +File.separator+Main.CLASS_NAME_FILE_NAME;
        String pathToTrainingFolder = Main.TEMPORARY_FOLDER_PATH +File.separator+Main.TRAINING_FOLDER_NAME+File.separator+Main.SECOND_LEVEL_FOLDER_NAME;

		long start = System.currentTimeMillis();
		SecondLevelClassifier secondLevelClassifier = new SecondLevelClassifier();
		secondLevelClassifier.buildTrainData( pathToClassName, pathToTrainingFolder);

		long end = System.currentTimeMillis();
		System.out.println( "Time taken to build secondLevelTraining file = " + (end - start) + " msec" );
		Linear.disableDebugOutput();
		start = System.currentTimeMillis();
		File file = new File( secondLevelClassifier.OUTPUT_TRAIN_FILE );
		Problem prob = Problem.readFromFile( file, 1 );

		SolverType solver = SolverType.L2R_L2LOSS_SVC; // -s 0
		double C = 0.006;// cost of constraints violation
		double eps = 0.001; // stopping criteria

		Parameter parameter = new Parameter( solver, C, eps );
		Model model = Linear.train( prob, parameter );
		File modelFile = new File( secondLevelClassifier.OUTPUT_MODEL_FILE );
		model.save( modelFile );
		System.out.println( "Finished writing model file - " + secondLevelClassifier.OUTPUT_MODEL_FILE );
		end = System.currentTimeMillis();
		System.out.println( "Time taken to  create the SecondLevelTraining SVM = " + (end - start) + " msec" );
	}

}
