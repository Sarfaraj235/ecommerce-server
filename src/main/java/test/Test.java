package test;

class Test{
	

	public static void main(String[] args) {
		
		int n= 7;
		
		int mid = n /2;
		
		for(int i = 0; i<n ; i++) {
			
			for(int j= 0; j<n; j++ ) {
				
				if(i<=mid) {
					
					if(j == mid - i || j == mid + i){
						System.out.print("*");
					}else {
						System.out.print(" ");
					}
					
				}
				else {
					if(j== i - mid || j == (3*mid) - i){
						System.out.print("*");
					}
					else {
						System.out.print(" ");
					}
				}
			}
			System.out.println();

		}
		
	}
	
	
}