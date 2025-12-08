package br.com.contabil.courses.exception;

public class internalServerError extends Exception {

	private static final long serialVersionUID = 1L;

	public internalServerError(String message) {
		super(message);
	}
}
