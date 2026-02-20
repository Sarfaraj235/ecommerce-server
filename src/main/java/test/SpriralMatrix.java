package test;

import java.util.Arrays;

public class SpriralMatrix {

	public static void main(String[] args) {


		int[][] arr = { { 1, 2, 3, 4 },
				             { 5, 6, 7, 8 },
				          { 9, 10, 11, 12 } };


		spriralPrint(arr);

	}

	private static void spriralPrint(int[][] arr) {

		int top = 0;
		int bottom = arr.length - 1;
		int left = 0;
		int right = arr[0].length - 1;

		while (top <= bottom && left <= right) {

			// left to right

			for (int i = left; i <= right; i++) {
				System.out.print(arr[top][i] + " ");
			}
			top++;

			// top to bottom

			for (int i = top; i <= bottom; i++) {
				System.out.print(arr[i][right] + " ");
			}
			right--;

			// right to left
			if (top <=bottom) {
				for (int i = right; i >= left; i--) {
					System.out.print(arr[bottom][i] + " ");
				}
				bottom--;
			}

			// bottom to top
			if (left <=right) {
				for (int i = bottom; i >= top; i--) {
					System.out.print(arr[i][left] + " ");
				}
			}
				left++;
			
		}

	}

}
