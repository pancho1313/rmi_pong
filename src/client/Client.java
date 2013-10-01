package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import server.IDate;


public class Client {

	public static void main(String[] args) {
		//establecer la IP del HOST
		String ipHostDefault = "192.168.2.14";
		String ipHost;
		if(args.length == 1){
			ipHost = args[0];
		}else{
			System.out.println("Warning: no se ha especificado la IP del HOST! (default = "+ipHostDefault+")");
			ipHost = ipHostDefault;
		}
		
		IDate date;
		try {
			date = (IDate) Naming.lookup("//"+ipHost+":1099/dateServer");
			System.out.println("Date... " + date.currentDate());
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
}
