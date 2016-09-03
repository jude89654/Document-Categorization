package liblinear;

public final class Parameter
{

	double C;

	/** stopping criteria */
	double eps;

	int max_iters = 1000; // maximal iterations

	SolverType solverType;

	double[] weight = null;

	int[] weightLabel = null;

	double p = 0.1;

	public Parameter ( SolverType solver, double C, double eps )
	{
		setSolverType( solver );
		setC( C );
		setEps( eps );
	}

	public Parameter ( SolverType solverType, double C, double eps, double p )
	{
		setSolverType( solverType );
		setC( C );
		setEps( eps );
		setP( p );
	}

	public Parameter ( SolverType solverType, double C, double eps, int max_iters, double p )
	{
		setSolverType( solverType );
		setC( C );
		setEps( eps );
		setMaxIters( max_iters );
		setP( p );
	}

	public Parameter ( SolverType solver, double C, int max_iters, double eps )
	{
		setSolverType( solver );
		setC( C );
		setEps( eps );
		setMaxIters( max_iters );
	}

	public double getC()
	{
		return C;
	}

	public double getEps()
	{
		return eps;
	}

	public int getMaxIters()
	{
		return max_iters;
	}

	/**
	 * the number of weights
	 * 
	 * @see #setWeights(double[], int[])
	 */
	public int getNumWeights()
	{
		if ( weight == null )
			return 0;
		return weight.length;
	}

	public double getP()
	{
		return p;
	}

	public SolverType getSolverType()
	{
		return solverType;
	}

	/**
	 * @see #setWeights(double[], int[])
	 */
	public int[] getWeightLabels()
	{
		return Linear.copyOf( weightLabel, weightLabel.length );
	}

	/**
	 * @see #setWeights(double[], int[])
	 */
	public double[] getWeights()
	{
		return Linear.copyOf( weight, weight.length );
	}

	/**
	 * C is the cost of constraints violation. (we usually use 1 to 1000)
	 */
	public void setC( double C )
	{
		if ( C <= 0 )
			throw new IllegalArgumentException( "C must not be <= 0" );
		this.C = C;
	}

	/**
	 * eps is the stopping criterion. (we usually use 0.01).
	 */
	public void setEps( double eps )
	{
		if ( eps <= 0 )
			throw new IllegalArgumentException( "eps must not be <= 0" );
		this.eps = eps;
	}

	public void setMaxIters( int iters )
	{
		if ( iters <= 0 )
			throw new IllegalArgumentException( "max iters not be <= 0" );
		this.max_iters = iters;
	}

	/**
	 * set the epsilon in loss function of epsilon-SVR (default 0.1)
	 */
	public void setP( double p )
	{
		if ( p < 0 )
			throw new IllegalArgumentException( "p must not be less than 0" );
		this.p = p;
	}


	public void setSolverType( SolverType solverType )
	{
		if ( solverType == null )
			throw new IllegalArgumentException( "solver type must not be null" );
		this.solverType = solverType;
	}

	/**
	 * <p>
	 * nr_weight, weight_label, and weight are used to change the penalty for some classes (If the weight for a class is not changed, it is set to 1). This is useful for training classifier using unbalanced input data or with asymmetric misclassification cost.
	 * </p>
	 *
	 * <p>
	 * Each weight[i] corresponds to weight_label[i], meaning that the penalty of class weight_label[i] is scaled by a factor of weight[i].
	 * </p>
	 *
	 * <p>
	 * If you do not want to change penalty for any of the classes, just set nr_weight to 0.
	 * </p>
	 */
	public void setWeights( double[] weights, int[] weightLabels )
	{
		if ( weights == null )
			throw new IllegalArgumentException( "'weight' must not be null" );
		if ( weightLabels == null || weightLabels.length != weights.length )
			throw new IllegalArgumentException( "'weightLabels' must have same length as 'weight'" );
		this.weightLabel = Linear.copyOf( weightLabels, weightLabels.length );
		this.weight = Linear.copyOf( weights, weights.length );
	}
}
