package file_managers;

public class BlocksFileManager extends FileManager {
	public BlocksFileManager(String path, String plansDir, String problemDir) {
		super(path, plansDir, problemDir);
	}
	
	@Override
	public String getShortRepresentation(String str) {
		String shortStr = str.toUpperCase();
		shortStr = shortStr.replaceAll("CLEAR", "C");
		shortStr = shortStr.replaceAll("ONTABLE", "OT");
		shortStr = shortStr.replaceAll("ON", "O");
		shortStr = shortStr.replaceAll("HOLDING", "H");
		shortStr = shortStr.replaceAll("HANDEMPTY", "HE");
		return shortStr;
	}
}
