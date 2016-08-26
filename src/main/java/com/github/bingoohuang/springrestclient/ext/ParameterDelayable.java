package com.github.bingoohuang.springrestclient.ext;

import java.util.Map;

public interface ParameterDelayable {
    String computeDelayedParam(Map<String, Object> mergedRequestParams);
}
