package dev;

import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public class Client {

	static MyUtil U = new MyUtil();
	private Player myPlayer;
	private IPongServer server;
	
	//thread
	
	public Client(String ipHost){
		try {
			U.localMessage("Connecting to PongServer...");
			server = (IPongServer) Naming.lookup("//"+ipHost+":1099/PongServer");
			
			myPlayer = new Player();
			if(server.iWantToPlay((IPlayer)myPlayer)){
				startPongWindow();
			}else{
				U.localMessage("Not now my friend, go home.");
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startPongWindow(){
		new Pong(myPlayer, server);
	}
	
	
	
	public static void main(String[] args) {
		String ipHost = U.getIpHost(args);
		new Client(ipHost);
	}
	
}
