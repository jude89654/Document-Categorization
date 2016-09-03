import java.io.File;
import java.io.IOException;

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
	public static void train() throws IOException, InvalidInputDataException
	{

	    String pathToClassName="tempFolder/class_name.txt";
        String pathToTrainingFolder = "tempFolder/train";

		long start = System.currentTimeMillis();
		Classifier classifier = new Classifier();
		classifier.buildTrainData( pathToClassName, pathToTrainingFolder);

		long end = System.currentTimeMillis();
		System.out.println( "Time taken to build train file = " + (end - start) + " msec" );
		Linear.disableDebugOutput();
		start = System.currentTimeMillis();
		File file = new File( classifier.OUTPUT_TRAIN_FILE );
		Problem prob = Problem.readFromFile( file, 1 );

		SolverType solver = SolverType.L2R_L2LOSS_SVC; // -s 0
		double C = 0.006;// cost of constraints violation
		double eps = 0.001; // stopping criteria

		Parameter parameter = new Parameter( solver, C, eps );
		Model model = Linear.train( prob, parameter );
		File modelFile = new File( classifier.OUTPUT_MODEL_FILE );
		model.save( modelFile );
		System.out.println( "Finished writing model file - " + classifier.OUTPUT_MODEL_FILE );
		end = System.currentTimeMillis();
		System.out.println( "Time taken to train SVM = " + (end - start) + " msec" );
	}

}
