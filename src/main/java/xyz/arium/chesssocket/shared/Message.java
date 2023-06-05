package xyz.arium.chesssocket.shared;

import java.io.Serializable;

public class Message implements Serializable {
    private String command;
	private PieceColor pieceColor;
	private ChessMove move;

	public Message(String command, PieceColor pieceColor) { 
        this.command = command;
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

    public String getCommand() {
        return command;
    }

}
