package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.FixedRequestParam;
import com.github.bingoohuang.springrestclient.annotations.FixedRequestParams;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringRestClientEnabled(baseUrl = "http://yunpian.com/v1")
@RequestMapping("/sms")
public interface YunpianApi {

    @FixedRequestParam(name = "apikey", value = "xxx")
    @RequestMapping("/send.json")
    @SuccInResponseJSONProperty(key = "code", value = "0")
    void send(@RequestParam("text") String text,
              @RequestParam("mobile") String mobile);

    @FixedRequestParams({
            @FixedRequestParam(name = "apikey", value = "xxx")
    })
    @RequestMapping("/send.json")
    @SuccInResponseJSONProperty(key = "code", value = "0")
    void send2(@RequestParam("text") String text,
               @RequestParam("mobile") String mobile);

    @RequestMapping("/send.json")
    YunpianResult send3(@RequestParam("text") String text,
                        @RequestParam("mobile") String mobile);

    class YunpianResult {
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
            if (msg != null ? !msg.equals(that.msg) : that.msg != null) return false;
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
}
