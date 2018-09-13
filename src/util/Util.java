package util;

public class Util {
	
	public static double distance(double lat1, double lon1, double lat2,  double lon2,
	        double el1, double el2) {
	 
		//el1 e el2 são as altitudes, em metros, dos dois pontos.
		//
	    final int R = 6371; // Raio da terra
	 
	    Double latDistance = deg2rad(lat2 - lat1);
	    Double lonDistance = deg2rad(lon2 - lon1);
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convertendo para metros
	 
	    double height = el1 - el2;
	    distance = Math.pow(distance, 2) + Math.pow(height, 2);// aplicando pitagoras
	    return Math.sqrt(distance);//retorna o comprimento da hipotenusa.
	}
	
	private static double deg2rad(double deg) {
	    return (deg * Math.PI / 180.0);
	}
}
