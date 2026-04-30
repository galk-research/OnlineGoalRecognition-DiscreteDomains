package file_managers;

public class FerryFileManager extends FileManager{
	public FerryFileManager (String path, String plansDir, String problemDir) {
		super(path, plansDir, problemDir);
	}
	
	@Override
	public String getShortRepresentation(String str) {
		String shortStr = str.toUpperCase();
		
		// Delete predicats that stays the same no matter what actions are performed.
		shortStr = shortStr.replaceAll("AT HOIST\\d* DEPOT\\d*, ", "");
		
		shortStr = shortStr.replaceAll("\\(LOCATION [^\\)]*\\)", "");
		shortStr = shortStr.replaceAll("\\(CAR [^\\)]*\\)", "");
		shortStr = shortStr.replaceAll("\\(NOT-EQ [^\\)]*\\)", "");
		
		// Create a shorter representation of all the pradicates.
		shortStr = shortStr.replaceAll("AT-FERRY", "AF");
		shortStr = shortStr.replaceAll("EMPTY-FERRY", "E");
		shortStr = shortStr.replaceAll("CAR", "C");
		shortStr = shortStr.replaceAll("LOCATION", "L");
		

		return shortStr;
	}
}
