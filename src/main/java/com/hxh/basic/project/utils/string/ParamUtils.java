package com.hxh.basic.project.utils.string;

import com.hxh.basic.project.enums.ResultEnum;
import com.hxh.basic.project.exception.CustomException;
import com.hxh.basic.project.utils.MethodUtil;

public class ParamUtils {
    /**
     * 禁止实例化
     */
    private ParamUtils() {
    }

    public static void isnotNull(String url, String s) {
        if(StrUtils.isNull(url)){
            throw new CustomException(ResultEnum.REQ_PARAM_NOT_NULL.setParam(s), MethodUtil.getLineInfo());
        }
    }
}
