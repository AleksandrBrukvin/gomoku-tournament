package gomoku;

public class ComputerStrategyImplementation {

	// for the last move
		private int[][] previousBoard = null;
		public int getMove(int[][] board, int player) {
			int[][] b = board;
			if (previousBoard == null) {
				// store previous state to get the last move
				previousBoard = new int[b.length][b[0].length];
			} else {
				// let's look for the last move to cut down search area
				lastmove:
				for (int row = 0; row < b.length; row++) {
					for (int col = 0; col < b[0].length; col++) {
						if (b[row][col] != previousBoard[row][col]) {
							lastRow = row;
							lastCol = col;
							System.out.println("lastmove:" + row + ", " + col);
							break lastmove;
						}
					}
				}
			}
			/*
			System.out.println("getmove");
			for (int row = 0; row < b.length; row++) {
				for (int col = 0; col < b[0].length; col++) {
					b[row][col] = b[row][col] == SimpleBoard.PLAYER_WHITE ? ComputerStrategyImplementation.whitecell
							: (b[row][col] == SimpleBoard.PLAYER_BLACK ? blackcell : emptycell);
				}
			}
			*/
			System.out.println("calc move");
			int move = best_opponent_move(b, player /*== SimpleBoard.PLAYER_WHITE ? whitecell : blackcell*/);
			System.out.println(move);
			// let's mark our move to the previous state board
			previousBoard[move/1000][move%1000] = player;
			
			return move;
			//return new Location(move / 1000, move % 1000);
		}


	private static int winPlayer = 100000;
	private static int winOpponent = -90000;
	public static final int whitecell = 2;
	public static final int blackcell = 1;
	public static final int emptycell = 0;
	private static int maxdepth = 2;
	private static int maxcolor = 2;
	private static int stat_eval_board = 0;
	private static int stat_eval_cell = 0;
	
	// last opponent move
	private static int lastRow = -1;
	private static int lastCol = -1;

	private static int best_opponent_move(int[][] paramArrayOfInt, int player) {
		maxcolor = player;
		int k = minimax(paramArrayOfInt, player, 0, lastRow, lastCol, 0);
		if (k < 0) {
			System.out.println("error in best_opponent_move: no move found");
			System.exit(1);
		}
		return k;
	}
	
	private static boolean checkWin(int[][] board, int row, int col) {
		int rows = board.length;
		int cols = board[0].length;
		int cell = board[row][col];
		int[][] directions = {
				// column |
				{-1, 0, 1, 0},
				// diagonal /
				{-1, 1, 1, -1},
				// row -
				{0, 1, 0, -1},
				// diagonal \
				{1, 1, -1, -1}};
		for (int i = 0; i < directions.length; i++) {
			int[] d = directions[i];
			int sum = eval_count(board, rows, cols, cell, row, d[0], col, d[1]) 
					+ eval_count(board, rows, cols, cell, row, d[2], col, d[3]);
			sum = sum / 1000;
			if (sum > 4) {
				System.out.println("direction[" + i + "] gives " + sum);
			}
			if (sum + 1 >= 5) return true;
		}
		return false;
	}

	private static int encode_move(int paramInt1, int paramInt2) {
		return paramInt1 * 1000 + paramInt2;
	}

	private static int minimax(int[][] board, int player,
			int depth, int lastRow, int lastCol, int debug) {
		int i = -1;
		int j;
		if (player == maxcolor) {
			j = -10000000;
		} else {
			j = 10000000;
		}
		if (lastRow > -1 && lastCol > -1) {
			if (checkWin(board, lastRow, lastCol)) {
				System.out.println("win at " + lastRow + ", " + lastCol);
				// opponent's move was the last, therefore we have to return opposite values
				if (player == maxcolor) {
					return winOpponent;
				} else {
					return winPlayer;
				}
			}
		}
		int m = board.length;
		int n = board[0].length;
		for (int row = 0; row < m; row++) {
			for (int col = 0; col < n; col++) {
				if (board[row][col] == emptycell) {
					board[row][col] = player;
					int k;
					if (depth >= maxdepth) {
						k = evalboard(board, player);
						if (debug == 1 && k != 0) {
							System.out.println(row + ", " + col + " k=" + k);
						}
						if (player != maxcolor) {
							k = 0 - k;
						}
					} else {
						k = minimax(board, 3 - player, depth + 1, row, col, 0);
					}
					board[row][col] = emptycell;
					if ((k == j) && (depth < maxdepth)) {
						if (Math.random() > 0.6D) {
							j = k;
							if (depth == 0) {
								i = encode_move(row, col);
							}
						}
					} else if (player == maxcolor) {
						if (k > j) {
							j = k;
							if (depth == 0) {
								i = encode_move(row, col);
							}
						}
					} else if (k < j) {
						j = k;
						if (depth == 0) {
							i = encode_move(row, col);
						}
					}
				}
				if (depth == 0) {
					System.out.print(row + ", " + col + " j=" + j + "  ");
				}
			}
			if (depth == 0)
			System.out.println();
		}
		if (depth == 0) {
			return i;
		}
		return j;
	}

	private static int evalboard(int[][] board, int player) {
		int k = 0;

		stat_eval_board += 1;
		int rows = board.length;
		int cols = board[0].length;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int cell = board[row][col];
				if (cell != emptycell) {
					stat_eval_cell += 1;
					int n = eval_count(board, rows, cols, cell, row, -1, col, 0);
					int i1 = eval_count(board, rows, cols, cell, row, -1, col, 1);
					int i2 = eval_count(board, rows, cols, cell, row, 0, col, 1);
					int i3 = eval_count(board, rows, cols, cell, row, 1, col, 1);
					int i4 = eval_count(board, rows, cols, cell, row, 1, col, 0);
					int i5 = eval_count(board, rows, cols, cell, row, 1, col, -1);
					int i7 = eval_count(board, rows, cols, cell, row, 0, col, -1);
					int i6 = eval_count(board, rows, cols, cell, row, -1, col, -1);
					int i8 = eval_line(player, cell, n, i4);
					int i9 = eval_line(player, cell, i1, i5);
					int i10 = eval_line(player, cell, i2, i7);
					int i11 = eval_line(player, cell, i3, i6);
					k = k + i8 + i9 + i10 + i11;
				}
			}
		}
		return k;
	}

	private static int eval_count(int[][] board, int rows,
			int cols, int cell, int row, int rowd,
			int col, int cold) {
		int i = 0;
		for (;;) {
			row += rowd;
			col += cold;
			if ((row < 0) || (row >= rows) || (col < 0)
					|| (col >= cols)) {
				return i * 1000;
			}
			if (board[row][col] == emptycell) {
				return i * 1000 + 1;
			}
			if (board[row][col] != cell) {
				return i * 1000;
			}
			i++;
		}
	}

	private static int eval_line(int player, int cell, int paramInt3,
			int paramInt4) {
		int i2 = 0;

		int i = paramInt3 / 1000;
		int j = paramInt3 % 1000;
		int k = paramInt4 / 1000;
		int m = paramInt4 % 1000;

		int n = i + k + 1;
		int i1 = j + m;
		if (n < 2) {
			return 0;
		}
		if (player == cell) {
			if (n >= 5) {
				i2 = 1000000;
			} else if ((n == 4) && (i1 == 2)) {
				i2 = 10000;
			} else if ((n == 4) && (i1 == 1)) {
				i2 = 1000;
			} else if ((n == 3) && (i1 == 2)) {
				i2 = 1500;
			} else if ((n == 2) && (i1 == 2)) {
				i2 = 100;
			} else {
				i2 = 0;
			}
		} else if (n >= 5) {
			i2 = -900000;
		} else if ((n == 4) && (i1 == 2)) {
			i2 = -30000;
		} else if ((n == 4) && (i1 == 1)) {
			i2 = -20000;
		} else if ((n == 3) && (i1 == 2)) {
			i2 = -5000;
		} else if ((n == 2) && (i1 == 2)) {
			i2 = -100;
		} else {
			i2 = 0;
		}
		return i2;
	}
}
