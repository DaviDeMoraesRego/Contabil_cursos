package br.com.contabil.courses.exception;

public class InternalServerError extends Exception {

	private static final long serialVersionUID = 1L;

	public InternalServerError(String message) {
		super(message);
	}
}
