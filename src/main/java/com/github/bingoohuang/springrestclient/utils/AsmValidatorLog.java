package com.github.bingoohuang.springrestclient.utils;

import com.github.bingoohuang.asmvalidator.AsmParamsValidatorFactory;
import com.github.bingoohuang.asmvalidator.ex.AsmValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsmValidatorLog {
    public static void validate(
            Class<?> apiClass,
            String methodSignature,
            Object... parametersValues
    ) {
        try {
            AsmParamsValidatorFactory.validate(methodSignature, parametersValues);
        } catch (AsmValidateException e) {
            Logger log = LoggerFactory.getLogger(apiClass);
            log.warn("validate failed {}", e.getMessage());
            throw e;
        }
    }
}
