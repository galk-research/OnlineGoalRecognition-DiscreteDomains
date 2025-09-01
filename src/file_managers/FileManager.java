package file_managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javaff.data.Action;
import javaff.data.Fact;
import javaff.data.Plan;
import javaff.data.TotalOrderPlan;
import javaff.data.strips.STRIPSInstantAction;
import javaff.planning.STRIPSState;
import recognizer.MultiGoalRecognizer;

public class FileManager {
	private String filePath;
	private String planFilesDir;
	private String problemDefinitionDir;
	
	
	public FileManager (String path, String plansDir, String problemDir) {
		this.filePath = path;
		this.planFilesDir = plansDir;
		this.problemDefinitionDir = problemDir;
	}

	public String getShortRepresentation(String str) {
		return str;
	}
	
	private String getUniqueStateRepresentation(Fact goal) {
		// TODO work with string builder. cause duh!
		String strGoals = this.getShortRepresentation(goal.toString());
		
		strGoals = strGoals.toUpperCase();
		strGoals = strGoals.replaceAll("AND", "");
		strGoals = strGoals.replaceAll("\\( ", "");
		strGoals = strGoals.replaceAll(" \\)", "");
		strGoals = strGoals.replaceAll("\\)  \\(", "\\)-\\(");
		strGoals = strGoals.replaceAll(" ", "_");
						
		ArrayList<String> arrlstGoals = new ArrayList<>(Arrays.asList(strGoals.split("-")));
		Collections.sort(arrlstGoals);
						
		strGoals = arrlstGoals.toString();
		strGoals = strGoals.replaceAll("\\[", "");
		strGoals = strGoals.replaceAll("\\]", "");
		strGoals = strGoals.replaceAll(",", "");
		strGoals = strGoals.replaceAll("\\) \\(", "\\)\\(");
		strGoals = strGoals.replaceAll(" ", "_");
		return strGoals;
	}
	
	private String getUniqueStateRepresentation(Set<Fact> state) {
		String strState = this.getShortRepresentation(state.toString());
		
		strState = strState.toUpperCase();
		strState = strState.replaceAll("\\[", "");
		strState = strState.replaceAll("\\]", "");
		strState = strState.replaceAll(", ", ",");
		strState = strState.replaceAll(" ", "_");
		
		ArrayList<String> arrlstState = new ArrayList<>(Arrays.asList(strState.split(",")));
		Collections.sort(arrlstState);
		
		strState = arrlstState.toString();
		strState = strState.replaceAll("\\]", "\\)");
		strState = strState.replaceAll("\\[", "\\(");
		strState = strState.replaceAll(",", "\\)\\(");
		strState = strState.replaceAll("\\( +", "\\(");
		strState = strState.replaceAll(" ", "_");
		
		return strState;
	}
	
	
	private String buildFileName(Set<Fact> state, Fact goal) {
		// Build the file's name.
		return "obs-from-" + this.getUniqueStateRepresentation(state) + "-to-" + this.getUniqueStateRepresentation(goal);		
	}
	
	public Plan getPlanFromFile(Set<Fact> state, Fact goal) {
		String fileName = this.buildFileName(state, goal);
		fileName = this.filePath + "\\" + this.planFilesDir + "\\" + fileName + ".pddl";
		
		Plan p = new TotalOrderPlan(goal);
		Boolean isDone = false;
		
		try {
			File planFile = new File(fileName);
			Scanner myReader = new Scanner(planFile);
			
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				javaff.data.strips.STRIPSInstantAction action = new STRIPSInstantAction(data);
				p.addAction(action);
		    }			
			myReader.close();
		    isDone = true;
		}catch(IOException e) {
			//System.out.println("File not found." + fileName);
			//e.printStackTrace();
		}
		
		if (isDone) {
			return p;
		}
		return null;
	}
	
	private String formatInitState(STRIPSState state) {
		String strState = state.getTrueFacts().toString();
		strState = strState.toUpperCase();
		strState = strState.replaceAll("\\]", "\\)");
		strState = strState.replaceAll("\\[", "\\(");
		strState = strState.replaceAll(", ", "\\) \\(");
		strState = "(:INIT " + strState + ")"; 
		return strState;
	}
	
	public void printWorldStatesToFile(MultiGoalRecognizer recognizer) {
		String fileName = this.filePath + "\\" + this.problemDefinitionDir + "\\init_states.pddl";  
		List<STRIPSState> lstStates =  recognizer.getWorldStatesDuringPlan();
		
		try {
			File myObj = new File(fileName);
			myObj.createNewFile();
		    FileWriter myWriter = new FileWriter(fileName);
		    
		    for(STRIPSState state: lstStates){
		    	myWriter.write(this.formatInitState(state));
		    	myWriter.write("\n");
		    }
		    
		    myWriter.close();
		} catch (IOException e) {
			System.out.println("Could not find file.");
			e.printStackTrace();
		}
	}
	
	public void savePlanToFile(Set<Fact> startState ,Plan pln) {
		String fileName = this.buildFileName(startState , pln.getGoal());
		fileName = this.filePath + "\\" + this.planFilesDir + "\\" + fileName + ".pddl";
		
		try {
			File myObj = new File(fileName);
			myObj.createNewFile();
		    FileWriter myWriter = new FileWriter(fileName);
		    
		    for(Action action : pln.getActions()){
		    	myWriter.write(action.toString());
		    	myWriter.write("\n");
		    }
		    
		    myWriter.close();
		} catch (IOException e) {
			System.out.println("Could not find file.");
			e.printStackTrace();
		}
	}
}
