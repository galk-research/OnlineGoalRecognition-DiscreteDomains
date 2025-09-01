package recognizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javaff.data.Fact;
import javaff.data.GroundFact;
import javaff.data.strips.And;

public class ComplexGoal {

	private GroundFact goal;
	private int atomicGoalsNum;
	private Set<Integer> combinationIndexes;
	
	public ComplexGoal (List<GroundFact> atomicGoals, List<Integer> combinedGoalsIndexes) {
		this.atomicGoalsNum = combinedGoalsIndexes.size();
		
		this.combinationIndexes = new HashSet<Integer>();
		
		// TODO gone when complex goal extends goal. 
		if (this.atomicGoalsNum == 1) {
			int index = combinedGoalsIndexes.getFirst();
			this.goal = atomicGoals.get(index);
			this.combinationIndexes.add(index);
		}
		else {
			for (int i = 0; i < this.atomicGoalsNum; i++) {
				this.combinationIndexes.add(combinedGoalsIndexes.get(i));
			}
		
			// Create a list of facts, consist of all the fact of the combined goals.
			Set<Fact> facts = new HashSet<Fact>();
			for (int i = 0; i < this.atomicGoalsNum; i++) {
				facts.addAll(atomicGoals.get(combinedGoalsIndexes.get(i)).getFacts());
			}
		
			// Create the goal from the facts list.
			this.goal = new And(facts);
		}
	}
	
	
	public GroundFact getGoal() {
		return this.goal;
	}
	
	public int getAtomicGoalsNum() {
		return this.atomicGoalsNum;
	}
	
	public Set<Integer> getAtomicGoalIndexes(){
		return this.combinationIndexes;
	}
	
	@Override
	public boolean equals(Object o){
		if ((o instanceof ComplexGoal) || (o instanceof GroundFact)) {
			ComplexGoal cmlxO = (ComplexGoal)o;
			return this.goal.equals(cmlxO.getGoal());
		}
		
		return false;
	}
}

