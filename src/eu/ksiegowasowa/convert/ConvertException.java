package eu.ksiegowasowa.convert;

public class ConvertException extends Exception {

	private static final long serialVersionUID = -1840851903844226432L;
	private String message;
	
	public ConvertException(String message) {
		super();
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
