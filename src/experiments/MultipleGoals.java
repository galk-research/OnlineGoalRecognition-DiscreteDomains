package experiments;

import java.io.IOException;

import javaff.search.UnreachableGoalException;
import recognizer.GoalRecognitionResult;
import recognizer.MultiGoalRecognizer;
import recognizer.OnlineGoalRecognitionMirroringWithLandmarks;
import recognizer.OnlineGoalRecognitionNaive;

public class MultipleGoals{

	public static void main(String[] args) {
		//GoalRecognitionApproach approach = GoalRecognitionApproach.MIRRORING_LANDMARKS;
		//MultiGoalRecognizer  recognizer = new MultiGoalRecognizer("dataset\\blocks-world\\yifat_experiments\\exp1.rar");
		String file_name ="C:\\Users\\Yifat\\Documents\\GitHub\\OnlineGoalRecognition-DiscreteDomains\\dataset\\blocks-world\\10\\block-words_p01_hyp-0_10_0.tar.bz2";
		MultiGoalRecognizer  recognizer = new MultiGoalRecognizer("");
		try {
			GoalRecognitionResult rst = recognizer.call();
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

