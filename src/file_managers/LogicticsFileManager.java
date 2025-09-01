package file_managers;

public class LogicticsFileManager extends FileManager {
	public LogicticsFileManager (String path, String plansDir, String problemDir) {
		super(path, plansDir, problemDir);
	}
	
	@Override
	public String getShortRepresentation(String str) {
		String shortStr = str.toUpperCase();
		// Delete predicats that stays the same no matter what action are performed. 
		shortStr = shortStr.replaceAll("\\(IN-CITY AIRPORT\\d* CITY\\d*\\)", "");
		shortStr = shortStr.replaceAll("\\(IN-CITY POS\\d* CITY\\d*\\)", "");
		
		// Create a shorter representation of all the pradicates.
		shortStr = shortStr.replaceAll("CITY", "C");
		shortStr = shortStr.replaceAll("AIRPLANE", "PLN");
		shortStr = shortStr.replaceAll("AIRPORT", "PRT");
		shortStr = shortStr.replaceAll("POS", "P");
		shortStr = shortStr.replaceAll("TRUCK", "T");
		shortStr = shortStr.replaceAll("OBJ", "O");
		return shortStr;
	}
}
