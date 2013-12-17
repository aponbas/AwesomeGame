package cast;

public class InvalidByteArraySize extends Exception {
	private static final long serialVersionUID = 2973113730656047926L;
	private String message;
	public InvalidByteArraySize(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
}
