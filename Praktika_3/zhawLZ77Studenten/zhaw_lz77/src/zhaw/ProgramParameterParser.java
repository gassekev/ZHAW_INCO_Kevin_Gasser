package zhaw;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgramParameterParser {
	
	public static final boolean LDEBUG = false;
	
	private HashMap<String, String> programParams = new HashMap<>();
	
	public void parse( String[] args) throws UserErrorException {
		String currParamValue = null;
	    for (int i = 0; i < args.length; i++) {
	    	switch (args[i].charAt(0)) {
	        case '-':
	        	Matcher matcher = Pattern.compile("\\-+([\\w_]+)").matcher(args[i]);
	        	if ( ! matcher.find() || matcher.groupCount() <= 0 )
	        		throw new UserErrorException("Ivalid argument name: " + args[i]);
	        	programParams.put( currParamValue =  matcher.group(1), null);
	        	continue;
	        default:
	        	if ( currParamValue == null || ! programParams.containsKey( currParamValue) )
	        		throw new UserErrorException("No argument name specified for argument value: " + args[i]);
	        	programParams.put( currParamValue, args[i]);
	        	currParamValue = null;
	        	continue;
	    	}
	    }
	    
	    if ( LDEBUG ) {
	    	System.out.println("Program parameters:   ");
		    for ( String k: programParams.keySet()) {
		    	System.out.println("\t" + k + " : " + programParams.get(k));
		    }
	    }
	}

	boolean hasOption( String optionName) {
		return programParams.containsKey( optionName);
	} 
	
	String getOptionValue( String optionName) throws UserErrorException {
		if ( ! hasOption(optionName))
			return "";
		String optionValue = programParams.get( optionName);
		if ( optionValue == null || optionValue.isEmpty())
			throw new UserErrorException("expected option  '" + optionName + "' with value which is not empty");
		return optionValue;		
	}
}


