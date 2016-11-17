package shared;

import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class SocketIO
{
	public ObjectInputStream in;
	public ObjectOutputStream out;
	public Socket socket;

	public SocketIO(Socket socket) throws IOException
	{
		this.socket = socket;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
	}
}