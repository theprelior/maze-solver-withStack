package maze;

import java.util.Stack;

public class Solver_Ordinal {
	/**
	 * Solves the maze using a stack as an auxiliary data structures. <br>
	 * The solution is obtained by visiting every neighbor of the current top and looking for a path that contains 
	 * this neighbor. Any point none of whose neighbors yield a solution is popped from the stack. <br>
	 * NOTE-1: One solution for the input file "MazeConf1.txt": [[5,0], [5,1], [4,1], [3,1], [3,2], [3,3], [2,3], [1,3], [1,4], [1,5], [1,6], [1,7], [1,8]].
	 * NOTE-2: Notice that there might exist multiple solutions for an input. Any correct answer will be accepted. 
	 * NOTE-3: Use only <code>push</code>, <code>pop</code>, <code>peek</code> and <code>isEmpty</code> on <code>history</code>.
	 * NOTE-4: You may edit ONLY Solver_Ordinal.solve. Do not edit the other classes/methods.
	 * @param maze Maze to be solved
	 * @return The path from the entry point to the exit point (exit on the top, entry at the bottom)
	 */
	public static Stack<Point2D> solve(Maze maze) {
		//history of points that mark our path from start to the current position

	   Stack<Point2D> history = new Stack<>();		
		
	   history.add(maze.getEntry());

	   Stack<Point2D>path=pathFinder(maze.getEntry(),maze,history,new Point2D(maze.getEntry().getY(),maze.getEntry().getX()),new Point2D(maze.getEntry().getY(),maze.getEntry().getX()));
		
	   history=path;
	  
	   return history;
	
	}
	
	/*  Maze pathfinder
	 *  It's the common algorithm.Go forward and look at your way is wall or not.
	 *  It recursively works.
	 *  When you stuck go to backwards to last turn that is in turnpoint variable.
	 *  There are 4 ways to go.ABOVE, BELOW, RIGHT, LEFT look them all, then make your decision.
	 *  So, in method we are looking at four way to find the clear way to reach the point that is given. 
	 *  When it's found the clear way and is the way visited and also the way is in map,then add in stack and keep going look forward.
	 *  Then the snake marks the way where is initial pos and also back pos to don't turn back.
	 *  There is one stack that is named the history. It's saving the data where the <code>initial_pos</code> on.
	 *  There are also four decisions ,if the snake that is given a name by me has stuck. The decisions call the backward algorithm.
	 *  The backward algorithm gets two variables theese name are history(stack) and turnpoint(Point2D).
	 *  Eventually, the recursive algorithm returns the history stack to the solve method if initial_pos(curr) equals the exit position.
	 * */
	public static Stack<Point2D> pathFinder(Point2D curr,Maze maze,Stack<Point2D> history,Point2D turnpoint,Point2D old) {
		if(curr.equals(maze.getExit())) {
			return history;
		}
		else {
			Point2D turn=turnpoint;
			Point2D initial_pos=curr;
		
		
			if(!maze.isWall(new Point2D(curr.getY(),curr.getX()+1))&&!curr.isNeighborVisited(Point2D.Direction.RIGHT)
					&&!inSizeOfMap(new Point2D(curr.getY(),curr.getX()+1),maze)) {
				
				
				initial_pos.markNeighborVisited(Point2D.Direction.RIGHT);
				initial_pos=new Point2D(curr.getY(),curr.getX()+1);
				if(!maze.isWall(new Point2D(initial_pos.getY()+1,initial_pos.getX()))
						||!maze.isWall(new Point2D(initial_pos.getY()-1,initial_pos.getX()))){
					turn=initial_pos;
				}
				initial_pos.markNeighborVisited(Point2D.Direction.LEFT);
				history.add(initial_pos);
				
				return pathFinder(initial_pos,maze,history,turn,initial_pos);
			}

			if(!maze.isWall(new Point2D(curr.getY()-1,curr.getX()))&&!curr.isNeighborVisited(Point2D.Direction.ABOVE)
					&&!inSizeOfMap(new Point2D(curr.getY()-1,curr.getX()),maze)) {
				
				
				initial_pos.markNeighborVisited(Point2D.Direction.ABOVE);
				initial_pos=new Point2D(curr.getY()-1,curr.getX());
				if(!maze.isWall(new Point2D(initial_pos.getY(),initial_pos.getX()-1))
						||!maze.isWall(new Point2D(initial_pos.getY(),initial_pos.getX()+1))){
					turn=initial_pos;
				}
				initial_pos.markNeighborVisited(Point2D.Direction.BELOW);
				history.add(initial_pos);
				
				return pathFinder(initial_pos,maze,history,turn,initial_pos);

			}
			if(!maze.isWall(new Point2D(curr.getY()+1,curr.getX()))&&!curr.isNeighborVisited(Point2D.Direction.BELOW)
					&&!inSizeOfMap(new Point2D(curr.getY()+1,curr.getX()),maze)) {
				initial_pos.markNeighborVisited(Point2D.Direction.BELOW);
				initial_pos=new Point2D(curr.getY()+1,curr.getX());
				if(!maze.isWall(new Point2D(initial_pos.getY(),initial_pos.getX()+1))
						||!maze.isWall(new Point2D(initial_pos.getY(),initial_pos.getX()-1))){
					turn=initial_pos;
				}
				initial_pos.markNeighborVisited(Point2D.Direction.ABOVE);
				history.add(initial_pos);
				
				
				return pathFinder(initial_pos,maze,history,turn,initial_pos);
			}
			if(!maze.isWall(new Point2D(curr.getY(),curr.getX()-1))&&!curr.isNeighborVisited(Point2D.Direction.LEFT)
					&&!inSizeOfMap(new Point2D(curr.getY(),curr.getX()-1),maze)) {
				initial_pos.markNeighborVisited(Point2D.Direction.LEFT);
				initial_pos=new Point2D(curr.getY(),curr.getX()-1);
				if(!maze.isWall(new Point2D(initial_pos.getY()+1,initial_pos.getX()))
						||!maze.isWall(new Point2D(initial_pos.getY()-1,initial_pos.getX()))){
					turn=initial_pos;
				}
				initial_pos.markNeighborVisited(Point2D.Direction.RIGHT);
				history.add(initial_pos);
				
				
				return pathFinder(initial_pos,maze,history,turn,initial_pos);
			}
			
			/*
			 * The four decisions are looking for the initial position if stuck.
			 * And call the backward algorithm.
			 * */
			
			if(maze.isWall(new Point2D(curr.getY(),curr.getX()+1))&&initial_pos.isNeighborVisited(Point2D.Direction.LEFT)){
				history=backtracking(history,turn);
				initial_pos=history.peek();
				return pathFinder(initial_pos,maze,history,turn,initial_pos);
				
			}
			if(maze.isWall(new Point2D(curr.getY()-1,curr.getX()))&&initial_pos.isNeighborVisited(Point2D.Direction.BELOW)){
				history=backtracking(history,turn);
				initial_pos=history.peek();
				return pathFinder(initial_pos,maze,history,turn,initial_pos);
			}
			if(maze.isWall(new Point2D(curr.getY()+1,curr.getX()))&&initial_pos.isNeighborVisited(Point2D.Direction.ABOVE)){
				history=backtracking(history,turn);
				initial_pos=history.peek();
				return pathFinder(initial_pos,maze,history,turn,initial_pos);
			}
			if(maze.isWall(new Point2D(curr.getY(),curr.getX()-1))&&initial_pos.isNeighborVisited(Point2D.Direction.RIGHT)){
				history=backtracking(history,turn);
				initial_pos=history.peek();
				return pathFinder(initial_pos,maze,history,turn,initial_pos);
			}

			

			
		}
		
		
		return history;
	}
	
	/*
	 * The backtrack algorithm 
	 * Name is backtracking and gets two variable.
	 * It's called by pathfinder recursive algorithm if the snake has stuck. 
	 * The while loop pop(remove) the stacks until find the turn point stack.
	 * Then return the last ways with stack.
	 * */
	public static Stack<Point2D> backtracking(Stack<Point2D> history ,Point2D searching) {
		
		while(!history.peek().equals(searching)) {
			if(history.peek().equals(searching)) break;
			history.pop();
		}
		return history;
	}
	
	
	/*
	 * It controls the point that is given with the maze if it is in range or not.
	 * */

	public static boolean inSizeOfMap(Point2D size,Maze maze) {
		int x =size.getX();
		int y =size.getY();
		if(x<maze.getNumRows() && 0>=x && y>maze.getNumCols() && 0>=y) {
			return true;
		}
		return false;
	}
	
	


	public static void main(String[] args) throws Exception{
		//read the configuration file
		Maze maze = new Maze("mazeConf3.txt");
		//output the configuration version of the maze
		System.out.println(maze.toString());
	
		//solve the maze
		Stack<Point2D> solution = solve(maze);
		//output the solution
	
		System.out.println(solution + "\n");
		
		//output the maze such that every visited point of the solution
		//   is marked with an "X".
		String output = "";
		for(int i = 0; i < maze.getNumRows(); i++) {
			String line = "Row " + i + ":\t";
			for(int j = 0; j < maze.getNumCols(); j++) {
				Point2D curr = new Point2D(i, j);
				if(maze.isWall(curr)) {
					line = line + "1";
				} else if(solution.contains(curr)) {
					line = line + "X";
				} else {
					line = line + "0";
				}
			}
			output = output + line + "\n";
		}
		System.out.println(output);
	}
}
