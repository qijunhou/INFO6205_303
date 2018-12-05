package GA_Maze;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Application of genetic algorithm in maze game 
 * 
 * @author hou ,wang
 * 
 */
public class GA {
 // Maze entrance marks
	
 public static final int MAZE_ENTRANCE_POS = 1;
 public static final int MAZE_EXIT_POS = 2;
 // The code arrays corresponding to the direction
 public static final int[][] MAZE_DIRECTION_CODE = new int[][] { { 0, 0 },
   { 0, 1 }, { 1, 0 }, { 1, 1 }, };
 // coordinates change
 public static final int[][] MAZE_DIRECTION_CHANGE = new int[][] {
   { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, };
 // Description of the direction
 public static final String[] MAZE_DIRECTION_LABEL = new String[] { "Up",
   "Down", "Left", "Right" };

 // map data file address
 private String filePath;
 // shortest path in the maze
 private int stepNum;
 // The number of initial individuals
 private int initSetsNum;
 // Maze entrance location
 private int[] startPos;
 // Maze exit location
 private int[] endPos;
 // Maze map data
 private int[][] mazeData;
 // Initial set of individuals
 private ArrayList<int[]> initSets;
 //Random number generator
 private Random random;
 //Mutation rate
 private double mutationRate;

 public GA(String filePath, int initSetsNum) {
  this.filePath = filePath;
  this.initSetsNum = initSetsNum;

  readDataFile();
 }

 /**
  * read data from a file
  */
 public void readDataFile() {
  
  ArrayList<String[]> dataArray = Tools.readDataFile(filePath);

  int rowNum = dataArray.size();
  mazeData = new int[rowNum][rowNum];
  for (int i = 0; i < rowNum; i++) {
   String[] data = dataArray.get(i);
   for (int j = 0; j < data.length; j++) {
    mazeData[i][j] = Integer.parseInt(data[j]);

    // Assigns entry and exit positions
    if (mazeData[i][j] == MAZE_ENTRANCE_POS) {
     startPos = new int[2];
     startPos[0] = i;
     startPos[1] = j;
    } else if (mazeData[i][j] == MAZE_EXIT_POS) {
     endPos = new int[2];
     endPos[0] = i;
     endPos[1] = j;
    }
   }
  }

  // Calculate the minimum number of steps out of the maze
  stepNum = Math.abs(startPos[0] - endPos[0])
    + Math.abs(startPos[1] - endPos[1]);
  
  //Initialize the mutation rate.
  mutationRate=0.01;
 }

 /**
  * Generates the initial data set
  */
 private void produceInitSet() {
  // The direction of coding
  int directionCode = 0;
  random = new Random();
  initSets = new ArrayList<>();
  // The operation of each step needs to be represented by two digits
  int[] codeNum;

  for (int i = 0; i < initSetsNum; i++) {
   codeNum = new int[stepNum * 2];
   for (int j = 0; j < stepNum; j++) {
    directionCode = random.nextInt(4);
    codeNum[2 * j] = MAZE_DIRECTION_CODE[directionCode][0];
    codeNum[2 * j + 1] = MAZE_DIRECTION_CODE[directionCode][1];
   }

   initSets.add(codeNum);
  }
 }

 /**
  * Selection operation, the fitness of the higher individual priority inherited to the next generation
  * 
  * @param initCodes
  *           Initial individual coding
  * @return
  */
 private ArrayList<int[]> selectOperate(ArrayList<int[]> initCodes) {
  double randomNum = 0;
  double sumFitness = 0;
  ArrayList<int[]> resultCodes = new ArrayList<>();
  double[] adaptiveValue = new double[initSetsNum];

  for (int i = 0; i < initSetsNum; i++) {
  
   adaptiveValue[i] = calFitness(initCodes.get(i));
   sumFitness += adaptiveValue[i];
  }

  // convert it to probability, normalize it
  for (int i = 0; i < initSetsNum; i++) {
   adaptiveValue[i] = adaptiveValue[i] / sumFitness;
  }

  for (int i = 0; i < initSetsNum; i++) {
   randomNum = random.nextInt(100) + 1;
   randomNum = randomNum / 100;
   //since 1.0 cannot be judged, the sum will be infinitely close to 1.0 and 0.99 will be used for judgment
   if(randomNum == 1){
    randomNum = randomNum - 0.01;
   }
   
   sumFitness = 0;
   // confidence interval
   for (int j = 0; j < initSetsNum; j++) {
    if (randomNum > sumFitness
      && randomNum <= sumFitness + adaptiveValue[j]) {
     // Avoid duplication of references by copying
     resultCodes.add(initCodes.get(j).clone());
     break;
    } else {
     sumFitness += adaptiveValue[j];
    }
   }
  }

  return resultCodes;
 }

 /**
  * crossover calculation
  * 
  * @param selectedCodes
  *            Coding after the selection of the previous step
  * @return
  */
 private ArrayList<int[]> crossOperate(ArrayList<int[]> selectedCodes) {
  int randomNum = 0;
  // crossover point
  int crossPoint = 0;
  ArrayList<int[]> resultCodes = new ArrayList<>();
  // random crossover
  ArrayList<int[]> randomCodeSeqs = new ArrayList<>();

  // random sorting
  while (selectedCodes.size() > 0) {
   randomNum = random.nextInt(selectedCodes.size());

   randomCodeSeqs.add(selectedCodes.get(randomNum));
   selectedCodes.remove(randomNum);
  }

  int temp = 0;
  int[] array1;
  int[] array2;

  for (int i = 1; i < randomCodeSeqs.size(); i++) {
   if (i % 2 == 1) {
    array1 = randomCodeSeqs.get(i - 1);
    array2 = randomCodeSeqs.get(i);
    crossPoint = random.nextInt(stepNum - 1) + 1;

   
    for (int j = 0; j < 2 * stepNum; j++) {
     if (j >= 2 * crossPoint) {
      temp = array1[j];
      array1[j] = array2[j];
      array2[j] = temp;
     }
    }


    resultCodes.add(array1);
    resultCodes.add(array2);
   }
  }

  return resultCodes;
 }

 /**
  * mutation
  * 
  * @param crossCodes
  *          results after crossover
  * @return
  */
 private ArrayList<int[]> variationOperate(ArrayList<int[]> crossCodes) {
  // Mutation point
  int variationPoint = 0;
  ArrayList<int[]> resultCodes = new ArrayList<>();

  for (int[] array : crossCodes) {
	  if(random.nextDouble() < mutationRate) {
   variationPoint = random.nextInt(stepNum);

   for (int i = 0; i < array.length; i += 2) {
    // mutate
    if (i % 2 == 0 && i / 2 == variationPoint) {
     int[] temp = {array[i], array[i+1]};
     temp = Tools.mutation(temp);
     array[i] = temp[0];
     array[i + 1] = temp[1];
     break;
    }
   }
  }
   resultCodes.add(array);
  }

  return resultCodes;
 }

 /**
  * calculate the fitness
  * 
  * @param code
  *            current code
  * @return
  */
 public double calFitness(int[] code) {
  double fitness = 0;
  // endpoint X calculated by coding
  int endX = 0;
  // endpoint Y calculated by coding
  int endY = 0;
  // walking direction Based on representation of fragment
  int direction = 0;
  // temporary x coordinates
  int tempX = 0;
  // temporary y coordinates
  int tempY = 0;

  endX = startPos[0];
  endY = startPos[1];
  for (int i = 0; i < stepNum; i++) {
   direction = binaryArrayToNum(new int[] { code[2 * i],
     code[2 * i + 1] });

   // coordinates changing base on the direction changing
   tempX = endX + MAZE_DIRECTION_CHANGE[direction][0];
   tempY = endY + MAZE_DIRECTION_CHANGE[direction][1];

   // Determine whether the coordinate point is out of bounds
   if (tempX >= 0 && tempX < mazeData.length && tempY >= 0
     && tempY < mazeData[0].length) {
    // Determines whether the coordinate point has reached the blocking block
    if (mazeData[tempX][tempY] != -1) {
     endX = tempX;
     endY = tempY;
    }
   }
  }

  // using  fitness function to calculate the fitness
  fitness = 1.0 / (Math.abs(endX - endPos[0])
    + Math.abs(endY - endPos[1]) + 1);
  return fitness;
 }

 /**
  * Determine whether the exit location has been found according to the current code
  * 
  * @param code
  *          It goes through several genetic codes
  * @return
  */
 private boolean ifArriveEndPos(int[] code) {
  boolean isArrived = false;
//endpoint X calculated by coding
 int endX = 0;
 // endpoint Y calculated by coding
 int endY = 0;
 // walking direction Based on representation of fragment
 int direction = 0;
 // temporary x coordinates
 int tempX = 0;
 // temporary y coordinates
 int tempY = 0;


  endX = startPos[0];
  endY = startPos[1];
  for (int i = 0; i < stepNum; i++) {
   direction = binaryArrayToNum(new int[] { code[2 * i],
     code[2 * i + 1] });

   //  coordinates changing base on the direction changing
   tempX = endX + MAZE_DIRECTION_CHANGE[direction][0];
   tempY = endY + MAZE_DIRECTION_CHANGE[direction][1];

// determine whether the coordinate point is out of bounds
   if (tempX >= 0 && tempX < mazeData.length && tempY >= 0
     && tempY < mazeData[0].length) {
	   // Determines whether the coordinate point has reached the blocking block
    if (mazeData[tempX][tempY] != -1) {
     endX = tempX;
     endY = tempY;
    }
   }
  }

  if (endX == endPos[0] && endY == endPos[1]) {
   isArrived = true;
  }

  return isArrived;
 }

 /**
  *Binary arrays converted to Numbers
  * 
  * @param binaryArray
  *           
  */
 private int binaryArrayToNum(int[] binaryArray) {
  int result = 0;

  for (int i = binaryArray.length - 1, k = 0; i >= 0; i--, k++) {
   if (binaryArray[i] == 1) {
    result += Math.pow(2, k);
   }
  }

  return result;
 }

 /**
  * using Genetic algorithm to get out of the maze
  */
 public void goOutMaze() {
  // Iteration number
  int loopCount = 0;
  boolean canExit = false;
  //result of the path
  int[] resultCode = null;
  ArrayList<int[]> initCodes;
  ArrayList<int[]> selectedCodes;
  ArrayList<int[]> crossedCodes;
  ArrayList<int[]> variationCodes;

  // Generates the initial data set
  produceInitSet();
  initCodes = initSets;

  while (true) {
   for (int[] array : initCodes) {
    // The termination condition of genetic iteration is whether the exit position is found
    if (ifArriveEndPos(array)) {
     resultCode = array;
     canExit = true;
     break;
    }
   }

   if (canExit) {
    break;
   }

   selectedCodes = selectOperate(initCodes);
   crossedCodes = crossOperate(selectedCodes);
   variationCodes = variationOperate(crossedCodes);
   initCodes = variationCodes;

   loopCount++;
   
   //If the number of heredity is more than 100, it stops.
   if(loopCount >= 100){
    break;
   }
  }

  System.out.println("There are" +" "+loopCount+" "+"genetic evolutions");
  printFindedRoute(resultCode);
 }

 /**
  * output the path that found
  * 
  * @param code
  */
 private void printFindedRoute(int[] code) {
  if(code == null){
   System.out.println("In the limited number of genetic evolution, no optimal path was found");
   return;
  }
  
  int tempX = startPos[0];
  int tempY = startPos[1];
  int direction = 0;

  System.out.println(MessageFormat.format(
    "Starting point position ({0},{1}), exit point position ({2}, {3})", tempX, tempY, endPos[0],
    endPos[1]));
  
  System.out.print("Code of searching results：");
  for(int value: code){
   System.out.print("" + value);
  }
  System.out.println();
  
  for (int i = 0, k = 1; i < code.length; i += 2, k++) {
   direction = binaryArrayToNum(new int[] { code[i], code[i + 1] });

   tempX += MAZE_DIRECTION_CHANGE[direction][0];
   tempY += MAZE_DIRECTION_CHANGE[direction][1];

   System.out.println(MessageFormat.format(
     "Step {0}, code {1}{2}, move  {3}, move to ({4},{5})", k, code[i], code[i+1],
     MAZE_DIRECTION_LABEL[direction],  tempX, tempY));
  }
 }
}