package dev;

public class MyUtil {
	public String getFirstArg(String[] args){
		//establecer la IP del HOST
		String ipHostDefault = "192.168.2.14";
		String ipHost;
		if(args.length >= 1){
			ipHost = args[0];
		}else{
			System.out.println("Warning: no se ha especificado la IP del HOST! (default = "+ipHostDefault+")");
			ipHost = ipHostDefault;
		}
		return ipHost;
	}
	
	public String getSecondArg(String[] args){
		//establecer la IP del HOST
		String ipHostDefault = "192.168.2.14";
		String ipHost;
		if(args.length == 2){
			ipHost = args[1];
		}else{
			System.out.println("Warning: no se ha especificado la IP del GUEST! (default = "+ipHostDefault+")");
			ipHost = ipHostDefault;
		}
		return ipHost;
	}
	
	//mostrar mensaje en consola?
	public void localMessage(String m){
		System.out.println(m);
	}
}
