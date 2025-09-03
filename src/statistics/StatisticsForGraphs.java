package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javaff.data.Action;
import javaff.data.GroundFact;

public class StatisticsForGraphs {
	Map<GroundFact, List<Float>> goalsToProbabiltyStats;
	Map<GroundFact, List<Float>> goalsToScoresStats; 
	List<Action> observations = new LinkedList<Action>(); 
	
	public StatisticsForGraphs (List<GroundFact> candidateGoals) {
		this.goalsToProbabiltyStats = new HashMap<GroundFact, List<Float>>();
		this.goalsToScoresStats = new HashMap<GroundFact, List<Float>>();
		this.observations = new LinkedList<Action>();
		
		for(GroundFact goal : candidateGoals) {
			this.goalsToProbabiltyStats.put(goal, new LinkedList<Float>());
			this.goalsToScoresStats.put(goal, new LinkedList<Float>());
		}
	}
	
	public Map<GroundFact, List<Float>> getProbabilityStats(){
		return this.goalsToProbabiltyStats;
	}
	
	public Map<GroundFact, List<Float>> getScoresStats(){
		return this.goalsToScoresStats;
	}
	
	
	public List<Action> getObservations(){
		return this.observations;
	}
	
	public void AddStatsToGoals(GroundFact goal, Float score, Float prob) {
		List<Float> scores = this.goalsToScoresStats.get(goal);
		List<Float> probs = this.goalsToProbabiltyStats.get(goal);
		
		scores.add(score);
		probs.add(prob);
	}
	
	public void addObservation(Action observation) {
		this.observations.add(observation);
	}
	
	public void addNewGoal(GroundFact goal) {
		this.goalsToScoresStats.put(goal, new ArrayList<Float>());
		this.goalsToProbabiltyStats.put(goal, new ArrayList<Float>());

		int numOfAnalizedObservation = this.observations.size();
		for (int iObs = 0; iObs < numOfAnalizedObservation; iObs++) {
			this.goalsToScoresStats.get(goal).add((float) 0);
			this.goalsToProbabiltyStats.get(goal).add((float)0);
		}
	}
}
