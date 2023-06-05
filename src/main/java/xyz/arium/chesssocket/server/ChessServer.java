package xyz.arium.chesssocket.server;


import java.net.Socket;
import java.net.ServerSocket;

import java.io.IOException;

import xyz.arium.chesssocket.shared.SocketIO;
import xyz.arium.chesssocket.shared.Message;
import xyz.arium.chesssocket.shared.PieceColor;

public class ChessServer {
	private ServerSocket server;
	private SocketIO firstPlayer, secondPlayer; 

	private boolean running;

	public ChessServer(int port) throws IOException {
        this.server = new ServerSocket(port);
	}

	public void run() {
        this.running = true;
        try {
            waitForPlayersToConnect();

            // Choose player color
            if (Math.round(Math.random()) == 1) {
                firstPlayer.sendMessage(new Message("init", PieceColor.BLACK));
                secondPlayer.sendMessage(new Message("init", PieceColor.WHITE));
            } else {
                firstPlayer.sendMessage(new Message("init", PieceColor.WHITE));
                secondPlayer.sendMessage(new Message("init", PieceColor.BLACK));
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected server fault", e);
        } finally {
            halt();
        }
    }

	public void halt() {
        this.running = false;
	}

    private void waitForPlayersToConnect() throws IOException {
        while(running && (firstPlayer == null || secondPlayer == null)) {
            Socket newConnection = server.accept();
            this.secondPlayer = firstPlayer;
            this.firstPlayer = new SocketIO(newConnection);
        }
    }
}
