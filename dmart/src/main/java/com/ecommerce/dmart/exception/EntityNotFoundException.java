package com.ecommerce.dmart.exception;

public class EntityNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String entity, Long id) {
		 super(entity + " with ID " + id + " not found.");
	    }
	
	public EntityNotFoundException(String message) {
		 super(message);
	    }
}
