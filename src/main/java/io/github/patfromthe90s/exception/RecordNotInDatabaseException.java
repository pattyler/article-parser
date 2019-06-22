package io.github.patfromthe90s.exception;

/**
 * Exception denoting that a record did not exist in the database when it is expected to be there. (e.g. {@code "SELECT *
 * FROM table_name WHERE id = 1"} returns nothing, when you expect a row with {@code id = 1} exists within the table).
 * @author Patrick
 *
 */
public class RecordNotInDatabaseException extends Exception {
	
	// TODO alter this?
	private static final long serialVersionUID = -7956008359501826618L;

	public RecordNotInDatabaseException() {
		super();
	}
	
	public RecordNotInDatabaseException(String msg) {
		super(msg);
	}
	
	public RecordNotInDatabaseException(Throwable cause) {
		super(cause);
	}
	
	public RecordNotInDatabaseException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
