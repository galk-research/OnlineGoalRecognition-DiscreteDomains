package file_managers;

public class CampusFileManager extends FileManager {
	public CampusFileManager (String path, String plansDir, String problemDir) {
		super(path, plansDir, problemDir);
	}
	
	@Override
	public String getShortRepresentation(String str) {
		String shortStr = str.toUpperCase();
		shortStr = shortStr.replaceAll("GROUP-MEETING-", "GM");
		shortStr = shortStr.replaceAll("LECTURE-", "L");
		shortStr = shortStr.replaceAll("-TAKEN", "T");
		shortStr = shortStr.replaceAll("BANKING", "BN");
		shortStr = shortStr.replaceAll("BREAKFAST", "BR");
		shortStr = shortStr.replaceAll("COFFEE", "C");
		shortStr = shortStr.replaceAll("LUNCH", "LU");
		
		// Places names.
		shortStr = shortStr.replaceAll("WATSON_THEATER", "W_TH");
		shortStr = shortStr.replaceAll("HAYMAN_THEATER", "H_TH");
		shortStr = shortStr.replaceAll("JONES_THEATER", "J_TH");
		shortStr = shortStr.replaceAll("DAVIS_THEATER", "D_TH");
		shortStr = shortStr.replaceAll("BOOKMARK_CAFE", "B_CF");
		shortStr = shortStr.replaceAll("ANGAZI_CAFE", "A_CF");
		shortStr = shortStr.replaceAll("LIBRARY", "LIB");
		shortStr = shortStr.replaceAll("PSYCHOLOGY_BLDG", "P_BLD");
		
		return shortStr;
	}
}
