package server;

import shared.SocketIO;

import java.net.Socket;
import java.net.ServerSocket;

import java.io.IOException;

import shared.Message;
import shared.PieceColor;
import shared.ChessException;

public class ChessServer extends Thread
{
	private ServerSocket server;
	private SocketIO firstPlayer, secondPlayer; 

	private boolean running;

	public ChessServer(int port) throws IOException {
        this.server = new ServerSocket(port);
	}

	public void run() {
        try {
            waitForPlayersToConnect();
            Message firstPlayerChoice = firstPlayer.readMessage();
            Message secondPlayerChoice = secondPlayer.readMessage();

            if (firstPlayerChoice.getPieceColor().equals(secondPlayerChoice.getPieceColor())) {
                if (Math.round(Math.random()) == 1) {
                    firstPlayer.sendMessage(new Message(PieceColor.BLACK));
                    secondPlayer.sendMessage(new Message(PieceColor.WHITE));
                } else {
                    firstPlayer.sendMessage(new Message(PieceColor.WHITE));
                    secondPlayer.sendMessage(new Message(PieceColor.BLACK));
                }

            }
        } catch (ChessException | IOException e) {
            throw new RuntimeException("Unexpected server fault", e);
        }
    }

	public void halt() {
        this.running = false;
	}

    public void waitForPlayersToConnect() throws IOException {
        while(running && (firstPlayer == null || secondPlayer == null)) {
            Socket newConnection = server.accept();
            this.secondPlayer = firstPlayer;
            this.firstPlayer = new SocketIO(newConnection);
        }
    }
}
