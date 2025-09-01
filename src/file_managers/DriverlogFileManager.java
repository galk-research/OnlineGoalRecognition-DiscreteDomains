package file_managers;

public class DriverlogFileManager extends FileManager {
	public DriverlogFileManager(String path, String plansDir, String problemDir) {
		super(path, plansDir, problemDir);
	}
	
	@Override
	public String getShortRepresentation(String str) {
		String shortStr = str.toUpperCase();
		// Delete predicats that stays the same no matter what action are performed. 
		shortStr = shortStr.replaceAll("\\(LINK[^\\)]*\\)", "");
		shortStr = shortStr.replaceAll("\\(PATH[^\\)]*\\)", "");

		// Create a shorter representation of all the pradicates.
		shortStr = shortStr.replaceAll("OBJ", "O");
		shortStr = shortStr.replaceAll("TRUCK", "T");
		shortStr = shortStr.replaceAll("LOCATION", "L");
		shortStr = shortStr.replaceAll("DRIVER", "D");
		shortStr = shortStr.replaceAll("PACKAGE", "PKG");
		shortStr = shortStr.replaceAll("EMPTY", "E");
		return shortStr;
	}
}

