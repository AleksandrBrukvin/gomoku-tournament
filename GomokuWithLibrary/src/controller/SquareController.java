package controller;

import controller.GomokuGame.GameStatus;
import controller.GomokuGame.SquareState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Location;

public class SquareController {
	
	
	private final GomokuGame game;
	private final Location location;
	public SquareController(GomokuGame game, Location location, int size) {
		this.game = game;
		this.location = location;
	}
	
	@FXML
	private Pane root;
	@FXML
	private Circle circle;
	
	/**
	 * Initializing
	 */
	public void initialize() {
		// circle size
		circle.radiusProperty().bind(Bindings.min(root.widthProperty(), 
				root.heightProperty()).divide(2).subtract(5));
		circle.layoutXProperty().bind(root.widthProperty().divide(2));
		circle.layoutYProperty().bind(root.heightProperty().divide(2));
		
		ReadOnlyObjectProperty<SquareState> squareState = game.squareStateProperty(location);
		
		change(squareState);
		
		// every square state in the board gets listener (if the square state changes...)
		squareState.addListener((event) ->
		{
			change(squareState);
		});
		// disable if ..
		
		root.disableProperty().bind(
				// location is not empty
				game.squareStateProperty(location).isNotEqualTo(SquareState.EMPTY)
				// or game status is not "open" (someone has won or it's a draw)
				.or(game.gameStatusProperty().isNotEqualTo(GameStatus.OPEN))
				// not current player's turn?
				// or in replay mode?
				.or(game.inReplayMode())
		);
		
	}
	
	/**
	 * Changes square state
	 * @param squareState current square instance
	 */
	private void change(ReadOnlyObjectProperty<SquareState> squareState) {
		if (squareState.get() == SquareState.EMPTY) {
			circle.visibleProperty().set(false);
		} else {
			circle.visibleProperty().set(true);
			if (squareState.get() == SquareState.BLACK) {
				circle.setFill(Color.BLACK);
			} else if (squareState.get() == SquareState.WHITE) {
				circle.setFill(Color.WHITE);
			}
		}
	}
	
	/**
	 * Makes a move
	 */ 
	@FXML
	public void makeMove() {
		game.makeMove(location);
	}
}
