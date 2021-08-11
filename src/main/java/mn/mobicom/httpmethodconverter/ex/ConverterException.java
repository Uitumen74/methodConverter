package mn.mobicom.httpmethodconverter.ex;

/**
 *
 * @author uitumen.t
 */
public class ConverterException extends Exception {

    private final ErrorCode code;

    public ConverterException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }

}
