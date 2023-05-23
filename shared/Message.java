package shared;

import java.io.Serializable;

public class Message implements Serializable {
	private PieceColor pieceColor;
	private ChessMove move;

	public Message(PieceColor pieceColor) { 
        this.pieceColor = pieceColor;
        this.move = null;
	}

	public Message(PieceColor pieceColor, ChessMove move) {
		this.pieceColor = pieceColor;
		this.move = move;
	}

	public PieceColor getPieceColor() {
		return pieceColor;
	}

	public ChessMove getMove() {
		return move;
	}

}