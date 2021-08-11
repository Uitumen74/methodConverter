package mn.mobicom.httpmethodconverter.ex;

/**
 *
 * @author uitumen.t
 */
public enum ErrorCode {
    OK(0),
    TASK_NOT_FOUND(4001),
    FAILED(2000);

    private final Integer code;

    private ErrorCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
