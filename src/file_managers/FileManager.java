package file_managers;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private String databaseFileName;
	private Map<String, String> planToFileName;
	private int dbIndex;

	public FileManager(String path, String plansDir, String problemDir) {
		this.filePath = path;
		this.planFilesDir = plansDir;
		this.problemDefinitionDir = problemDir;
		this.planToFileName = new HashMap<String, String>();
		this.dbIndex = -1;
		databaseFileName = "files_mappping.txt";
		this.loadFilesNames();
	}

	private void loadFilesNames() {
		Path dbfilePath = Paths.get(this.filePath, this.planFilesDir, this.databaseFileName);
		Path folderPath = Paths.get(this.filePath, this.planFilesDir);
		try {
			File folder = folderPath.toFile();
			if (!folder.exists()) {
				boolean succeed = folder.mkdirs();
				if (!succeed) {
					System.out.println("There was a problem creating the directory: " + folderPath);
				}
			}

			File f = dbfilePath.toFile();
			// If there is no data base file that holds the plan files name, create this
			// file.
			if (!f.exists()) {
				f.createNewFile();
				// If the data base file exists, load it.
			} else {
				Scanner myReader = new Scanner(f);
				while (myReader.hasNextLine()) {
					// Put the entry in the local memory.
					String data = myReader.nextLine();
					String[] arrData = data.split(":");
					this.planToFileName.put(arrData[0], arrData[1]);

					// Update the highest index a file has in the data base.
					int temp = Integer.parseInt(arrData[1]);
					if (temp > this.dbIndex) {
						this.dbIndex = temp;
					}
				}
				myReader.close();
			}
		} catch (Exception e) {
			System.out.println("A problem occured with the file: " + filePath);
		}
		this.dbIndex++;

	}

	private void addFileNameToMap(String fileName) {
		this.planToFileName.put(fileName, String.valueOf(this.dbIndex));
		this.dbIndex++;

		// Update the saved file with the new entry.
		Path filePath = Paths.get(this.filePath, this.planFilesDir, this.databaseFileName);
		try {
			String lineToAdd = fileName + ":" + this.planToFileName.get(fileName) + "\n";

			File f = filePath.toFile();
			FileWriter writer = new FileWriter(f, true);
			writer.append(lineToAdd);
			writer.close();
		} catch (Exception e) {
			System.out.println("A problem occured with the file: " + filePath);
		}
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
		return "obs-from-" + this.getUniqueStateRepresentation(state) + "-to-"
				+ this.getUniqueStateRepresentation(goal);
	}

	public Plan getPlanFromFile(Set<Fact> state, Fact goal) {
		String fileName = this.buildFileName(state, goal);

		if (!this.planToFileName.containsKey(fileName)) {
			return null;
		}

		// fileName = this.filePath + "\\" + this.planFilesDir + "\\" +
		// this.planToFileName.get(fileName);
		Path filePath = Paths.get(this.filePath, this.planFilesDir, this.planToFileName.get(fileName));

		Plan p = new TotalOrderPlan(goal);
		Boolean isDone = false;

		try {
			File planFile = filePath.toFile();
			Scanner myReader = new Scanner(planFile);

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				javaff.data.strips.STRIPSInstantAction action = new STRIPSInstantAction(data);
				p.addAction(action);
			}
			myReader.close();
			isDone = true;
		} catch (IOException e) {
			// System.out.println("File not found." + fileName);
			// e.printStackTrace();
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
		// String fileName = this.filePath + "\\" + this.problemDefinitionDir +
		// "\\init_states.pddl";
		Path filePath = Paths.get(this.filePath, this.problemDefinitionDir, "init_states.pddl");
		List<STRIPSState> lstStates = recognizer.getWorldStatesDuringPlan();

		try {
			File myObj = filePath.toFile();
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter(filePath.toFile());

			for (STRIPSState state : lstStates) {
				myWriter.write(this.formatInitState(state));
				myWriter.write("\n");
			}

			myWriter.close();
		} catch (IOException e) {
			System.out.println("Could not find file.");
			e.printStackTrace();
		}
	}

	public void savePlanToFile(Set<Fact> startState, Plan pln) {
		String fileName = this.buildFileName(startState, pln.getGoal());
		if (!this.planToFileName.containsKey(fileName)) {
			this.addFileNameToMap(fileName);
		}

		// fileName = this.filePath + "\\" + this.planFilesDir + "\\" +
		// this.planToFileName.get(fileName);
		Path filePath = Paths.get(this.filePath, this.planFilesDir, this.planToFileName.get(fileName));

		try {
			File myObj = filePath.toFile();// File(fileName);
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter(filePath.toFile());

			for (Action action : pln.getActions()) {
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
