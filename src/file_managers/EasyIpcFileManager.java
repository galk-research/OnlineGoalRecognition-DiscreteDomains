package file_managers;

public class EasyIpcFileManager extends FileManager {
	public EasyIpcFileManager (String path, String plansDir, String problemDir) {
		super(path, plansDir, problemDir);
	}
	
	@Override
	public String getShortRepresentation(String str) {
		String shortStr = str.toUpperCase();
		
		// Delete predicats that stays the same no matter what actions are performed.
		shortStr = shortStr.replaceAll("\\(CONN[^\\)]*\\)", "");
		
		// Create a shorter representation of all the pradicates.
		shortStr = shortStr.replaceAll("PLACE", "P");
		shortStr = shortStr.replaceAll("KEY", "K");
		shortStr = shortStr.replaceAll("LOCK-SHAPE", "LSH");
		shortStr = shortStr.replaceAll("SHAPE", "SH");
		shortStr = shortStr.replaceAll("OPEN", "O");
		shortStr = shortStr.replaceAll("LOCKED", "L");
		shortStr = shortStr.replaceAll("AT-ROBOT", "AR");
		shortStr = shortStr.replaceAll("CARRING", "CR");

		return shortStr;
	}
}
