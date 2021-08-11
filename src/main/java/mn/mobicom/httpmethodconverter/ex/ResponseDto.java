/*
 * This file is the property of Mobicom Corporation 
 * and may not be used outside the organization.
 */
package mn.mobicom.httpmethodconverter.ex;

/**
 *
 * @author uitumen.t
 */
public class ResponseDto {

    private ErrorCode code = ErrorCode.OK;
    private String info = "ok";

    public ResponseDto() {
    }

    public ResponseDto(ErrorCode code, String info) {
        this.code = code;
        this.info = info;
    }

    public void setCode(ErrorCode code) {
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

}
