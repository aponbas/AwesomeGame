package cast;

public class Cast {
	
	public static int byteArrayToInt(byte[] b) throws InvalidByteArraySize{
		if(b.length == 4){
			return b[0] << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | (b[3] & 0xFF);
		}
		else if(b.length == 2){
			return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xFF) << 8 | (b[1] & 0xFF);
		}
		throw new InvalidByteArraySize("b.length: " + b.length + " it should be 4 or 2");
	}
	
	
}
