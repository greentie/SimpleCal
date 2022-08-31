package org.gt.syntax;

public class ParseException extends RuntimeException{

	private static final long serialVersionUID = 7455467922855130488L;


	public ParseException() {
		super();
	}
	
	
	public ParseException(String msg) {
		super(msg);
	}
}
