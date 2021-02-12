package wwwordz.shared;

import java.io.Serializable;

public class WWWordzException extends Exception implements Serializable {
	private static final long serialVersionUID = 1L;

	public WWWordzException() {

	}

	public WWWordzException(java.lang.String message, java.lang.Throwable cause) {
		super(message, cause);

	}

	public WWWordzException(java.lang.String message) {
		
		super(message);

	}

	public WWWordzException(java.lang.Throwable cause) {
		super(cause);

	}
	

}
