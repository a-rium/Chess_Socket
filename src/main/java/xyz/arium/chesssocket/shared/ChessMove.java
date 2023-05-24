package xyz.arium.chesssocket.shared;

import java.io.Serializable;

public class ChessMove implements Serializable
{
	private String from, to;

	private String[] numToLetter = {"a", "b", "c", "d", "e", "f", "g"};

	public ChessMove(String from, String to) throws InvalidMoveException
	{
		if(!from.matches("[1-8][a-h]") || !to.matches("[1-8][a-h]"))
			throw new InvalidMoveException("Invalid move: from=" + from + " to=" + to);
		this.from = from;
		this.to = to;
	}

	public ChessMove(int fromX, int fromY, int toX, int toY) throws InvalidMoveException
	{
		if((fromX > 7 || fromX < 0) || (fromY > 7 || fromY < 0) ||
			  (toX > 7 || toX < 0) || (toY > 7 || toY < 0))
			throw new InvalidMoveException("Invalid move: fromX=" + fromX + " fromY=" + fromY 
				+ " toX=" + toX + " toY=" + toY);
		this.from = "" + numToLetter[fromY] + (fromX - 1);
		this.to = "" + numToLetter[toY] + (toX - 1);
	}

	public String getFrom()
	{
		return from;
	}

	public String getTo()
	{
		return to;
	}

}
