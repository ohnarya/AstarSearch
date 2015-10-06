/*
 * Author : Jiyoung Hwang
 * Date   : 2015.10.5
 * Name   : BlockWorld
 * Desc   : manage block world puzzle
 *          1. generate initial problem : generate initial state 
 *                                        which is randomly generated with n stacks and m blocks
 *          2. set the goal state  
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class BlockWorld {

	
	/*
	 * generate initial problem with n stakcs and m blocks randomly
	 * 
	 * */
	public List<Stack<Integer>> generateInitialProblem(int blocks, int stacks, boolean israndom)
	{
		List<Stack<Integer>> state = new ArrayList<Stack<Integer>>();
		if(israndom){
			Random r = new Random();
			
			int j = 0;
			while(j<stacks){
				state.add(j, new Stack<Integer>());
				j++;
			}
			while(blocks>0){
				int i = r.nextInt(stacks);
				Stack<Integer> aStack  = new Stack<Integer>();
				aStack = state.get(i);
				aStack.push(blocks);
				state.remove(i);
				state.add(i,aStack);
				blocks--;
			}
		}else{
	/* example */		
//			Stack<Integer> s1 = new Stack<Integer>();
//			s1.push(2);
//			Stack<Integer> s2 = new Stack<Integer>();
//			s2.push(3);
//			s2.push(5);
//			Stack<Integer> s3 = new Stack<Integer>();
//			s3.push(1);
//			s3.push(4);
//			state.add(s1);
//			state.add(s2);
//			state.add(s3);

			Stack<Integer> s1 = new Stack<Integer>();
			s1.push(4);
			Stack<Integer> s2 = new Stack<Integer>();
			s2.push(5);
			s2.push(6);
			s2.push(9);
			s2.push(10);
			Stack<Integer> s3 = new Stack<Integer>();
			s3.push(2);
			s3.push(7);
			Stack<Integer> s4 = new Stack<Integer>();
			s4.push(3);
			s4.push(8);
			Stack<Integer> s5 = new Stack<Integer>();
			s5.push(1);
			state.add(s1);
			state.add(s2);
			state.add(s3);	
			state.add(s4);
			state.add(s5);	
		}
		
		System.out.println("Initial state : ");
    	for(int i=0;i<state.size();i++){
    		System.out.println(state.get(i).toString());
    	}
		return state;
	}
	
	/*
	 * set a goal state
	 * */
	public Node getGoal(int blocks, int stack){
		Node goal = new Node();

		List<Stack<Integer>> state  = new ArrayList<Stack<Integer>>();
		Stack<Integer>       astate = new Stack<Integer>();
		
		for(int i=1;i<=blocks;i++){
			astate.push(i);
		}
		
		/*set a goal state in the first row*/
		state.add(0,astate);
		for(int j=1;j<stack;j++){
			state.add(j, new Stack<Integer>());
		}
		
		goal.state = state;
		/*calculate Heuristic and G */
		goal.setH(goal);
		goal.setG(0);
		/*return goal*/
		return goal;
	}
}