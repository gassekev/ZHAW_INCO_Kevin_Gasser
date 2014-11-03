package zhaw;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LZ77Main {

	public static final boolean LDEBUG = true;

	/**
	 * Program arguments:
	 * 	LZ77Main [-e <input_text_file_name> ] | [-d <file_for_decoding.LZ77>]
	 * 		-e is the input text file for encoding
	 * 		-d decode a file based on LZ77 encoding and generate the relevant .decoded file
	 * */

	public static void main(String[] args) 
	{
		ProgramParameterParser ppp = new ProgramParameterParser();
		try {
			/// past the program parameters
			ppp.parse( args);

			if ( ppp.hasOption("e")) /// encode the text file based on LZ77 algorithm
				LZ77Encoder.encode( ppp.getOptionValue( "e"));
			else if ( ppp.hasOption("d")) /// decode the encoded with LZ77 file to a text file 
				LZ77Encoder.decode( ppp.getOptionValue( "d"));
			else
			   	throw new UserErrorException("expected options: -e or -d with the path to the file for encoding or decoding");
		} catch (UserErrorException uex) {
			System.err.println("Error: " + uex.getMessage());
		}
			
	}

}
