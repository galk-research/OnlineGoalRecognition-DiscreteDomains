package file_managers;

public class DWRFileManager extends FileManager{
	public DWRFileManager(String path, String plansDir, String problemDir) {
		super(path, plansDir, problemDir);
	}
	
	@Override
	public String getShortRepresentation(String str) {
		String shortStr = str.toUpperCase();
		// Delete predicats that stays the same no matter what action are performed.
		shortStr = shortStr.replaceAll("\\(ADJACENT[^\\)]*\\)", "");
		shortStr = shortStr.replaceAll("\\(ATTACHED[^\\)]*\\)", "");
		shortStr = shortStr.replaceAll("\\(BELONG[^\\)]*\\)", "");
		
		// Create a shorter representation of all the pradicates.
		shortStr = shortStr.replaceAll("OCCUPIED", "OCC");
		shortStr = shortStr.replaceAll("UNLOADED", "ULD");
		shortStr = shortStr.replaceAll("LOADED", "LDD");
		shortStr = shortStr.replaceAll("HOLDING", "HLD");
		shortStr = shortStr.replaceAll("EMPTY", "E");
		
		
		
		return shortStr;
	}
}
