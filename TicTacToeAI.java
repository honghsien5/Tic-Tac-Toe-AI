/**
 Name: TicTacToeAI.java
 Description: TicTacToe game implementation to demonstrate minimax and alpha beta pruning algorithms
 Author: Shien Hong
 Date: Nov 10 2014
 
 */
import java.util.ArrayList;
import java.util.List;


class TicTacToe {
	String board;
	int size ;
	int player;
	TicTacToe(){
		board ="_________";
		size =0;
		player = 0;//0 is X.  1 is O.
	}
	TicTacToe(String str){
		if(!(checkStr(str))){
			board ="_________";
			size =0;
			player = 0;//0 is X.  1 is O.
			System.out.println(str +": string is in the incorrent format.");
			System.out.println("Initialized to default state.");
		}else{
			board=str;
			size = countSize(str);
			player = size%2;
		}
	}
	boolean checkStr(String str){
		if(str.length()==9){
			for(int i=0; i < 9;i++){
				char temp = str.charAt(i);
				if(temp == '_' || temp == 'X' || temp =='O'){
					
				}else{
					return false;
				}
				
			}
			return true;
		}else{
			return false;
		}
	}
	String getBoard(){
		return board;
	}
	int getPlayer(){
		return player;
	}
	int countSize(String str){
		int count =0;
		for(int i = 0 ; i < str.length();i++){
			if(str.charAt(i) == '_'){
				count++;
			}
		}
		return 9-count;
	}
	void read(String str){
		if(!checkStr(str)){
			System.out.println(str +": string is in the incorrent format.");
		}else{
			board=str;
			size = countSize(str);
			player = size%2;
		}
	}
	
	void print(){
		for(int i = 0 ; i < 9;i+=3){
			System.out.println(board.substring(i,i+3));
		}
	}
	String move(int n){
		if(player == 0 ){
			return board.substring(0,n) + "X" + board.substring(n+1);
		}else{
			return board.substring(0,n) + "O" + board.substring(n+1);
		}
	}
	TicTacToe newMove(int n){
		if(player == 0 ){
			return new TicTacToe(board.substring(0,n) + "X" + board.substring(n+1));
		}else{
			return new TicTacToe(board.substring(0,n) + "O" + board.substring(n+1));
		}
	}
	List<Integer> availableMoves(){
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0 ; i < board.length();i++){
			if(board.charAt(i)=='_'){
				list.add(i);
			}
		}
		return list;
	}
	int gameEnd(){
		int[] count = new int[16];
		//horizontal check
		for(int i = 0 ; i <= 6 ; i+=3){
			for(int j = 0 ; j < 3;j++){
				if(board.charAt(i+j) == 'X'){
					count[0+i/3]++;
				}
				if(board.charAt(i+j) == 'O'){
					count[8+i/3]++;
				}
			}
		}
		//vertical check
		for(int i = 0 ; i < 3;i++){
			for(int j = 0 ; j <=6; j+=3){
				if(board.charAt(i+j) == 'X'){
					count[3+i]++;
				}
				if(board.charAt(i+j) == 'O'){
					count[11+i]++;
				}
			}
		}
		//diagonal top left to bottom right
		for(int i = 0; i <=8;i+=4){
			if(board.charAt(i)=='X'){
				count[6]++;
			}
			if(board.charAt(i)=='O'){
				count[14]++;
			}
		}
		//diagonal top left to bottom right
		for(int i = 2; i <=6;i+=2){
			if(board.charAt(i)=='X'){
				count[7]++;
			}
			if(board.charAt(i)=='O'){
				count[15]++;
			}
		}
		
		//return the result if any count is equal to 3
		for(int i = 0 ; i < 16 ;i++){
			if(count[i] == 3){
				if(i <=7){
					return 0;
				}else{
					return 1;
				}
			}
		}
		//tie when no winner and size is 9
		if(size ==9){
			return 2; // tie
		}else{ //not ended
			return 3;
		}
	}
}


public class TicTacToeAI {
	static int score(TicTacToe game){
		if(game.gameEnd() ==0){
			return 1;
		}else if(game.gameEnd() == 1){
			return -1;
		}else{
			return 0;
		}
		
	}
	static int minScore(List<Integer> list){
		int min = list.get(0);
		for(int n : list){
			if(n<min){
				min = n;
			}
		}
		return min;
	}
	static int maxScore(List<Integer> list){
		int max = list.get(0);
		for(int n : list){
			if(n>max){
				max = n;
			}
		}
		return max;
	}
	
	static int count;
	static void minimax(TicTacToe game){
		count = 0;
		System.out.println("Game Result:" +minimaxHelper(game));
		System.out.println("Moves considered without alpha-beta pruning: "+count);
		
	}
	static int minimaxHelper(TicTacToe game){
		int winner = game.gameEnd();
		if(winner !=3){
			count++;
			return score(game);
		}
		List<Integer> children = game.availableMoves();
		List<Integer> scores = new ArrayList<Integer>();
		for(int n : children){
			scores.add(minimaxHelper(game.newMove(n)));
		}
		if(game.getPlayer() ==0){
			return maxScore(scores);
		}else{
			return minScore(scores);
		}
		
	}
	static int alphaCount;
	static int betaCount;
	static void minimaxAlphaBeta(TicTacToe game){
		count=0;
		alphaCount=0;
		betaCount=0;
		System.out.println("Game Result:" +minimaxAlphaBeta(game, -10000, 10000));
		System.out.println("Moves considered with alpha-beta pruning: "+count);
		System.out.println("Alpha cuts: " + alphaCount);
		System.out.println("Beta cuts: " + betaCount);
	}
	static int minimaxAlphaBeta(TicTacToe game, int alpha, int beta){
		int winner = game.gameEnd();
		if(winner !=3){
			count++;
			return score(game);
		}
		List<Integer> children = game.availableMoves();
		
		if(game.getPlayer() ==0){
			for(int n : children){
				int score = minimaxAlphaBeta(game.newMove(n),alpha,beta);
				//update alpha
				if(score > alpha){
					alpha= score;
				}
				//cutoff
				if(alpha >= beta){
					alphaCount ++;
					break;
				}
			}
			return alpha;
		}else{
			for(int n : children){
				int score = minimaxAlphaBeta(game.newMove(n),alpha,beta);
				//update beta
				if(score < beta){
					beta=score;
				}
				//cutoff
				if(alpha >=beta){
					betaCount++;
					break;
				}
			}
			return beta;
			
		}
	}
	
	public static void main(String[] args){
		TicTacToe game = new TicTacToe(args[0]);
        System.out.println("Initial board:");
        game.print();
		System.out.println();
		minimax(game);
		System.out.println();
		minimaxAlphaBeta(game);
		
//		System.out.println("Minimax");
//		game.read("XO_______");
//		minimax(game);// should be 10
//		game.read("OXX___O__");
//		minimax(game);// should be -10
//		game.read("O___X____");
//		minimax(game);// should be 0
//		game.read("_________");
//		minimax(game);// should be 0
//		
//		System.out.println("Alpha beta");
//		game.read("XO_______");
//		minimaxAlphaBeta(game);// should be 10
//		game.read("OXX___O__");
//		minimaxAlphaBeta(game);// should be -10
//		game.read("O___X____");
//		minimaxAlphaBeta(game);// should be 0
//		game.read("_________");
//		minimaxAlphaBeta(game);// should be 0
		
		
		
	}
}
