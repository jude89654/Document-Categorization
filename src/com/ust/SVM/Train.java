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
	public static void startTraining() throws IOException, InvalidInputDataException
	{

	    String pathToClassName= Main.tempFolderPath +File.separator+Main.classNameFile;
        String pathToTrainingFolder = Main.tempFolderPath +File.separator+Main.trainingFolderName;

		long start = System.currentTimeMillis();
		FirstLevelClassifier firstLevelClassifier = new FirstLevelClassifier();
		firstLevelClassifier.buildTrainData( pathToClassName, pathToTrainingFolder);

		long end = System.currentTimeMillis();
		System.out.println( "Time taken to build startTraining file = " + (end - start) + " msec" );
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
		System.out.println( "Time taken to startTraining SVM = " + (end - start) + " msec" );
	}

}
