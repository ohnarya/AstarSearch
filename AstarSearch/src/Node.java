/*
 * Author : Jiyoung Hwang
 * Date   : 2015.10.5
 * Name   : Node
 * Desc   : manage functionalities of a node 
 * 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Node implements Comparable<Node>{
	
	List<Stack<Integer>> state; /* state of the node */
	int   h; /*h(n) */
    int   g; /*g(n) */
    Node  n; /*next Node*/
    Node  p; /*parent Node*/
    
    List<Node> successors = new ArrayList<Node>();
    
    Node(){
    	this.state      = null;
    	this.g          = 0;
    	this.p          = null;
    	this.n          = null;
    	this.h          = 0;
    	this.successors = null;
    }

    Node(Node p, List<Stack<Integer>> state){
    	this.state      = state;
    	this.g          = p.getG() + 1;
    	this.p          = p;
    	this.n          = null;
    	this.h          = 0;	    	
    	this.successors = null;
    } 
    
    /* get Successors 
     * 
     * desc: get Successors of a node 
     *       calculate heuristic value for remaining distance to the goal 
     * */
    public void getSuccessor(Node goal,int stacks, boolean isbaseline){
    	
    	List<Node>           successors = new ArrayList<Node>();
    	List<Stack<Integer>> s          = copyState(this.state);
    	
    	Node    n = null;
     	
    	for(int i=0;i<stacks;i++){    				
			for(int j=0;j<stacks;j++){
				/*initialize sub lists */
		    	s = copyState(this.state);
		    	/*check if the stack is empty*/
	    		if(!(s.get(i).isEmpty())){
	    			/*a moving element to other stacks!*/
	    			Integer I = s.get(i).pop();
					if(i!=j){
						/*the element is moved*/
						s.get(j).push(I);
						/*a new Node is created with a new state*/
						n = new Node(this, s);
						/*set heuristic*/
						n.setH(goal,isbaseline);
						successors.add(n);
					}
	    		}
    			
    		}
    	}
    	/*add successors to a node*/
    	this.successors = successors;
    }
    
    /*copyState
     * desc : copy the origin node to a new node
     * 
     * */
    public List<Stack<Integer>> copyState(List<Stack<Integer>> origin){
    	List<Stack<Integer>> copy = new ArrayList<Stack<Integer>>();
    	Stack<Integer>       c    = null;
    	
    	for(Stack<Integer> ori : origin){
    		c = new Stack<Integer>();
    		c.addAll(ori);
    		copy.add(c);
    	}
    	return copy;
    }
    
    /*print state of a node */
    public void printState(List<Stack<Integer>> state){
    	for(int i=0;i<state.size();i++){
    		System.out.println(state.get(i).toString());
    	}
    }
    /*get State*/
    public List<Stack<Integer>> getState(){
    	return state;
    }
    
    /*get G value */
    public int getG(){
    	return this.g;
    }
    
    /*set G value*/
    public void setG(int g){
    	this.g = g;       
    }

    /*Manage Heuristic cost*/
    public void setH(int h){
    	this.h = h;
    }
    public void setH(Node goal){
    	this.h= calHeur(goal);
    }
    /*set Heuristic value*/
    public void setH(Node goal, boolean isbaseline){
    	if(isbaseline)
    		this.h = calHeurBaseline(goal);
    	else
    		this.h = calHeur(goal);
    } 
    
    /*calculate baseline Heuristic*/
    /*
     * return number of blocks out of place
     * */
    public int calHeurBaseline(Node goal){
    	int heur      = 0;
    	
		Stack<Integer> subgoal  = goal.state.get(0);
		Stack<Integer> substate = state.get(0);
		
		int j;    			
		for(j=0;j<substate.size();j++){
			if((int)subgoal.get(j) != (int)substate.get(j)){
				heur += 1;
			}				
		}
		
		heur += subgoal.size() - substate.size();
    	return heur;
    }
    
    /*calculate Heuristic */
    /* run though each block to calculate heuristic value
     * +0     : when a block is on the right place.
     * ----------------------------------------------------
     * when a block is on the wrong place
     * +1     : when a block is on the place where it should be empty: moving-out is required
     * +1     : when a block is not place where a block should be    : moving-in  is required 
     * +2     : when a block is on the something else's place        : moving-in and -out are required
     * +(n*2) : when above plus [n] block is on top of the node at wrong place : [n] moving-in and -out are required
     * 
     * */
    public int calHeur(Node goal){
    	int heur      = 0;
    	int minlength = 0;
    	int minsize   = goal.state.get(0).size();
    	int maxsize   = 0;
    	int avgsize   = 0;

    	if(this.equals(goal))
    		return heur;

    	/*calculate h*/ 	
    	for(int i=0;i<goal.state.size();i++){ 		
			Stack<Integer> subgoal  = goal.state.get(i);
			Stack<Integer> substate = state.get(i);
			
			maxsize = (state.get(i).size()>=maxsize) ? state.get(i).size() : maxsize;
			minsize = (state.get(i).size()<=minsize) ? state.get(i).size() : minsize;
			avgsize += state.get(i).size();
			minlength = (subgoal.size() >= substate.size()) ? substate.size() : subgoal.size();
			int j;    			
			for(j=0;j<minlength;j++){
				/*+2     : when a wrong block has occupied in the spot for the goal node
				 *+(n*2) : when above plus [n] blocks are on top of the node 
				 *+0     : when a right block has occupied in the right spot 
				 **/
				if((int)subgoal.get(j) != (int)substate.get(j)){
					heur += 2;
					if(substate.size()-(j+1)>0)
						heur += (substate.size()-j+1)*2;
					//System.out.println("subgoal.get(j) != substate.get(j) : Heru["+heur+"]");
				}
				
			}
			/*+1 : when the place where a block should be is empty*/
			if(subgoal.size()>minlength){
				for(;j<subgoal.size();j++){					
					heur +=1;
					//System.out.println("j<subgoal.size() : Heru["+heur+"]");
				}
			}else{
			/*+1 : when a block has occupied a place where it should be empty*/	
				for(;j<substate.size();j++){
					heur +=1;
					//System.out.println("j<SUBSTATE.size() : Heru["+heur+"]");
				}
			}
		}
    	avgsize = avgsize/goal.state.size();
    	
    	heur += maxsize-avgsize;
    	
    	return heur;
    }      
    /*get a Heuristic value*/
    public int getH(){
    	return this.h;
    }
    /*print a Node*/
    public void printNode(){

		for(int i=0;i<this.state.size();i++){
			System.out.format("%d | %s \n", i,this.state.get(i) );
		}
    }
    /*printSuccessors*/
    public void printSucessors(boolean isdetail){
    	if(!isdetail)
    		return;
    	
    	System.out.println("print successors");
    	if(this.successors == null || this.successors.size() == 0){
    		System.out.println("There is no successors");
    		return;
    	}
    	for(Node n : successors){
    		n.printNode();
    		System.out.println("==========================");
    	}
    	System.out.println("==========================");	
    		
    }
   /*@override hashCode*/
    public int hashCode(){
    	int hash = 1;
    	for(List<Integer> l : this.state){
    		for(Integer I : l)
    			hash = hash * 7 + l.size()*((int)I);
    	}
    	return hash;
    }
    
   /*@override equals*/
    public boolean equals(Object o){
    	
    	if((o instanceof Node )){
    		List<Stack<Integer>> s = ((Node)o).getState();
    		
    		if(s.size() == this.state.size()){
	    		for(int i=0;i<s.size();i++){
	    			Stack<Integer> ss       = s.get(i);
	    			Stack<Integer> substate = state.get(i);
	    			if(ss.size() == substate.size()){	    			
		    			for(int j=0;j<ss.size();j++){
		    				if((int)ss.get(j)!=(int)substate.get(j))
		    					return false;
		    			}
	    			}else
	    				return false;
	    		}
    		}else
    			return false;
    	}else
    		return false;
    	
    	return true;
    	
    }
    /*@override compareTo*/
    public int compareTo(Node n) {
		if (this.getG() + this.getH() < n.getG() + n.getH()  )
			return -1;
		else if ( n.getG() + n.getH() == this.getG() + this.getH())
			return 0;
		else 
			return 1;
    }
    
    /*track back to the root*/
    public void traceback() 
    {
    	Node n = this;
    	System.out.println("\nSolution path backward:");
    	while(n != null){
	   		System.out.format("[%d] Node \n" ,n.getG());
    		n.printNode();
    		System.out.println("");
	    	n = n.p;
    	}
    		
    }   
}
