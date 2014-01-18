package wiggler2;


//
//[1:29:39 PM] Wade Hought: load it with:
//[1:29:44 PM] Wade Hought: CmdLine.load( args );
//[1:29:50 PM] Wade Hought: where args is what comes into main( ... )
//[1:30:02 PM] Wade Hought: then you can do things like
//[1:30:10 PM] Wade Hought: if ( CmdLine.get( "debug" ) != null ) {
//[1:30:34 PM] Wade Hought: or
//[1:30:37 PM] Steven Feltner: this is your code?
//[1:30:38 PM] Wade Hought: String passphrase = CmdLine.get( "passphrase" );
//[1:30:42 PM] Wade Hought: yes, my code
//[1:31:06 PM] Wade Hought: it basically just parses the cmdline into a bunch 
//             of name=value pairs
//[1:31:12 PM] Wade Hought: and dumps them into a hashmap
//[1:31:46 PM] Wade Hought: then does some singleton/static stuff so the values 
//             are simple to pull from anywhere in the code - vs having to pass 
//             args all over creation to have access to them

import java.util.*;


public class CmdLine {
	private HashMap<String, Object> argMap;
	
	//
	// singleton this object... main reason for this is so that a main object can
	// set it up, but then not have to pass it around to other methods that may 
	// need to access cmdline args.  Simple static methods to access, done.
	private static CmdLine	singleton;	
	private CmdLine() {		// no external instantiations of the object allowed, just internal
		argMap = new HashMap<String, Object>();
		
	}	
	@Override
	public Object clone() throws CloneNotSupportedException {		// don't let anyone clone the object
		throw new CloneNotSupportedException();
	}
	private static synchronized CmdLine init() {	// create/get the singleton
		if ( singleton == null ) {
			singleton = new CmdLine();
		}
		return singleton;
	}
	
	// load an array of arguments of the form:  -paramname:value   into the hashmap for retrieval
	public static void load( String[] args ) throws java.lang.IllegalArgumentException {
		CmdLine	cl = CmdLine.init();
		
		String	arg, key, value;
		int		offset, filecount = 0;

		for ( int i=0 ; i<args.length ; i++ ) {
			arg = args[i];
			if ( arg.indexOf( "?" ) >= 0 ) {
				throw new IllegalArgumentException( "Displaying help" );
			}
			else if ( arg.substring( 0, 1 ).equals( "-" ) ) {
				offset = arg.indexOf( "=" );
				if ( offset == -1 ) {
					offset = arg.indexOf( ":" );
				}

				if ( offset >= 1 ) {
					// this is an option of the type "-option:value" or "-option=value"
					key = arg.substring( 0, offset ).substring( 1 );	// remove the '-' too
					value = arg.substring( offset+1 );

					
					cl.argMap.put(key, value);
					
				}
				else {
					// this is a plain flag-type option, "-debug"
					key = arg.substring( 1 );
					cl.argMap.put(key,  "true");
					
				}
			}
			else {
				// this is a standalone option without a '-', treat it as a filename and
				// store it in the map with a special key
				cl.argMap.put( "filename" + String.valueOf( filecount ), arg );
				filecount++;
			}

		}
	}

	public static void setDefaultValue( String name, String defaultValue ) {
		CmdLine	cl = CmdLine.init();

		if ( ! cl.argMap.containsKey( name ) ) {
			cl.argMap.put( name, defaultValue );
		}
	}
	
	public static <E> Set<String> keySet(){
		return CmdLine.init().argMap.keySet();
	}
	
	public static String get( String key ) {
		return (String) CmdLine.init().argMap.get( key );
	}
//	public static String getString( String key ) {
//		return CmdLine.init().argMap.get( key );
//	}
//	public static Integer getInteger( String key ) {
//		return (Integer) CmdLine.init().argMap.get( key );
//	}
	public static void put( String key, String value ) {
		CmdLine.init().argMap.put( key, value );
	}


}