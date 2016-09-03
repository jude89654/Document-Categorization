import java.io.File;
import java.io.IOException;

import liblinear.InvalidInputDataException;
import liblinear.Linear;
import liblinear.Model;
import liblinear.Parameter;
import liblinear.Predict;
import liblinear.Problem;
import liblinear.SolverType;

public class LiblinearSample
{
	public static void main( String[] args ) throws IOException, InvalidInputDataException
	{
		Linear.disableDebugOutput();
		File file = new File( "va_train" );
		Problem prob = Problem.readFromFile( file, 1 );

		SolverType solver = SolverType.L2R_LR; // -s 0
		double C = 1; // cost of constraints violation
		double eps = 0.0001; // stopping criteria

		Parameter parameter = new Parameter( solver, C, eps );
		Model model = Linear.train( prob, parameter );
		File modelFile = new File( "model" );
		model.save( modelFile );

		String[] param = new String[ 3 ];
		param[ 0 ] = "va_test";
		param[ 1 ] = "model";
		param[ 2 ] = "output";
		Linear.enableDebugOutput();
		Predict.main( param );
	}
}
