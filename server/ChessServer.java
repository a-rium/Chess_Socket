package server;

import shared.Request;
import shared.SocketIO;
import shared.ChessMove;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import java.util.ArrayList;

import java.io.IOException;

public class ChessServer extends Thread
{
	private ServerSocket server;
	private SocketIO firstPlayer, secondPlayer; 

	private int port;

	private boolean running;

	private AccepterThread accepter;
	private ListenerThread playerOne, playerTwo;

	public ChessServer(int port)
	{
		this.port = port;
	}

	public void run()
	{
		while(running)
		{
			try
			{
				accepter = new AccepterThread(port);
				accepter.start();
				while(accepter.isRunning());
				ListenerThread playerOne, playerTwo;
				Request<String> firstPlayerChoice = (Request<String>) firstPlayer.in.readObject();
				Request<String> secondPlayerChoice = (Request<String>) secondPlayer.in.readObject();
				if(firstPlayerChoice.getData().equals(secondPlayerChoice.getData()))
				{
					if(Math.round(Math.random()) == 1)
					{
						firstPlayerChoice.setData("bianco");
						secondPlayerChoice.setData("nero");
						playerOne = new ListenerThread(firstPlayer);
						playerTwo = new ListenerThread(secondPlayer);
					}
					else
					{
						firstPlayerChoice.setData("bianco");
						secondPlayerChoice.setData("nero");
						playerOne = new ListenerThread(secondPlayer);
						playerTwo = new ListenerThread(firstPlayer);
					}
				}
				else
				{
					if(firstPlayerChoice.getData().equals("bianco"))
					{
						playerOne = new ListenerThread(firstPlayer);
						playerTwo = new ListenerThread(secondPlayer);
					}
					else
					{
						playerOne = new ListenerThread(secondPlayer);
						playerTwo = new ListenerThread(firstPlayer);
					}
				}
				firstPlayer.out.writeObject(firstPlayerChoice);
				secondPlayer.out.writeObject(secondPlayerChoice);
				playerOne.start();
				playerTwo.start();
				while(playerOne.isRunning() && playerTwo.isRunning());
			}
			catch(IOException ie) { ie.printStackTrace(); }
			catch(ClassNotFoundException cnfe) {}
			try
			{
				if(playerOne.isRunning())
					playerOne.halt();
				if(playerTwo.isRunning())
					playerTwo.halt();
			}
			catch(IOException ie) { ie.printStackTrace(); }
			firstPlayer = secondPlayer = null;		
		}
	}

	public void halt() throws IOException
	{
		if(playerOne != null)
			playerOne.halt();
		if(playerTwo != null)
			playerTwo.halt();
		if(accepter != null)
			accepter.halt();
		running = false;
	}

	private class AccepterThread extends Thread
	{
		private int port;
		private boolean running;
		private ServerSocket server;

		public AccepterThread(int port) throws IOException
		{
			server = new ServerSocket(port);
			this.port = port;
		}

		public void run()
		{
			running = true;
			while(running && (firstPlayer != null || secondPlayer != null))
			{
				try
				{
					Socket newConnection = server.accept();
					if(firstPlayer == null)
						firstPlayer = new SocketIO(newConnection);
					else if(secondPlayer == null)
						secondPlayer = new SocketIO(newConnection);
				}
				catch(IOException ie) {}
			}
		}

		public void halt()
		{
			running = false;
		}

		public boolean isRunning()
		{
			return (running && (firstPlayer != null || secondPlayer != null));
		}
	}

	public class ListenerThread extends Thread
	{
		private SocketIO connection;
		private boolean running;

		private ArrayList<SocketIO> receivers;

		public ListenerThread(SocketIO connection)
		{
			this.connection = connection;
			receivers = new ArrayList<SocketIO>(); 
		}

		public void run()
		{
			running = false;
			while(running)
			{
				try
				{
					Request<ChessMove> request = (Request<ChessMove>) connection.in.readObject();
					for(SocketIO receiver : receivers)
						receiver.out.writeObject(request);
					if(request.getRequest().equals("close"))
					{
						running = false;
					}
				}
				catch(SocketTimeoutException ste)
				{
					try
					{
						connection.socket.setSoTimeout(0);
						for(SocketIO receiver : receivers)
							receiver.out.writeObject(new Request<ChessMove>("close"));
					}
					catch(IOException ie) {}
				}
				catch(IOException i){}
				catch(ClassNotFoundException ie){}
			}
		}

		public void halt() throws IOException
		{
			running = false;
			connection.socket.setSoTimeout(1);
		}

		public void addReceiver(SocketIO receiver)
		{
			receivers.add(receiver);
		}

		public boolean isRunning()
		{
			return running;
		}

	}

}