package liblinear;

// origin: tron.h
interface Function
{

	double fun( double[] w );

	int get_nr_variable();

	void grad( double[] w, double[] g );

	void Hv( double[] s, double[] Hs );
}
