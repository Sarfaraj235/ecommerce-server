package test;

import java.util.Arrays;

public class MatrixTransp {

	public static void main(String[] args) {
		
		//[[12,5] , [2,3], [45,7] ]= output : [[12,2,45] [5,3,7]]
		
		int[][] arr = {{12,5} , {2,3}, {45,7}};
		int rows = arr.length;
		int cols = arr[0].length;
		int[][] output = new int[cols][rows];
		
		
		for(int i = 0; i<rows; i++) {
			
			for(int j= 0; j <cols ; j++) {
				
				output[j][i] =arr[i][j]; 
				
			}
		
		}
		
		System.out.println(Arrays.deepToString(output));
		

	}

}
