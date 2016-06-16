package gomoku.strategies;

import java.util.ArrayList;

import gomoku.ComputerStrategy;
import gomoku.Location;
import gomoku.SimpleBoard;

/**
 * Important!
 * This is an example strategy class.
 * You should not overwrite this.
 * Instead make your own class, for example:
 * public class AgoStrategy implements ComputerStrategy
 * 
 * and add all the logic there. The created strategy
 * should be visible under player selection automatically.
 * 
 * This file here might be overwritten in future versions.
 *
 */
public class TanjaStrategy implements ComputerStrategy {

	private static int COUNT4 = 4;
	private static int COUNT3 = 3;
	private static int COUNT2 = 2;
	private static int COUNT1 = 1;
	private static int TOSCORE = 10;
	private static int TO9SCORE = 9;
	private static int PLAYER = SimpleBoard.PLAYER_BLACK;
	private static Location LOCATION = null;

	@Override
	public Location getMove(SimpleBoard board, int player) {
		// let's operate on 2-d array
		int[][] b = board.getBoard();
		if(getPossibleMoves(b).size() == 100 || getPossibleMoves(b).size() == 400) {
			PLAYER = SimpleBoard.PLAYER_WHITE;
			return new Location(0,0);
		}
		if (b.length == 20) {
			makeScore(b);
			getScore(b);
		} else {
			minimax(b, PLAYER, 2);
		}
		return LOCATION;
	}
	
	public static int minimax(int[][] board, int player, int depth) {
		int bestScore = Integer.MIN_VALUE;
		makeScore(board);
		
		if (depth == 0) {
			return getScore(board);
		}

		ArrayList<Location> possibleMoves = getPossibleMoves(board);
		for (Location loc : possibleMoves) {
			board[loc.getRow()][loc.getColumn()] = player;
			//makeScore(board);
			if (player == SimpleBoard.PLAYER_BLACK)
				player = SimpleBoard.PLAYER_WHITE;
			else
				player = SimpleBoard.PLAYER_BLACK;
			int value = minimax(board, player, depth - 1);
			board[loc.getRow()][loc.getColumn()] = SimpleBoard.EMPTY;
			//makeScore(board);
			if (value > bestScore) {
				bestScore = value;
			}
		}
		
		return bestScore;
	}

	/**
	 * Returns all possible moves. It is used by the minimax algorithm. It might
	 * be wise to only return certain empty cells (as cells in the corner and
	 * border are not so valuable as in the center etc).
	 * 
	 * @param board
	 *            The board.
	 * @return A list of location objects
	 */
	public static ArrayList<Location> getPossibleMoves(int[][] board) {
		ArrayList<Location> availableMoves = new ArrayList<Location>();

		// TODO:
		// loop over the board
		// and return only cells which are available
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] != SimpleBoard.PLAYER_BLACK && board[row][col] != SimpleBoard.PLAYER_WHITE) {
					availableMoves.add(new Location(row, col));
				}
			}
		}
		return availableMoves;
	}

	/**
	 * Given a game state (board with pieces) returns a score for the state.
	 * 
	 * @param board
	 *            Game state (e.g. board)
	 * @return score A heuristic score for the given state.
	 */
	public static int getScore(int[][] board) {
		int score = 0;
		int tempScore = 0;
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				tempScore = board[row][col];
				if(tempScore > score) {
					score = tempScore;
					LOCATION = new Location(row, col);
				}
			}
		}
		return score;	
	}
	
	public static void makeScore(int[][] board) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				int score = 0;
				if(board[row][col] != SimpleBoard.PLAYER_BLACK && board[row][col] != SimpleBoard.PLAYER_WHITE) {
					if(col + 1 < board.length && board[row][col + 1] != SimpleBoard.PLAYER_BLACK && board[row][col + 1] != SimpleBoard.PLAYER_WHITE) {
						if(col + 2 < board.length && board[row][col + 2] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(col + 3 < board.length && board[row][col + 3] == PLAYER) {
								score += COUNT2 * TO9SCORE;
								if(col + 4 < board.length && board[row][col + 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col + 1 < board.length && board[row][col + 1] == PLAYER) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col + 2 < board.length && board[row][col + 2] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(col + 3 < board.length && board[row][col + 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col + 4 < board.length && board[row][col + 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col - 1 >= 0 && board[row][col - 1] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
							if(col - 1 >= 0 && board[row][col - 1] == PLAYER) {
								score += COUNT1 * TO9SCORE;
								if(col - 2 >= 0 && board[row][col - 2] == PLAYER) {
									score += COUNT4 * TOSCORE * TO9SCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && board[row][col - 1] != SimpleBoard.PLAYER_BLACK && board[row][col - 1] != SimpleBoard.PLAYER_WHITE) {
						if(col - 2 >= 0 && board[row][col - 2] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(col - 3 >= 0 && board[row][col - 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col - 4 >= 0 && board[row][col - 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && board[row][col - 1] == PLAYER) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col - 2 >= 0 && board[row][col - 2] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(col - 3 >= 0 && board[row][col - 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col - 4 >= 0 && board[row][col - 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col + 1 < board.length && board[row][col + 1] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								
							}
							if(col + 1 < board.length && board[row][col + 1] == PLAYER) {
								score += COUNT1 * TO9SCORE;
								if(col + 2 < board.length && board[row][col + 2] == PLAYER) {
									score += COUNT4 * TOSCORE * TO9SCORE;
								}
							}
						}
					}
					if(col - 2 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] != SimpleBoard.PLAYER_BLACK && board[row - 1][col - 1] != SimpleBoard.PLAYER_WHITE) {
						if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(col - 3 >= 0 && row - 3 >= 0 && board[row - 3][col - 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col - 4 >= 0 && row - 4 >= 0 && board[row - 4][col - 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == PLAYER) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(col - 3 >= 0 && row - 3 >= 0 && board[row - 3][col - 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col - 4 >= 0 && row - 4 >= 0 && board[row - 4][col - 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
							if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == PLAYER) {
								score += COUNT1 * TO9SCORE;
								if(col + 2  < board.length && row + 2 < board.length && board[row + 2][col + 2] == PLAYER) {
									score += COUNT4 * TOSCORE * TO9SCORE;
								}
							}
						}
					}
					if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] != SimpleBoard.PLAYER_BLACK && board[row - 1][col + 1] != SimpleBoard.PLAYER_WHITE) {
						if(col + 2 < board.length && row - 2 >= 0 && board[row - 2][col + 2] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(col + 3 < board.length && row - 3 >= 0 && board[row - 3][col + 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col + 4 < board.length && row - 4 >= 0 && board[row - 4][col + 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] == PLAYER) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col + 2 < board.length && row - 2 >= 0 && board[row - 2][col + 2] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(col + 3 < board.length && row - 3 >= 0 && board[row - 3][col + 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col + 4 < board.length && row - 4 >= 0 && board[row - 4][col + 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
							if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] == PLAYER) {
								score += COUNT1 * TO9SCORE;
								if(col - 2 >= 0 && row + 2 < board.length && board[row + 2][col - 2] == PLAYER) {
									score += COUNT4 * TOSCORE * TO9SCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] != SimpleBoard.PLAYER_BLACK && board[row + 1][col - 1] != SimpleBoard.PLAYER_WHITE) {
						if(col - 2 >= 0 && row + 2 < board.length && board[row + 2][col - 2] == PLAYER) {
							score += COUNT2* TOSCORE;
							if(col - 3 >= 0 && row + 3 < board.length && board[row + 3][col - 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col - 4 >= 0 && row + 4 < board.length && board[row + 4][col - 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] == PLAYER) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col - 2 >= 0 && row + 2 < board.length && board[row + 2][col - 2] == PLAYER) {
							score += COUNT2* TOSCORE;
							if(col - 3 >= 0 && row + 3 < board.length && board[row + 3][col - 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col - 4 >= 0 && row + 4 < board.length && board[row + 4][col - 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
							if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] == PLAYER) {
								score += COUNT1 * TO9SCORE;
								if(col + 2 < board.length && row - 2 >= 0 && board[row - 2][col + 2] == PLAYER) {
									score += COUNT4 * TOSCORE * TO9SCORE;
								}
							}
						}
					}
					if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] != SimpleBoard.PLAYER_BLACK && board[row + 1][col + 1] != SimpleBoard.PLAYER_WHITE) {
						if(col + 2  < board.length && row + 2 < board.length && board[row + 2][col + 2] == PLAYER) {
							score += COUNT2* TOSCORE;
							if(col + 3  < board.length && row + 3 < board.length && board[row + 3][col + 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col + 4  < board.length && row + 4 < board.length && board[row + 4][col + 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == PLAYER) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col + 2  < board.length && row + 2 < board.length && board[row + 2][col + 2] == PLAYER) {
							score += COUNT2* TOSCORE;
							if(col + 3  < board.length && row + 3 < board.length && board[row + 3][col + 3] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(col + 4  < board.length && row + 4 < board.length && board[row + 4][col + 4] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
							if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == PLAYER) {
								score += COUNT1 * TO9SCORE;
								if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] == PLAYER) {
									score += COUNT4 * TOSCORE * TO9SCORE;
								}
							}
						}
					}
					if(row + 1 < board.length && board[row + 1][col] != SimpleBoard.PLAYER_BLACK && board[row + 1][col] != SimpleBoard.PLAYER_WHITE) {
						if(row + 2 < board.length && board[row + 2][col] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(row + 3 < board.length && board[row + 3][col] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(row + 4 < board.length && board[row + 4][col] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(row + 1 < board.length && board[row + 1][col] == PLAYER) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(row + 2 < board.length && board[row + 2][col] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(row + 3 < board.length && board[row + 3][col] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(row + 4 < board.length && board[row + 4][col] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(row - 1 >= 0 && board[row - 1][col] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
							if(row - 1 >= 0 && board[row - 1][col] == PLAYER) {
								score += COUNT1 * TO9SCORE;
								if(row - 2 >= 0 && board[row - 2][col] == PLAYER) {
									score += COUNT4 * TOSCORE * TO9SCORE;
								}
							}
						}
					}
					if(row - 1 >= 0 && board[row - 1][col] != SimpleBoard.PLAYER_BLACK && board[row - 1][col] != SimpleBoard.PLAYER_WHITE) {
						if(row - 2 >= 0 && board[row - 2][col] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(row - 3 >= 0 && board[row - 3][col] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(row - 4 >= 0 && board[row - 4][col] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(row - 1 >= 0 && board[row - 1][col] == PLAYER) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(row - 2 >= 0 && board[row - 2][col] == PLAYER) {
							score += COUNT2 * TOSCORE;
							if(row - 3 >= 0 && board[row - 3][col] == PLAYER) {
								score += COUNT3 * TO9SCORE;
								if(row - 4 >= 0 && board[row - 4][col] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(row + 1 < board.length && board[row + 1][col] == PLAYER) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
							if(row + 1 < board.length && board[row + 1][col] == PLAYER) {
								score += COUNT1 * TO9SCORE;
								if(row + 2 < board.length && board[row + 2][col] == PLAYER) {
									score += COUNT4 * TOSCORE * TO9SCORE;
								}
							}
						}
					}
					if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == PLAYER){
						if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == PLAYER){
							score += COUNT2*TOSCORE;
							if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] != SimpleBoard.PLAYER_BLACK && board[row - 2][col - 2] != SimpleBoard.PLAYER_WHITE){
								if(col - 3 >= 0 && row - 3 >= 0 && board[row - 3][col - 3] == PLAYER){
									score += COUNT4*TOSCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == PLAYER){
						if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == PLAYER){
							score += COUNT2*TOSCORE;
							if(col + 2 < board.length && row + 2 < board.length && board[row + 2][col + 2] != SimpleBoard.PLAYER_BLACK && board[row + 2][col + 2] != SimpleBoard.PLAYER_WHITE){
								if(col + 3 < board.length && row + 3 < board.length && board[row + 3][col + 3] == PLAYER){
									score += COUNT4*TOSCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] == PLAYER) {
						if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == PLAYER){
							score += COUNT2*TOSCORE;
							if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] != SimpleBoard.PLAYER_BLACK && board[row - 2][col - 2] != SimpleBoard.PLAYER_WHITE){
								if(col - 3 >= 0 && row - 3 >= 0 && board[row - 3][col - 3] == PLAYER){
									score += COUNT4*TOSCORE;
								}
							}
						}
					}
						
					if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] == PLAYER) {
						if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == PLAYER){
							score += COUNT2*TOSCORE;
							if(col + 2 < board.length && row + 2 < board.length && board[row + 2][col + 2] != SimpleBoard.PLAYER_BLACK && board[row + 2][col + 2] != SimpleBoard.PLAYER_WHITE){
								if(col + 3 < board.length && row + 3 < board.length && board[row + 3][col + 3] == PLAYER){
									score += COUNT4*TOSCORE;
								}
							}
						}
					}
					
				
				
				//Opponent
				int player = SimpleBoard.PLAYER_BLACK;
				if(PLAYER == SimpleBoard.PLAYER_BLACK) {
					player = SimpleBoard.PLAYER_WHITE;
				}
				if(board[row][col] != SimpleBoard.PLAYER_BLACK && board[row][col] != SimpleBoard.PLAYER_WHITE) {
					if(col + 1 < board.length && board[row][col + 1] == player) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col + 2 < board.length && board[row][col + 2] == player) {
							score += COUNT3 * TO9SCORE;
							if(col + 3 < board.length && board[row][col + 3] == player) {
								score += COUNT4 * TOSCORE;
								if(col + 4 < board.length && board[row][col + 4] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col + 4 < board.length && board[row][col + 4] != SimpleBoard.PLAYER_BLACK && board[row][col + 4] != SimpleBoard.PLAYER_WHITE) {
									score += COUNT4 * TOSCORE;
								}
								if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == player) {
									score += COUNT2 * TOSCORE;
									if(col + 2  < board.length && row + 2 < board.length && board[row + 2][col + 2] == player) {
										score += COUNT3 * TOSCORE;
									}
								}
								if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] == player) {
									score += COUNT2 * TOSCORE;
									if(col + 2 < board.length && row - 2 >= 0 && board[row - 2][col + 2] == player) {
										score += COUNT3 * TOSCORE;
									}
								}
							}
							if(col - 1 >= 0 && board[row][col - 1] == player) {
								score += COUNT4 * TOSCORE;
								if(col - 2 >= 0 && board[row][col - 2] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && board[row][col - 1] == player) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col - 2 >= 0 && board[row][col - 2] == player) {
							score += COUNT3 * TO9SCORE;
							if(col - 3 >= 0 && board[row][col - 3] == player) {
								score += COUNT4 * TOSCORE;
								if(col - 4 >= 0 && board[row][col - 4] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col - 4 >= 0 && board[row][col - 4] != SimpleBoard.PLAYER_BLACK && board[row][col - 4] != SimpleBoard.PLAYER_WHITE) {
									score += COUNT4 * TOSCORE;
								}
								if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == player) {
									score += COUNT2 * TOSCORE;
									if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] == player) {
										score += COUNT3 * TOSCORE;
									}
								}
								if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] == player) {
									score += COUNT2 * TOSCORE;
									if(col - 2 >= 0 && row + 2 < board.length && board[row + 2][col - 2] == player) {
										score += COUNT3 * TOSCORE;
									}
								}
							}
							if(col + 1 < board.length && board[row][col + 1] == player) {
								score += COUNT4 * TOSCORE;
								if(col + 2 < board.length && board[row][col + 2] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					
					if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == player) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] == player) {
							score += COUNT3 * TO9SCORE;
							if(col - 3 >= 0 && row - 3 >= 0 && board[row - 3][col - 3] == player) {
								score += COUNT4 * TOSCORE;
								if(col - 4 >= 0 && row - 4 >= 0 && board[row - 4][col - 4] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col - 4 >= 0 && row - 4 >= 0 && board[row - 4][col - 4] != SimpleBoard.PLAYER_BLACK && board[row - 4][col - 4] != SimpleBoard.PLAYER_WHITE) {
									score += COUNT4 * TOSCORE;
								}
							}
							if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == player) {
								score += COUNT4 * TOSCORE;
								if(col + 2  < board.length && row + 2 < board.length && board[row + 2][col + 2] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] == player) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col + 2 < board.length && row - 2 >= 0 && board[row - 2][col + 2] == player) {
							score += COUNT3 * TO9SCORE;
							if(col + 3 < board.length && row - 3 >= 0 && board[row - 3][col + 3] == player) {
								score += COUNT4 * TOSCORE;
								if(col + 4 < board.length && row - 4 >= 0 && board[row - 4][col + 4] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col + 4 < board.length && row - 4 >= 0 && board[row - 4][col + 4] != SimpleBoard.PLAYER_BLACK && board[row - 4][col + 4] != SimpleBoard.PLAYER_WHITE) {
									score += COUNT4 * TOSCORE;
								}
							}
							if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] == player) {
								score += COUNT4 * TOSCORE;
								if(col - 2 >= 0 && row + 2 < board.length && board[row + 2][col - 2] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] == player) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col - 2 >= 0 && row + 2 < board.length && board[row + 2][col - 2] == player) {
							score += COUNT3 * TO9SCORE;
							if(col - 3 >= 0 && row + 3 < board.length && board[row + 3][col - 3] == player) {
								score += COUNT4 * TOSCORE;
								if(col - 4 >= 0 && row + 4 < board.length && board[row + 4][col - 4] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col - 4 >= 0 && row + 4 < board.length && board[row + 4][col - 4] != SimpleBoard.PLAYER_BLACK && board[row + 4][col - 4] != SimpleBoard.PLAYER_WHITE) {
									score += COUNT4 * TOSCORE;
								}
							}
							if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] == player) {
								score += COUNT4 * TOSCORE;
								if(col + 2 < board.length && row - 2 >= 0 && board[row - 2][col + 2] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == player) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(col + 2  < board.length && row + 2 < board.length && board[row + 2][col + 2] == player) {
							score += COUNT3 * TO9SCORE;
							if(col + 3  < board.length && row + 3 < board.length && board[row + 3][col + 3] == player) {
								score += COUNT4 * TOSCORE;
								if(col + 4  < board.length && row + 4 < board.length && board[row + 4][col + 4] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(col + 4  < board.length && row + 4 < board.length && board[row + 4][col + 4] != SimpleBoard.PLAYER_BLACK && board[row + 4][col + 4] != SimpleBoard.PLAYER_WHITE) {
									score += COUNT4 * TOSCORE;
								}
							}
							if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == player) {
								score += COUNT4 * TOSCORE;
								if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					
					if(row + 1 < board.length && board[row + 1][col] == player) {
						//score += COUNT1 * TOSCORE;
						score += COUNT2;
						if(row + 2 < board.length && board[row + 2][col] == player) {
							score += COUNT3 * TO9SCORE;
							if(row + 3 < board.length && board[row + 3][col] == player) {
								score += COUNT4 * TOSCORE;
								if(row + 4 < board.length && board[row + 4][col] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(row + 4 < board.length && board[row + 4][col] != SimpleBoard.PLAYER_BLACK && board[row + 4][col] != SimpleBoard.PLAYER_WHITE) {
									score += COUNT4 * TOSCORE;
								}
								if(col + 1 < board.length && row + 1 < board.length && board[row + 1][col + 1] == player) {
									score += COUNT2 * TOSCORE;
									if(col + 2  < board.length && row + 2 < board.length && board[row + 2][col + 2] == player) {
										score += COUNT3 * TOSCORE;
									}
								}
								if(col - 1 >= 0 && row + 1 < board.length && board[row + 1][col - 1] == player) {
									score += COUNT2 * TOSCORE;
									if(col - 2 >= 0 && row + 2 < board.length && board[row + 2][col - 2] == player) {
										score += COUNT3 * TOSCORE;
									}
								}
								
							}
							if(row - 1 >= 0 && board[row - 1][col] == player) {
								score += COUNT4 * TOSCORE;
								if(row - 2 >= 0 && board[row - 2][col] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
					if(row - 1 >= 0 && board[row - 1][col] == player) {
						score += COUNT2;
						if(row - 2 >= 0 && board[row - 2][col] == player) {
							score += COUNT3 * TO9SCORE;
							if(row - 3 >= 0 && board[row - 3][col] == player) {
								score += COUNT4 * TOSCORE;
								if(row - 4 >= 0 && board[row - 4][col] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
								if(row - 4 >= 0 && board[row - 4][col] != SimpleBoard.PLAYER_BLACK && board[row - 4][col] != SimpleBoard.PLAYER_WHITE) {
									score += COUNT4 * TOSCORE;
								}
								if(col - 1 >= 0 && row - 1 >= 0 && board[row - 1][col - 1] == player) {
									score += COUNT2 * TOSCORE;
									if(col - 2 >= 0 && row - 2 >= 0 && board[row - 2][col - 2] == player) {
										score += COUNT3 * TOSCORE;
									}
								}
								if(col + 1 < board.length && row - 1 >= 0 && board[row - 1][col + 1] == player) {
									score += COUNT2 * TOSCORE;
									if(col + 2 < board.length && row - 2 >= 0 && board[row - 2][col + 2] == player) {
										score += COUNT3 * TOSCORE;
									}
								}
							}
							if(row + 1 < board.length && board[row + 1][col] == player) {
								score += COUNT4 * TOSCORE;
								if(row + 2 < board.length && board[row + 2][col] == player) {
									score += COUNT4 * TOSCORE * TOSCORE;
								}
							}
						}
					}
				
			board[row][col] = score;
			}
		}
			}
		}
	}

/*	*//**
	 * Counts pieces on the board starting from (row, col) and moving in
	 * direction (rowd, cold).
	 * 
	 * @param board
	 *            The board
	 * @param row
	 *            Starting row
	 * @param col
	 *            Starting col
	 * @param rowd
	 *            Row step (-1, 0, 1)
	 * @param cold
	 *            Col step (-1, 0, 1)
	 * @param player
	 *            Player (whose piece is expected)
	 * @return The number of connected player pieces "in a row"
	 *//*
	public static int getCount(int[][] board, int row, int col, int rowd,
			int cold, int player) {
		int count = 0;
		for (int i = 0; i < WINCOUNT; i++) {
			if (board[row + i * rowd][col + i * cold] == player)
				count++;
			else
				break;
		}
		return count;
	}*/


	@Override
	public String getName() {
		return "Tanja";
	}

}
