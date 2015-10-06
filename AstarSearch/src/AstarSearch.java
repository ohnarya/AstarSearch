import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class AstarSearch {
	public static void main(String args[]){

		if(!checkInput(args))
			return;

		AstarSearch astar    = new AstarSearch(Integer.parseInt(args[0]),          /*blocks*/
				                               Integer.parseInt(args[1]),          /*stacks*/
				                               Boolean.parseBoolean(args[2]),      /*isdetail*/
				                               Boolean.parseBoolean(args[3]),      /*israndom*/
				                               Boolean.parseBoolean(args[4])       /*isbaseline*/
		/*search a goal with A* search */		                             );
		Node        reached  = astar.Search();
		
		/*print statistics */
		astar.report();
		/*print nodes from initial state to the goal state*/
		reached.traceback();
	}
	
	int     stacks;
	int     blocks;
	int     iter;
	int     maxfrontier;
	int     depth;
	boolean isdetail;
	boolean israndom;
	boolean isbaseline;
	
	PriorityQueue<Node> frontier    = new PriorityQueue<Node>(); /*frontier*/	
	HashSet<Node>       explored    = new HashSet<Node>();       /*explored*/	
	Node                goal        = new Node();                /*Goal Node*/
	Node                InitNode    = new Node();	             /*Init Node*/
	
	AstarSearch(){
		this.iter        = 0;
		this.maxfrontier = 0;
		this.depth       = 0;
	}
	AstarSearch(int blocks, int stacks, boolean isdetail, boolean israndom, boolean isbaseline){
		this.stacks      = stacks;
		this.blocks      = blocks;
		this.iter        = 0;
		this.maxfrontier = 0;
		this.isdetail    = isdetail;
		this.israndom    = israndom;
		this.isbaseline  = isbaseline;
	}
	
	public Node Search(){
		Node       n = new Node();
		BlockWorld b = new BlockWorld();
		
		/*set a goal node*/
		goal           = b.getGoal(blocks,stacks);
		/*initial Node*/
		InitNode.state = b.generateInitialProblem(blocks,stacks,israndom);  	
		InitNode.setG(0);
		InitNode.setH(goal);

		frontier.add(InitNode);
		explored.add(InitNode);
		
		while(!frontier.isEmpty()){
			n = null;
			
			/*<optional>printaFrontier*/
			printFrontier(frontier);
			
			n = frontier.poll();
			
			this.depth = n.getG();
			printSearchProcess(frontier,n);
			maxfrontier = maxfrontier > frontier.size() ? maxfrontier :frontier.size();
			
			/*goal test*/
			if(n.equals(goal)){
				System.out.println("Initial state :");
				InitNode.printNode();
				System.out.print("SUCCESS!  ");
				
				break;
			}	
			
			this.iter++;
			
			/*get successors of a node*/
			n.getSuccessor(goal,stacks,isbaseline);
			n.printSucessors(isdetail);
			
			addFrontier(frontier, explored, n.successors);
			
			/*<optional> print explorer*/
			printExplored(explored);			

			
		}
		
		return n;
	}
	/*add a node to frontier*/
	public void addFrontier(PriorityQueue<Node> frontier, HashSet<Node> explored, Node successor){
		if(!frontier.contains(successor))
			frontier.add(successor);
		
	}
	/*add a list of nodes to frontiner*/
	public void addFrontier(PriorityQueue<Node> frontier, HashSet<Node> explored, List<Node> successors){		
	    for(Node s : successors){
	    	
			if(frontier.contains(s))
				continue;
	    	
			if(explored.contains(s))				
				continue;
			else
				explored.add(s);
			
			frontier.add(s);
	    	if(isdetail)
	    	  s.printNode();	
	    }		
	}
	
	/*add a node to explored*/
	public void addExplored(HashSet<Node> explored,Node n){
		if(!explored.contains(n))
			explored.add(n);
	}
	
	/*add a list of node to explored*/
	public void addExplored(HashSet<Node> explored, List<Node> successors){
		
	    for(Node s : successors){
			if(explored.contains(s))	
				continue;
			
			explored.add(s);
	    }
	}
	public void printFrontier(PriorityQueue<Node> frontier){
		if(!isdetail)
			return;
		System.out.println("***printFrontier is called");
		Iterator<Node> I = frontier.iterator();
		while(I.hasNext()){
			Node n = I.next();
			n.printNode();
			System.out.println("***printFrontier ends");
			
		}
		
	}	
	/*print process*/
	public void printSearchProcess(PriorityQueue<Node> frontier, Node n){
    	System.out.format("iter = %d, frontier=%d, f=g+h = %d (%d+%d) depth=%d\n",
		                   iter,      frontier.size(),  n.getG()+ n.getH(), n.getG(), n.getH(),n.getG());
    	if(isdetail){
	    	System.out.println("Poped ");
	    	n.printNode();
    	}
	}
	public void report(){
		System.out.format("depth=%d, total_goal_tests=%d, max_queue_size=%d\n",
						   this.depth, this.iter, this.maxfrontier 
				          );
	}
	public void printExplored(HashSet<Node> explored){
		if(!isdetail)
			return;
		
		System.out.println("*****explored nodes");
		Iterator<Node> I = explored.iterator();
		while(I.hasNext()){
			Node n = I.next();
			n.printNode();
			System.out.println("~~~~~~~~~~~~~~~~");
			
		}
		System.out.println("*****explored nodes ends");
		
	}
	
	public static boolean checkInput(String args[]){

		if(args.length != 5){
			System.out.println("1 Input Example :  5 3 false(or true) true(or false) true(or false) true(or false) ");
			System.out.println("                   blocks  stacks print_detail random(or example)");
			return false;
		}
		if(    !args[0].matches("\\d+")
	        || !args[1].matches("\\d+")){
			System.out.println("2 Input Example :  5 3 false(or true) true(or false) true(or false) true(or false) ");
			System.out.println("                   blocks  stacks print_detail random(or example)");
			return false;
		}
		if(  ! ((args[2].equals("false"))
		    ||  (args[2].equals("true" )))){
			System.out.println("3 Input Example :  5 3 false(or true) true(or false) true(or false) true(or false) ");
			System.out.println("                   blocks  stacks print_detail random(or example)");
			return false;
		}
		if(  ! ((args[3].equals("false"))
			||  (args[3].equals("true" )))){
				System.out.println("4 Input Example :  5 3 false(or true) true(or false) true(or false) true(or false) ");
				System.out.println("                   blocks  stacks print_detail random(or example)");
				return false;
		}
		if(  ! ((args[4].equals("false"))
			||  (args[4].equals("true" )))){
					System.out.println("5 Input Example :  5 3 false(or true) true(or false) true(or false) true(or false) ");
					System.out.println("                   blocks  stacks print_detail random(or example)");
					return false;
		}
		return true;
	}
	
}