package xyz.arium.chesssocket.shared;

import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class SocketIO {
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public SocketIO(Socket socket) throws IOException {
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
	}

    public Message readMessage() throws ChessException {
        try {
            return (Message) in.readObject();
        } catch (IOException e) {
            throw new ChessException("Could not read message", e);
        } catch (ClassNotFoundException e) {
            throw new ChessException("Received bad message", e);
        }
    }

    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }
}
