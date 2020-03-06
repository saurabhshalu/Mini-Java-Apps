import java.util.Scanner;

public class ConnectFour {

	private static int turn = 0; //0 -> RED ; 1 -> YELLOW
	private static boolean status = false; //'false' to continue game and 'true' to stop and display result

	private static char[][] board = {{' ', ' ', ' ', ' ', ' ', ' ', ' '},{' ', ' ', ' ', ' ', ' ', ' ', ' '},{' ', ' ', ' ', ' ', ' ', ' ', ' '},{' ', ' ', ' ', ' ', ' ', ' ', ' '},{' ', ' ', ' ', ' ', ' ', ' ', ' '},{' ', ' ', ' ', ' ', ' ', ' ', ' '}};
	private static void printBoard(){
		try {new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();}catch(Exception e){}
		for(int i=0;i<6;i++) {
			for(int j=0;j<7;j++) {
					System.out.print("|" + board[i][j]);
			}
			System.out.print("|\n");
		}
		System.out.println("---------------");
	}

	private static void playGame() {
		Scanner keyboard = new Scanner(System.in);
		while(!status) {
			int input, valid=1;
			do {
				if(valid==1)
					printBoard();
				System.out.print("Drop a " + (turn==0?"red":"yellow") + " disk at column (0-6): ");
				while(!keyboard.hasNext("[0-6]"))
					keyboard.next();
				input = keyboard.nextInt();

				if(board[0][input]==' ')
					break;
				else {
					valid = 0;
					System.out.println("\nThe place is already taken! Please try different location!\n");		
				}
			} while(true);


			for(int i=5;i>=0;i--) {
				if(board[i][input]==' ') {
					board[i][input]=(turn==0?'R':'Y');
					if(turn==0)
						turn=1;
					else
						turn=0;
					break;
				}
			}

			char winState = checkWin();
			if(winState!=' ') {
				printBoard();
				status = true;
				if(winState == 'D')
					System.out.println("Match Draw!");
				else
					System.out.println("The " + (winState=='R'?"red":"yellow") + " player won!");
			}
		}
	}

	private static char checkWin() {
		for(int i=5;i>=0;i--)
			for(int j=0;j<=3;j++)
				if(board[i][j] != ' ' && board[i][j] == board[i][j+1] && board[i][j+2] == board[i][j+3] && board[i][j+1] == board[i][j+2])
					return board[i][j];

		for(int i=0;i<=2;i++)
			for(int j=0;j<=6;j++)
				if(board[i][j] != ' ' && board[i][j] == board[i+1][j] && board[i+1][j] == board[i+2][j] && board[i+2][j] == board[i+3][j])
					return board[i][j];

		for(int i=0;i<=2;i++) {
			for(int j=0;j<7;j++) {
				if(j-3>=0)
					if(board[i][j] != ' ' && board[i][j] == board[i+1][j-1] && board[i+1][j-1] == board[i+2][j-2] && board[i+2][j-2] == board[i+3][j-3])
						return board[i][j];

				if(j+3<=6)
					if(board[i][j] != ' ' && board[i][j] == board[i+1][j+1] && board[i+1][j+1] == board[i+2][j+2] && board[i+2][j+2] == board[i+3][j+3])
						return board[i][j];
			}
		}

		for(int i=0;i<6;i++)
			for(int j=0;j<7;j++)
				if(board[i][j]==' ')
					return ' '; //return if cell is still empty


		return 'D'; //return if DRAW

	}

	public static void main(String[] args) {
		playGame();
	}
}