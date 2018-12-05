package GA_Maze;

 
/**
 * genetic algorithm 
 * @author hou wang
 *
 */
public class client {
	public static void main(String[] args) {
		//file path of the maze.
		String filePath = "data/maze1.txt";
		String filePath2="data/maze2.txt";
		String filePath3="data/maze3.txt";
		String filePath4="data/maze4.txt";
						
		//initialize the individual
		int initSetsNum = 1000;
		
		GA tool = new GA(filePath, initSetsNum);
		GA tool2 = new GA(filePath2, initSetsNum);
		GA tool3 = new GA(filePath3, initSetsNum);
		GA tool4 = new GA(filePath4, initSetsNum);
		tool.goOutMaze();
		tool2.goOutMaze();
		tool3.goOutMaze();
		tool4.goOutMaze();
		
		
	}
 
}


