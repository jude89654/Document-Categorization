package com.ust.SVM;

import java.io.IOException;

import liblinear.InvalidInputDataException;
import liblinear.Linear;
import liblinear.Predict;

/**
 *
 */
public class Test
{
    /**
     * A String to the path of the dev_label.txt which consists of the correct categories of the test documents
     */
    static String pathOfClassName = "tempFolder/dev_label.txt";

    /**
     * A String of the path to the testData
      */
    static String pathToTestData ="tempFolder/dev";

    /**
     * A method used to start the classification of the System
     * @throws IOException
     * @throws InvalidInputDataException
     */
	public static void StartTesting() throws IOException, InvalidInputDataException
	{

		long start = System.currentTimeMillis();
		Classifier classifier = new Classifier();
		classifier.buildTestFile( pathOfClassName, pathToTestData );
		long end = System.currentTimeMillis();
		System.out.println( "Time taken to build StartTesting file= " + (end - start) + " msec" );

		start = System.currentTimeMillis();
		Linear.disableDebugOutput();
		String[] param = new String[ 3 ];
		param[ 0 ] = classifier.OUTPUT_TEST_FILE;
		param[ 1 ] = classifier.OUTPUT_MODEL_FILE;
		param[ 2 ] = classifier.OUTPUT_RESULT_FILE;
		Linear.enableDebugOutput();
		Predict.main( param );
		end = System.currentTimeMillis();
		System.out.println( "Time taken to classify = " + (end - start) + " msec" );
		classifier.writeResultFile();
	}
}
