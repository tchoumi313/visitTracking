package com.example.visites.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	String resourceName;
	String field;
	String fieldName;
	Long fieldId;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%s %s: %s n'a pas ete trouve !!!", resourceName, field, fieldName));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}

	public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
		super(String.format("%s %s: %d n'a pas ete trouve !!!", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}
}
