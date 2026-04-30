package file_managers;

public class FileManagerFactory {
	public static FileManager getFileManager(String domainName, String path, String plansDir, String problemDir) {
		switch (domainName)
		{
			case "Block-World":{
				return new BlocksFileManager(path, plansDir, problemDir);
			}
			case "Campus":{
				return new 	CampusFileManager(path, plansDir, problemDir);
			}
			case "DWR":{
				return new DWRFileManager(path, plansDir, problemDir);
			}
			case "Depots":{
				return new DepotsFileManager(path, plansDir, problemDir);
			}
			case "Ferry":{
				return new FerryFileManager(path, plansDir, problemDir);
			}
			case "DriverLog":{
				return new DriverlogFileManager(path, plansDir, problemDir);
			}
			default:{
				return new FileManager(path, plansDir, problemDir);
			}
				
		}
	}
}
