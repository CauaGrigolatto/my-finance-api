package br.edu.ifsp.dsw.myfinanceapi.dto;

import java.util.List;

public class ResponseDTO<T> {
	private int status;
	private String message;
	private T data;
	
	private List<ErrorFieldDTO> errors;
	
	public ResponseDTO(int status, String message, T data, List<ErrorFieldDTO> errors) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
		this.errors = errors;
	}
	
	public ResponseDTO() {}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public List<ErrorFieldDTO> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorFieldDTO> errors) {
		this.errors = errors;
	}
}
