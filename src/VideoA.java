import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class VideoA
{

	public static void main( String[] args ) throws IOException
	{
		File file = new File( "va.txt" );
		Scanner in = new Scanner( file );

		File train_file = new File( "va_train" );
		train_file.createNewFile();

		FileWriter fw = new FileWriter( train_file.getAbsoluteFile() );
		BufferedWriter bw = new BufferedWriter( fw );

		boolean is_bra_open = false;
		boolean is_bra_closed = false;
		int j = 1;

		while ( in.hasNextLine() )
		{
			String line = in.nextLine();
			if ( line.contains( "[" ) )
				is_bra_open = true;
			if ( line.contains( "]" ) )
				is_bra_closed = true;

			String[] tokens = line.replace( "[", "," ).replaceAll( "\\s+", "" ).replaceAll( ";", "" ).replace( "]", "" ).split( "," );

			for ( int i = 0; i < tokens.length; i++ )
			{
				if ( is_bra_open )
				{
					bw.write( tokens[ i ] );
					is_bra_open = false;
				}
				else
				{
					bw.write( " " + j++ + ":" + tokens[ i ] );
					if ( is_bra_closed && i == tokens.length - 1 )
					{
						bw.write( "\n" );
						is_bra_closed = false;
						j = 1;
					}
				}
			}
		}
		in.close();
		bw.close();
	}

}
