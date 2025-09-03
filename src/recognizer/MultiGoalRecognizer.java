package recognizer;


import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import statistics.StatisticsForGraphs;
import file_managers.FileManager;
import javaff.data.Action;
import javaff.data.GroundFact;
import javaff.data.Plan;
import javaff.planning.STRIPSState;
import javaff.search.UnreachableGoalException;



public class MultiGoalRecognizer extends GoalRecognition {
	private List<ComplexGoal> cmplxGoals;
	
	// TODO make complex goal extend GroundFact.
	// This is disgusting.
	private Map<GroundFact, ComplexGoal> goalToComplexGoal; 
	
	public MultiGoalRecognizer(String fileName, FileManager fileManager){
		super(fileName, fileManager);
		this.cmplxGoals = new ArrayList<ComplexGoal>();
		
		// Create for each original goal, a complexGoal consist of 1 atomic goal.
		// TODO check id a better place for this is the recognition function.
		for (int i = 0; i< this.candidateGoals.size(); i++) {
			List<Integer> lst = new ArrayList<Integer>();
			lst.add(i);
			ComplexGoal goal = new ComplexGoal(this.candidateGoals, lst);
			this.cmplxGoals.add(goal);
		}
		
		// TODO make complex goal extend GroundFact.
		// This is disgusting.
		this.goalToComplexGoal = new HashMap<GroundFact, ComplexGoal>();
		for (ComplexGoal cmlx : this.cmplxGoals) {
			this.goalToComplexGoal.put(cmlx.getGoal(), cmlx);
		}
	}

	@Override
	public GoalRecognitionResult call() throws Exception {
		return this.recognizeOnline();
	}
	
	public List<STRIPSState> getWorldStatesDuringPlan(){
		List<STRIPSState> lstWorldStates = new LinkedList<STRIPSState>();
		
		STRIPSState currentState = this.initialSTRIPSState;
		lstWorldStates.add(currentState);
		
		for(Action o: this.observations){
			currentState = (STRIPSState) currentState.apply(o);
			lstWorldStates.add(currentState);
		}

		return lstWorldStates;
	}
	
	private void SaveStatsToFile(GroundFact realGoal, Map<GroundFact, List<Float>> goalsToStats, String statsName, List<Action> observationAnalyzed) {
		try {
			String fileName = this.getRecognitionFileName() + statsName + ".txt";
			File myObj = new File(fileName);
		    System.out.println("States are saved to file:"+ fileName);
			myObj.createNewFile();
		    FileWriter myWriter = new FileWriter(fileName);
		    myWriter.write(realGoal.toString() + "\n");
		    myWriter.write(observationAnalyzed.toString() + "\n");
		    
		    for(ComplexGoal goal: this.cmplxGoals){
		    	myWriter.write(goal.getGoal().toString() + ",");
		    }
		    
		    myWriter.write("\n");

		    for(ComplexGoal goal: this.cmplxGoals){
				for (Float stat : goalsToStats.get(goal.getGoal()))
				{
					myWriter.write(stat+" ");
				}
				myWriter.write("\n");
			}
		    
		    myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	@Override
	public GoalRecognitionResult recognizeOffline() throws UnreachableGoalException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public GoalRecognitionResult recognizeOnline() throws UnreachableGoalException, IOException, InterruptedException {
		Map<GroundFact, List<Action>> mObservationsGoals = new HashMap<>();
		STRIPSState currentState = this.initialSTRIPSState;
		System.out.println("#> Init status: " + currentState);
		System.out.println("#> Real Goal: " + this.realGoal);
		
		float numberOfCallsPlanner = 0;
		float observationCounter = 0;
		float topFirstFrequency = 0;
		float convergenceToTopRankedGoal = 0;
		
		// Variables used to calculate when to add the complex goals.
		int maxGoalsCombinations = 1;
		ContributionMap contributionMap = new ContributionMap();
		
		// Initiate statistics data for graphs.
		StatisticsForGraphs graphStatistics = new StatisticsForGraphs(candidateGoals); 
		
		//Map<GroundFact, List<Float>> goalsToProbabiltyStats = new HashMap<>();
		//Map<GroundFact, List<Float>> goalsToScoresStats = new HashMap<>();
		// Is used for logging when one observation is analyzed twice, it happens when the list if potential goals is updated.
		//List<Action> observationAnalyzed = new LinkedList<Action>(); 
		
		//for (GroundFact goal: this.candidateGoals) {
		//	goalsToProbabiltyStats.put(goal, new ArrayList<Float>());
		//	goalsToScoresStats.put(goal, new ArrayList<Float>());
		//}
		
		// For each observation
		System.out.println("observation:"+ this.observations);
		for(Action o: this.observations){
			System.out.println("$> Observation (" + (int) observationCounter + ") :" + o);
			observationCounter++;
			
			// Computing the current state, after the observation.
			currentState = (STRIPSState) currentState.apply(o);
			
			boolean doneWithCurrObservation = false;
			boolean wasCurrObservationAnalyzed = false;
			
			Map<GroundFact, Float> goalsProbabilities = new HashMap<GroundFact, Float>();
			float highestProbability = 0;
			
			while (!doneWithCurrObservation) {
				float sumOfScores = 0f;
				Map<GroundFact, Float> goalsToScores = new HashMap<>();
				
				
				// For each potential goal.
				for(ComplexGoal cmplxGoal: this.cmplxGoals){
				
					GroundFact curr_goal = cmplxGoal.getGoal();
				
					System.out.println("\n\t # Goal:" + curr_goal );
					Plan idealPlan = this.makePlan(initialState, curr_goal);
					numberOfCallsPlanner++;
					System.out.println("\t # Ideal Plan of G: " + idealPlan.getPlanLength());
					System.out.println("\t # " + idealPlan);
					List<Action> mMinus = mObservationsGoals.get(curr_goal);
					
					// Ask Mor about this, why each goal has a different list that contains all the seen observations?
					// Why not having one list for all the goals?
					if(mMinus == null){
						List<Action> mMinusNew = new ArrayList<Action>();
						mMinus = mMinusNew;
						mMinusNew.add(o);
						mObservationsGoals.put(curr_goal , mMinusNew);
					} else if(!wasCurrObservationAnalyzed) mMinus.add(o);
					
					//Plan mPlus = this.makePlan(currentState.getFacts(), curr_goal, (int)observationCounter);
					Plan mPlus = this.makePlan(currentState.getTrueFacts(), curr_goal);
					numberOfCallsPlanner++;
					System.out.println("\t # mMinus: " + mMinus.size());
					System.out.println("\t # mPlus: " + mPlus.getPlanLength());
					System.out.println("\t # idealPlan: " + idealPlan.getPlanLength());
					float mG = mMinus.size() + mPlus.getPlanLength();
					float score = this.match(mG, idealPlan.getPlanLength());
					System.out.println("\t @@@@ Score: " + score);
					sumOfScores += score;
					goalsToScores.put(curr_goal, score);
				
					// Scores for goal-combination. If the goal handled is an atomic goal.
					if ((cmplxGoal.getAtomicGoalsNum() == 1) && (!wasCurrObservationAnalyzed)) {
						// If this is the first time we update the contribution map.
						if (!contributionMap.IsInitiated(curr_goal)) {
							contributionMap.InitiateContributionMap(curr_goal, idealPlan.getPlanLength());
						}
						contributionMap.UpdateContributionMap(cmplxGoal.getGoal(), mPlus.getPlanLength());
					}
				} // END OF for(ComplexGoal cmplxGoal: this.cmplxGoals)- observation analysis?
				
				graphStatistics.addObservation(o);
				//observationAnalyzed.add(o);
				wasCurrObservationAnalyzed = true;
				
				// Calculates the probability for each goal, after seeing the current observation.
				float normalizingFactor = (1/sumOfScores);
				GroundFact mostLikelyGoal = this.cmplxGoals.get(0).getGoal();//  candidateGoals.get(0);
				//float highestProbability = (normalizingFactor*goalsToScores.get(mostLikelyGoal));
				highestProbability = (normalizingFactor*goalsToScores.get(mostLikelyGoal));
				//Map<GroundFact, Float> goalsProbabilities = new HashMap<>();
				goalsProbabilities = new HashMap<>();
				for(ComplexGoal cmplxGoal: this.cmplxGoals){//this.candidateGoals){
					GroundFact goal = cmplxGoal.getGoal();
					float probabilityOfG = (normalizingFactor*goalsToScores.get(goal));
					//System.out.println("\t - Probability of " + goal + ": " + probabilityOfG);
					goalsProbabilities.put(goal, probabilityOfG);
				
					// Save the probabilities and the scores.
					graphStatistics.AddStatsToGoals(goal, goalsToScores.get(goal), probabilityOfG);
					//goalsToProbabiltyStats.get(goal).add(probabilityOfG);
					//goalsToScoresStats.get(goal).add(goalsToScores.get(goal)); 
				
					if(probabilityOfG > highestProbability){
						mostLikelyGoal = goal;
						highestProbability = probabilityOfG;
					}
				} // END OF for(ComplexGoal cmplxGoal: this.cmplxGoals) - probability calculation.
			
							
				// Considering multiple goals.
				// If there is a goal with a 'perfect score', (all the observations contributes to the goal).
				if (Collections.max(goalsToScores.values()) >= 0.9 || (maxGoalsCombinations >= this.candidateGoals.size())){
					doneWithCurrObservation = true;
				} else {
					List<ComplexGoal> newcmplxGoals = new LinkedList<ComplexGoal>();
				
					// While new goals were not found.
					while((newcmplxGoals.size() == 0) && (maxGoalsCombinations < this.candidateGoals.size())) {
						// Increase the maximum number of atomic goals allowed in a combination.
						// And search for new goal combination to consider.
						maxGoalsCombinations++;
						newcmplxGoals = contributionMap.getCandidiatsforCombinedGoals(maxGoalsCombinations);
					}
				
					this.AddNewGoals(newcmplxGoals, graphStatistics, mObservationsGoals);
				} // END OF ELSE  (Collections.max(goalsToScores.values()) < 0.9)				
			} // END OF while (!doneWithCurrObservation)
			Set<GroundFact> recognizedGoals = new HashSet<>();
			for(GroundFact goal: goalsProbabilities.keySet())
				if(goalsProbabilities.get(goal) == highestProbability)
					recognizedGoals.add(goal);

			if(recognizedGoals.size() == 1 && recognizedGoals.toArray()[0].equals(this.realGoal)){  
				topFirstFrequency++;
				convergenceToTopRankedGoal++;
			} else convergenceToTopRankedGoal = 0;

			
		} // END OF for(Action o: this.observations)
		
		// Save general statistics, to be plotted later.
		this.SaveStatsToFile(this.realGoal, graphStatistics.getScoresStats(), "ScoreStats", graphStatistics.getObservations());
		this.SaveStatsToFile(this.realGoal, graphStatistics.getProbabilityStats(), "ProbStats", graphStatistics.getObservations());
		
		// Print general statistics.
		float topFirstRankedPercent  = (topFirstFrequency/observationCounter);
		float convergencePercent = (convergenceToTopRankedGoal/observationCounter);
		System.out.println("\n$$$$####> Top First Ranked Percent (%): " + topFirstRankedPercent);
		System.out.println("$$$$####> Convergence Percent (%): " + convergencePercent);
		System.out.println("$$$$####> Top Ranked First times: " + topFirstFrequency);
		System.out.println("$$$$####> Total Candidate Goals: " + this.candidateGoals.size());
		System.out.println("$$$$####> Total Observed Actions: " + observationCounter);
		System.out.println("$$$$####> Total Number of Landmarks: " + this.getAverageOfFactLandmarks());
		System.out.println("$$$$####> Total Number of Calls to Planner: " + numberOfCallsPlanner);
		
		return new GoalRecognitionResult(topFirstRankedPercent, convergencePercent, this.candidateGoals.size(), this.observations.size(), this.getAverageOfFactLandmarks(), numberOfCallsPlanner);
	}
	
	private void AddNewGoals(List<ComplexGoal> newcmplxGoals, StatisticsForGraphs graphStatistics, Map<GroundFact, List<Action>> mObservationsGoals) {
		for (ComplexGoal newcmplxGoal : newcmplxGoals) {
			
			// If this goals is not already added.
			if(!this.cmplxGoals.contains(newcmplxGoal)) {
				System.out.println("Addind goal" + newcmplxGoal.getGoal());
				this.cmplxGoals.add(newcmplxGoal);
				
				// Add the new goal to the class that hold statistics for graphs.
				graphStatistics.addNewGoal(newcmplxGoal.getGoal());
				
				GroundFact onlyToGetTheObservationsSeenSoFar = this.candidateGoals.get(0);
				List<Action> mMinusNew = new ArrayList<Action>(mObservationsGoals.get(onlyToGetTheObservationsSeenSoFar));
				mObservationsGoals.put(newcmplxGoal.getGoal(), mMinusNew);
			}
		}

	}
	
	protected class ContributionMap {
		Map<GroundFact, List<Integer>> goalsTostepsNumToReachGoal;
		Map<GroundFact, List<Boolean>> goalsToContibuteSteps;
		
		public ContributionMap() {
			this.goalsTostepsNumToReachGoal = new HashMap<GroundFact, List<Integer>>();
			this.goalsToContibuteSteps = new HashMap<GroundFact, List<Boolean>>();
			
			// For each atom goal, create a map entry.
			for(GroundFact goal : MultiGoalRecognizer.this.candidateGoals) {
				this.goalsTostepsNumToReachGoal.put(goal, new ArrayList<Integer>());
				this.goalsToContibuteSteps.put(goal, new ArrayList<Boolean>());
			}
		}
		
		public boolean IsInitiated(GroundFact goal) {
			if(this.goalsTostepsNumToReachGoal.get(goal).size() == 0) {
				return false;
			}
			return true;
		}
		
		public void InitiateContributionMap(GroundFact goal, int numberOfStepsToGoal) {
			List<Integer> stepsToGoal = this.goalsTostepsNumToReachGoal.get(goal);
			stepsToGoal.add(numberOfStepsToGoal);
		}
		
		public void UpdateContributionMap(GroundFact goal, int numberOfStepsToGoal) {
			List<Integer> stepsToGoal = this.goalsTostepsNumToReachGoal.get(goal);
			List<Boolean> stepsContribution = this.goalsToContibuteSteps.get(goal);
			
			
			Boolean isContributed = false;
			// If the current step takes the agent closer to the goal.
			// (The plan length before this step is bigger than the plan length after this step.)
			if (stepsToGoal.getLast() > numberOfStepsToGoal) {
				isContributed = true;
			}
				
			stepsToGoal.add(numberOfStepsToGoal);
			stepsContribution.add(isContributed);
		}
		
		public int countContibuteSteps(List<Boolean> lstContributionSteps) {
			int sum = 0;
			
			for (Boolean isContributed: lstContributionSteps) {
				if (isContributed) {
					sum++;
				}
			}
			
			return sum;
		}
		
		public List<Boolean> sumContributions(List<Boolean> lstContributionSteps1, List<Boolean> lstContributionSteps2 ) {
			List<Boolean> sumContributions = new ArrayList<Boolean>();
			
			for(int i = 0; i < lstContributionSteps1.size(); i++) {
				sumContributions.add(lstContributionSteps1.get(i) || lstContributionSteps2.get(i));
			}
			return sumContributions;
		}
		
		public  List<ComplexGoal> getCandidiatsforCombinedGoals(int sizeOfCombination) {
			float thresholdScore = (float)1 / (float)sizeOfCombination;
			List<ComplexGoal>  newcomplxGoals = new LinkedList<ComplexGoal>();
			
			// Goes over all the original goals.
			for (GroundFact goal : MultiGoalRecognizer.this.candidateGoals) {
				float expScore = this.countContibuteSteps(this.goalsToContibuteSteps.get(goal));
				expScore /=  this.goalsToContibuteSteps.get(goal).size();
				
				if (expScore >= thresholdScore) {
					//List<ComplexGoal> newCmplxGoals = this.getAllGoodCombinationOfGoal(this.goalToComplexGoal.get(goal), sizeOfCombination,
					//																goalsToContibuteSteps, goalsToContibuteSteps.get(goal));
					List<ComplexGoal> newCmplxGoals = this.getAllGoodCombinationOfGoal(MultiGoalRecognizer.this.goalToComplexGoal.get(goal), sizeOfCombination, this.goalsToContibuteSteps.get(goal));
					newcomplxGoals.addAll(newCmplxGoals);
				}
			}
			return  newcomplxGoals ;
		}
			
		public List<ComplexGoal> getAllGoodCombinationOfGoal(ComplexGoal cmplxGoal, int maxSizeOfCombination, List<Boolean> contributeStepsOfcmplxGoal) {
			List<ComplexGoal> lstNewGoals = new LinkedList<ComplexGoal>();

			// If the complex goals is a combination of the maximum number of original goals allowed.
			if (cmplxGoal.getAtomicGoalsNum() == maxSizeOfCombination) {
				// Return only this goal.
				lstNewGoals.add(cmplxGoal);
				return lstNewGoals;
			}

			// thresholdScore is the score for 1 original goal, multiply the number of goals combined in the complexGoal.
			// The maximum thresholdScore is 0.9. 
			float thresholdScore = (float)1 / (float)maxSizeOfCombination;
			thresholdScore *= (cmplxGoal.getAtomicGoalsNum() + 1);
			thresholdScore = Math.min((float)0.9, thresholdScore);

			// Goes over all the original goals.
			for (GroundFact goal : MultiGoalRecognizer.this.candidateGoals) {
				// Get the index of the current goal.
				ComplexGoal cmplxTemp = MultiGoalRecognizer.this.goalToComplexGoal.get(goal);
				Set<Integer> setIndexes = cmplxTemp.getAtomicGoalIndexes();
				Integer index = (Integer)setIndexes.toArray()[0];

				// If the current goal is not already combined in the complex goal.
				if (!cmplxGoal.getAtomicGoalIndexes().contains(index)) {
					List<Boolean> contributionOfCurrGoal = this.goalsToContibuteSteps.get(goal);
					List<Boolean> contributionWithCurrGoal = this.sumContributions(contributeStepsOfcmplxGoal, contributionOfCurrGoal); 
					float expScore = this.countContibuteSteps(contributionWithCurrGoal);
					expScore /= contributionWithCurrGoal.size();

					if(expScore >= thresholdScore) {
						// Create a new complex goal from the combination of cmplxGoal and goal. 
						List<Integer> indexes = new ArrayList<Integer>(cmplxGoal.getAtomicGoalIndexes());
						indexes.add(index);
						ComplexGoal newComplexGoal = new ComplexGoal(MultiGoalRecognizer.this.candidateGoals, indexes);

						List<ComplexGoal> lst = this.getAllGoodCombinationOfGoal(newComplexGoal, maxSizeOfCombination, contributionWithCurrGoal);
						lstNewGoals.addAll(lst);
					}
				}
			}
			return lstNewGoals;
		}
		
	}
}

