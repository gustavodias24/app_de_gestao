package benicio.soluces.appdegesto.model;

public class ResponseModel {
    String msg;
    Boolean success;

    public ResponseModel() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
