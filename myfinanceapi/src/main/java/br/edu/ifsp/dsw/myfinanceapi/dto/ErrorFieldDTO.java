package br.edu.ifsp.dsw.myfinanceapi.dto;

public class ErrorFieldDTO {
	private String field;
	private String message;
	
	public ErrorFieldDTO(String field, String message) {
		super();
		this.field = field;
		this.message = message;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
