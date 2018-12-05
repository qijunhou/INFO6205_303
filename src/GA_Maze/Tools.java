package GA_Maze;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Tools {
 
 public static int[] mutation(int[] point) {
  point[0] = (point[0] == 0 ? 1 : 0);
  point[1] = (point[1] == 0 ? 1 : 0);
  return point;
 }
 
 /**
  * binary array converts to numbers
  * 
  * @param binaryArray
  *           
  */
 public static int binaryArrayToNum(int[] binaryArray) {

  int result = 0;

  for (int i = binaryArray.length - 1, k = 0; i >= 0; i--, k++) {
   if (binaryArray[i] == 1) {
    result += Math.pow(2, k);
   }
  }
  
  return result;
 }
 
 public static ArrayList<String[]> readDataFile(String filePath){
  File file = new File(filePath);
  ArrayList<String[]> dataArray = new ArrayList<String[]>();
  try {
   BufferedReader in = new BufferedReader(new FileReader(file));
   String str;
   String[] tempArray;
   while ((str = in.readLine()) != null) {
    tempArray = str.split(" ");
    dataArray.add(tempArray);
   }
   in.close();
  } catch (IOException e) {
   e.getStackTrace();
  }
  return dataArray;
 }
}