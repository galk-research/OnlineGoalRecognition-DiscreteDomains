package experiments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import file_managers.BlocksFileManager;
import file_managers.FileManager;
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
		String basePath = "C:\\Users\\Yifat\\Desktop\\THESIS\\data\\";
		//MultiGoalRecognizer  recognizer = new MultiGoalRecognizer("dataset\\blocks-world\\yifat_experiments\\exp1.rar");
		//String file_name ="C:\\Users\\Yifat\\Documents\\GitHub\\OnlineGoalRecognition-DiscreteDomains\\dataset\\blocks-world\\10\\block-words_p01_hyp-0_10_0.tar.bz2";
		//String file_name ="dataset\\blocks-world\\yifat_experiments\\3-atom-goals-2-towers.rar";
		//dataset\yifat-experiments\proposal-exp\depots\depots-0.rar
		//String pathOfFiles = "C:\\Users\\Yifat\\Documents\\GitHub\\" +
		//				 "domain-independent-deceptive-planning\\" +
		//			"yifats_goals\\3-atom-goals-2-towers";
		
		// 3-atom-goals-2-towers"
		String file_name ="yifat-experiments\\proposal-exp\\blocks\\3-atom-goals-2-towers";
		String file_name_hyp ="yifat-experiments\\proposal-exp\\blocks\\3-atom-goals-2-towers-hyp.rar";
		String pathOfFiles = basePath + "3-atom-goals-2-towers";

		// "exp-mix1-0"
		//String file_name ="dataset\\yifat-experiments\\proposal-exp\\blocks\\exp-mix-0.rar";
		//String file_name_hyp ="dataset\\yifat-experiments\\proposal-exp\\blocks\\exp-mix-0-hyp.rar";
		//String pathOfFiles = basePath + "exp-mix1-0";

		// "exp-mix1-0"
		//String file_name ="dataset\\yifat-experiments\\proposal-exp\\blocks\\exp-mix-1.rar";
		//String file_name_hyp ="dataset\\yifat-experiments\\proposal-exp\\blocks\\exp-mix-1-hyp.rar";
		//String pathOfFiles = basePath + "exp-mix1-1";
		
		//String file_name ="dataset\\yifat-experiments\\proposal-exp\\depots\\depots-simple-0.rar";
		//String file_name_hyp ="dataset\\yifat-experiments\\proposal-exp\\depots\\depots-simple-0-hyp.rar";
		//String pathOfFiles = basePath + "depots-0";
		
		//exp-base
		//String file_name ="yifat-experiments\\proposal-exp\\blocks\\exp-base.rar";
		//String file_name_hyp ="yifat-experiments\\proposal-exp\\blocks\\exp-base.rar";
		//String pathOfFiles = basePath + "base-exp";
		
		FileManager fileManager = new BlocksFileManager(pathOfFiles, "plan_files", "problem_files"); 
		MultiGoalRecognizer  recognizer = new MultiGoalRecognizer(file_name, fileManager);

		//Plan p;
		//try {
		//	p = recognizer.makePlan();
		//	System.out.println(p.getGoal());
		//	System.out.println(p);
		//}catch (UnreachableGoalException e) {
		//	e.printStackTrace();
		//}
		fileManager.printWorldStatesToFile(recognizer);
		
		try {
			GoalRecognitionResult rst = recognizer.call();
			//GoalRecognitionResult rst1 = recCheckResultFromBeginning.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

				
		
		//try {
		//	if(args.length == 3){
		//		String goalRecognitionFile = args[1];
		//		float threshold = Float.valueOf(args[2]);
		//		if(args[0].equals("-offline")){
		//			OnlineGoalRecognitionMirroringWithLandmarks recognizer = new OnlineGoalRecognitionMirroringWithLandmarks(goalRecognitionFile, threshold);
		//			recognizer.recognizeOffline();
		//		} else if(args[0].equals("-online")){
		//			OnlineGoalRecognitionMirroringWithLandmarks recognizer = new OnlineGoalRecognitionMirroringWithLandmarks(goalRecognitionFile, threshold);
		//			recognizer.recognizeOnline();
		//		} else {
		//			printUsage();
		//		}
		//	} else {
		//		printUsage();
		//	}
		//} catch (IOException | InterruptedException | UnreachableGoalException e) {
		//	e.printStackTrace();
		//}
	}
	
	//private static void printUsage(){
	//	System.out.println("- Option - Parameters needed: < -online | -offline > < tar.bz2 file > < threshold_value >");
	//	System.out.println("\t $> Example: -online experiments/logistics/100/logistics_p01_hyp-1_full.tar.bz2 0.0");
	//}

}

