package file_managers;

public class DepotsFileManager extends FileManager {
	public DepotsFileManager(String path, String plansDir, String problemDir) {
		super(path, plansDir, problemDir);
	}
	
	@Override
	public String getShortRepresentation(String str) {
		String shortStr = str.toUpperCase();
		// Delete predicats that stays the same no matter what action are performed. 
		shortStr = shortStr.replaceAll("AT HOIST\\d* DEPOT\\d*, ", "");
		shortStr = shortStr.replaceAll("AT HOIST\\d* DISTRIBUTOR\\d*, ", "");
		shortStr = shortStr.replaceAll("AT PALLET\\d* DEPOT\\d*, ", "");
		shortStr = shortStr.replaceAll("AT PALLET\\d* DISTRIBUTOR\\d*, ", "");
		
		// Create a shorter representation of all the pradicates.
		shortStr = shortStr.replaceAll("CRATE", "CR");
		shortStr = shortStr.replaceAll("CLEAR", "C");
		shortStr = shortStr.replaceAll("AVAILABLE", "AV");
		shortStr = shortStr.replaceAll("LIFTING", "L");
		shortStr = shortStr.replaceAll("AT", "A");
		shortStr = shortStr.replaceAll("ON", "O");
		shortStr = shortStr.replaceAll("IN", "I");
		
		shortStr = shortStr.replaceAll("PALLET", "P");
		shortStr = shortStr.replaceAll("DEPOT", "D");
		
		shortStr = shortStr.replaceAll("DISTRIBUTOR", "DI");
		shortStr = shortStr.replaceAll("HOIST", "H");
		shortStr = shortStr.replaceAll("TRUCK", "T");
		return shortStr;
	}
}
