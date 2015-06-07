package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.RespStatusMapping;
import com.github.bingoohuang.springrestclient.annotations.RespStatusMappings;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.spring.exception.BadArgumentError;
import com.github.bingoohuang.springrestclient.spring.exception.NotFoundError;
import com.github.bingoohuang.springrestclient.spring.exception.OtherError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ex")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
public interface ExApi {
    /**
     * 如果不根据HTTP返回状态码定义异常，则统一抛出
     * com.github.bingoohuang.springrestclient.exception.RestException。
     */
    @RequestMapping("/exception1/{error}")
    int exception(@PathVariable("error") int error);

    /**
     * 根据HTTP返回状态码指定异常类型，则当匹配返回状态码时，返回指定类型的异常；
     * 无法匹配时，统一抛出com.github.bingoohuang.springrestclient.exception.RestException。
     * 当指定的异常类型是检查异常时，函数声明不许包含该异常。
     */
    @RequestMapping("/exception1/{error}")
    @RespStatusMappings({
            @RespStatusMapping(status = 404, exception = NotFoundError.class /* 检查异常，函数必须声明 */),
            @RespStatusMapping(status = 405, exception = BadArgumentError.class /* 检查异常，函数必须声明 */),
            @RespStatusMapping(status = 406, exception = OtherError.class /* 非检查异常，函数可以不声明 */)
    })
    int error(@PathVariable("error") int error) throws NotFoundError, BadArgumentError;


}
