package edu.ualberta.cs.papersdb;

public class ImportException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ImportException(String message) {
        super(message);
    }

    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
