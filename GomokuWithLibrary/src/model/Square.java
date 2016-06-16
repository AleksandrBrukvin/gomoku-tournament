package model;

import java.io.IOException;

import controller.GomokuGame;
import controller.SquareController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Square extends Pane {
	public static final int SIZE_10 = 50;
	public static final int SIZE_20 = 40;
	
	/**
	 * @param row Square location row
	 * @param column Square location column
	 * @param game state
	 * @param size board size
	 * @throws IOException
	 */
	public Square(int row, int column, GomokuGame game, int size) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Square.fxml"));
		loader.setRoot(this);
		final SquareController squareController = 
				new SquareController(game, new Location(row, column), size);
		loader.setController(squareController);
		loader.load();
		this.setPrefHeight(size);
		this.setPrefWidth(size);
		GridPane.setColumnIndex(this, column);
		GridPane.setRowIndex(this, row);
	}
}
