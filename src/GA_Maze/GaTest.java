package GA_Maze;
/**
 * 
 */



import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



/**
 * @author hou
 *
 */
class GaTest {


	@Test
	void gene_testup() {
	
		int direction =Tools.binaryArrayToNum(new int[] {0,0});
		
		String result = GA.MAZE_DIRECTION_LABEL[direction];
		assertEquals( "Up",result);
	}       
	@Test
	void gene_testdown() {
		int direction = Tools.binaryArrayToNum(new int[] {0,1});
		String result = GA.MAZE_DIRECTION_LABEL[direction];
		
		assertEquals("Down",result);
	}
	
	@Test
	void gene_testleft() {
		int direction =Tools.binaryArrayToNum(new int[] {1,0});
		
		String result = GA.MAZE_DIRECTION_LABEL[direction];
		 assertEquals( "Left",result);
	}
	@Test
	void gene_testright() {
		int direction = Tools.binaryArrayToNum(new int[] {1,1});
		
		String result = GA.MAZE_DIRECTION_LABEL[direction];
		
	assertEquals( "Right",result);
	}
	
	@Test //fitness test
	void fitness_test() {
		String filePath = "data/maze1.txt";
		// initial number of individuals
		int initSetsNum = 1000;
		
		GA tool = new GA(filePath, initSetsNum);
		int[] testCode = {1,0,1,0,1,0,1,0,0,0,0,0,0,0};
        double result = tool.calFitness(testCode);
		//System.out.println(result);
       Assertions.assertEquals(1.0, result);
}
	@Test //fitness test
	void fitness_test2() {
		String filePath = "data/maze4.txt";
		// initial number of individuals
		int initSetsNum = 1000;
		
		GA tool = new GA(filePath, initSetsNum);
		int[] testCode = {0,0,1,1,0,0,0,0,1,1,1,1};
        double result = tool.calFitness(testCode);
		
       Assertions.assertEquals(1.0, result);
}
	
	@Test //mutation test
	void mutation_test() {
		int[] test_data = {0,1};
		int[] mutation= {1,0};
        int[] result=  Tools.mutation(test_data);
        assertTrue(Arrays.equals(mutation, result));
      }
	@Test //mutation test
	void mutation_test2() {
		int[] test_data = {0,0};
		int[] mutation= {1,1};
        int[] result=  Tools.mutation(test_data);
        assertTrue(Arrays.equals(mutation, result));
      }

}

