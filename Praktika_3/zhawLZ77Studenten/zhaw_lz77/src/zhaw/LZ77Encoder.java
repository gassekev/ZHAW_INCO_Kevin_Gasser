package zhaw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LZ77Encoder {

	public static final boolean LDEBUG = false;

	public static char NO_MORE_CHARS = 0;
	
	public static final int LOOK_AHEAD_BUF_LEN = 32760;
	public static final int START_BUF_LEN = 32760;
	/*
	 * // in case the buffer is less than 254 in size 
	public static final int LOOK_AHEAD_BUF_LEN = 125; // read and write has to be with bytes // not more than 125 or you have to change all the data types
	public static final int START_BUF_LEN = 125;	 // read and write has to be with bytes // not more than 125 or you have to change all the data types
	*/
	public static final int TOTAL_BUF_LEN = START_BUF_LEN + LOOK_AHEAD_BUF_LEN;
	
	public static final String FILE_ENC_EXTENTION = ".lz77encoded";
	public static final String FILE_DEC_EXTENTION = ".lz77decoded";
	
	public static void encode( String filePath) throws UserErrorException 
	{
	    if ( LDEBUG ) {
	    	System.out.println("Current directory is: " + System.getProperty("user.dir"));
		}
	    
		try ( 	BufferedReader in = new BufferedReader( new FileReader( filePath));
				DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath + FILE_ENC_EXTENTION))
				)
		{	    
			System.out.println("Encoding " + filePath);

		    StringBuffer strBuf = new StringBuffer( TOTAL_BUF_LEN);
			
			int c;
			while ((c = in.read()) != -1 || strBuf.length() > START_BUF_LEN)   //   reads a single character from the input file
			{
				// fill the buffer if there is something for feeling
				if ( c != -1 && strBuf.length() < TOTAL_BUF_LEN )
				{
					strBuf.append( (char)c);
					if ( strBuf.length() <= START_BUF_LEN ) 
					{
						// write to the output file the starting string
						writeEncData( new FoundStringEncData(0, 0, (char)c), out);
					}
					// still to less data in
					if ( strBuf.length() < TOTAL_BUF_LEN )
						continue;
				}
				
				if (LDEBUG) System.out.println("BUFFER:   '" + strBuf.substring(0, START_BUF_LEN) + "'"+ strBuf.substring(START_BUF_LEN) + "'" );
				/* 
				 * ToDo: [1.1] implement: finding a matching string with the maximum length and writing the relevant encoded information to the file.
				 * */
				throw new NotImplementedException();
			}
			out.flush();
			System.out.println("Encoding successfully finished.");
		} catch (FileNotFoundException ex)
		{
			throw new UserErrorException( "input file " + filePath + " can not be find.");
		} catch (IOException e) {
			throw new UserErrorException( "file related to " + filePath + FILE_ENC_EXTENTION + " writting failed.");
		}
		
	}

	private static void writeEncData( FoundStringEncData encData, DataOutputStream out) throws IOException {
		out.writeShort(encData.offset);
		out.writeShort(encData.charsIn);
		/*
		 * // in case the buffer is less than 254 in size 
		out.writeByte(encData.offset);
		out.writeByte(encData.charsIn);
		*/
		out.writeByte(encData.nextChar);
	}

	private static FoundStringEncData findTheLongestMatchingString( StringBuffer strBuf) {
		FoundStringEncData res = new FoundStringEncData();
		
		// try to find the longest string which has to start form the start buffer area
		for ( int l = 1; l <= LOOK_AHEAD_BUF_LEN; ++l )
		{
			String lookingFor = strBuf.substring( START_BUF_LEN, START_BUF_LEN + l);
			int foundOn = strBuf.indexOf( lookingFor);
			if ( foundOn < 0 || foundOn >= START_BUF_LEN)
				break;
			
			res.offset = START_BUF_LEN - foundOn;
			res.charsIn = l;
			if ( strBuf.length() <= START_BUF_LEN + l)
			{
				res.nextChar = NO_MORE_CHARS;
			    break;
			}
			res.nextChar = strBuf.charAt( START_BUF_LEN + l);
		}
		
		if ( res.isEmpty() )
			res.nextChar = strBuf.charAt( START_BUF_LEN);
		
		if (LDEBUG) System.out.println("   ED:   " + res.offset + ",  " + res.charsIn + ", " + res.nextChar );
		return res;
	}

	public static void decode( String filePath) throws UserErrorException 
	{
	    if ( LDEBUG ) {
	    	System.out.println("Current directory is: " + System.getProperty("user.dir"));
		}
		String decodedFilePath = "";
		if ( filePath.endsWith( FILE_ENC_EXTENTION) )
			decodedFilePath = filePath.substring( 0, filePath.length() - FILE_ENC_EXTENTION.length()) + FILE_DEC_EXTENTION;
		else
		{
			decodedFilePath = filePath + FILE_DEC_EXTENTION;
			filePath += FILE_ENC_EXTENTION;
		}	    
		try ( 	DataInputStream in = new DataInputStream(new FileInputStream( filePath));
				BufferedWriter out = new BufferedWriter(new FileWriter( decodedFilePath))
				)
		{	
			System.out.println("Decoding " + filePath);

		    StringBuffer strBuf = new StringBuffer( 3 * TOTAL_BUF_LEN);
			
			while ( true)   //   reads a single byte from the encoded file
			{
				// deserialize the encoded data to the object
				FoundStringEncData encData = new FoundStringEncData();
				encData.offset = in.readShort();
				encData.charsIn = in.readShort();
				/*
				 * // in case the buffer is less than 254 in size 
				encData.offset = in.readByte();
				encData.charsIn = in.readByte();
				*/
				encData.nextChar = (char)in.readByte();
				
				// fill into the buffer with decoded data
				/* 
				 * ToDo: [1.2] implement: decode the data and put it into the strBuf.
				 * */
				throw new NotImplementedException();
				
				// flush part of the buffer in the decoded file in order to do not make it super big
				if ( strBuf.length() > 2 * TOTAL_BUF_LEN )
				{
					int tillWhereToFlushIntoTheFile = strBuf.length() - TOTAL_BUF_LEN;
					out.write( strBuf.substring( 0,  tillWhereToFlushIntoTheFile) );
					strBuf.delete( 0, tillWhereToFlushIntoTheFile);
				}
			}
			
			// flush the rest of the buffer
			if ( strBuf.length() > 0 )
				out.write( strBuf.toString() );
			
			out.flush();
			System.out.println("Decoded file " + decodedFilePath + " was generated.");
		} catch ( EOFException ex)
		{
			System.out.println("Reading encoded file complates, but probably the decoding does not work correctly.");
		} catch (FileNotFoundException ex)
		{
			throw new UserErrorException( "input file " + filePath + " can not be find.");
		} catch (IOException e) {
			throw new UserErrorException( "file related to " + filePath + FILE_ENC_EXTENTION + " writting failed.");
		}
		
	}
}
