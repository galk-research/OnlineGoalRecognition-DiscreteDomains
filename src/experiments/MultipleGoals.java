package experiments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import file_managers.*;
import javaff.data.Action;
import javaff.data.Plan;
import javaff.planning.STRIPSState;
import javaff.search.UnreachableGoalException;
import recognizer.ComplexGoal;
import recognizer.GoalRecognitionResult;
import recognizer.MultiGoalRecognizer;
import recognizer.OnlineGoalRecognitionMirroringWithLandmarks;
import recognizer.OnlineGoalRecognitionNaive;


public class MultipleGoals{

	public static void main(String[] args)  {		
		try {
			
			if(args.length >= 2){
				String goalRecognitionFile = args[0];
				String planFilesPath = args[1];
				FileManager fileManager;
				
				if (args.length > 2){
					String domain = args[2];
					fileManager = FileManagerFactory.getFileManager(domain, planFilesPath, "plan_files", "problem_files");  
				} else {
					fileManager = new FileManager(planFilesPath, "plan_files", "problem_files");
				}
				
				MultiGoalRecognizer  recognizer = new MultiGoalRecognizer(goalRecognitionFile, fileManager);
				
				
				String allParams = Arrays.asList(args).toString(); 
				if (allParams.contains("plan-only")) {
					makePlan(fileManager, recognizer, goalRecognitionFile);
				}else {
					try {
						GoalRecognitionResult rst = recognizer.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Done!");
	}
	
	private static void printUsage(){
		System.out.println("- Option - Parameters needed: < -online | -offline > < tar.bz2 file > < threshold_value >");
		System.out.println("\t $> Example: -online experiments/logistics/100/logistics_p01_hyp-1_full.tar.bz2 0.0");
	}
	
	private static void makePlan(FileManager fileManager, MultiGoalRecognizer  recognizer, String goalRecognitionFile) {
		Plan p;
		try {
			p = recognizer.makePlan();
			System.out.println(p.getGoal());
			System.out.println(p);
			
			String obsFileName = goalRecognitionFile.replace(".rar", "\\obs.dat");
			
			try {
				File myObj = new File(obsFileName);
				myObj.createNewFile();
			    FileWriter myWriter = new FileWriter(obsFileName);
			    
			    for(Action action : p.getActions()){
			    	myWriter.write(action.toString());
			    	myWriter.write("\n");
			    }
			    
			    myWriter.close();
			} catch (IOException e) {
				System.out.println("Could not find file." + obsFileName);
				e.printStackTrace();
			}
		}catch (UnreachableGoalException e) {
			e.printStackTrace();
		}
	}
	

}

