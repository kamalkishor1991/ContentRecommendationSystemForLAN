package utility;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import Log.Data;

public class Utility {
	/**
	 * Return Mac address of Address
	 * @param add IP Address
	 * @return Mac address in String.
	 * @throws SocketException
	 */
	public static String getMac(InetAddress add) throws SocketException,NullPointerException{
		
		NetworkInterface network = NetworkInterface.getByInetAddress(add);
		byte[] mac = network.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}
		Data.MAC_ADDRESS=sb.toString();
		return sb.toString();
		
	}

}
