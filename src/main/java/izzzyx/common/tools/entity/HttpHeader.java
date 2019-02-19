package izzzyx.common.tools.entity;

public enum HttpHeader {

    URL_ENCODE("application/x-www-form-urlencoded; charset=UTF-8");

    private String val;

    HttpHeader(String val){
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
