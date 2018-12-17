package com.frame.hariko.springcloud.feign;

import org.apache.commons.lang3.StringUtils;

import com.frame.hariko.core.support.MyThreadContext;
import com.frame.hariko.core.support.UserContext;
import com.frame.hariko.web.base.constant.ESBConstants;
import com.frame.hariko.web.base.constant.WebConstants;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignFilter implements RequestInterceptor {
    private String applicationName;
    public FeignFilter(String applicationName) {
        this.applicationName = applicationName;
    }

    public void apply(RequestTemplate template) {
        template.header(WebConstants.HEADER_FOR_TRACE_ID, MyThreadContext.getTraceId())
                .header(WebConstants.HEADER_FOR_LOG_LEVEL, (String)MyThreadContext.get(MyThreadContext.LOG_LEVEL))
                .header(WebConstants.HEADER_FOR_USER_ID, UserContext.id())
                .header(WebConstants.HEADER_FOR_USER_NAME, UserContext.name())
                .header(WebConstants.HEADER_FOR_USER_MOBILE, UserContext.mobile())
                .header(WebConstants.HEADER_FOR_SYSTEM_TYPE, "DASHUF-NEW-SYSTEM")
                .header(WebConstants.HEADER_FOR_REQ_NAME, applicationName);
        //ESB请求设置请求头
        if(null != MyThreadContext.get(ESBConstants.HEARDER_FOR_ESBSVCNAME)) {
            template.header(ESBConstants.HEARDER_FOR_ESBSVCNAME, 
                    StringUtils.isNotBlank(MyThreadContext.get(ESBConstants.HEARDER_FOR_ESBSVCNAME).toString())
                            ? MyThreadContext.get(ESBConstants.HEARDER_FOR_ESBSVCNAME).toString(): "");
            template.header(ESBConstants.HEADER_FOR_SYSSVCNAME, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_SYSSVCNAME)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_SYSSVCNAME).toString(): "");
            template.header(ESBConstants.HEADER_FOR_ORISYS, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_ORISYS)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_ORISYS).toString(): "");
            template.header(ESBConstants.HEADER_FOR_TRANTIME, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_TRANTIME)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_TRANTIME).toString(): "");
            template.header(ESBConstants.HEADER_FOR_TRANNO, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_TRANNO)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_TRANNO).toString(): "");
            template.header(ESBConstants.HEADER_FOR_BUSSNO, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_BUSSNO)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_BUSSNO).toString(): "");
            template.header(ESBConstants.HEADER_FOR_USERID, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_USERID)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_USERID).toString(): "");
            template.header(ESBConstants.HEADER_FOR_TOKENID, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_TOKENID)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_TOKENID).toString(): "");
            template.header(ESBConstants.HEADER_FOR_SIGNATURE, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_SIGNATURE)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_SIGNATURE).toString(): "");
            template.header(ESBConstants.HEADER_FOR_DESTSYS, 
                    null != MyThreadContext.get(ESBConstants.HEADER_FOR_DESTSYS)
                            ? MyThreadContext.get(ESBConstants.HEADER_FOR_DESTSYS).toString(): "");
        }
    }
}
