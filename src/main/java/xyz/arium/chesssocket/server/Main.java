package xyz.arium.chesssocket.server;

import java.io.IOException;

public class Main {
	public static void main(String[] args)  {
        try {
            ChessServer server = new ChessServer(9953);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
	}
}
