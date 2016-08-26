package com.github.bingoohuang.springrestclient.spring.api;

public class YunpianResult {
    int code;
    String msg;
    String detail;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YunpianResult that = (YunpianResult) o;

        if (code != that.code) return false;
        if (msg != null ? !msg.equals(that.msg) : that.msg != null)
            return false;
        return !(detail != null ? !detail.equals(that.detail) : that.detail != null);

    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "YunpianResult{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            ", detail='" + detail + '\'' +
            '}';
    }
}
