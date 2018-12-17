package com.frame.hariko.web.base.utils;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frame.hariko.core.support.MyThreadContext;
import com.frame.hariko.web.base.constant.ESBConstants;

public abstract class ESBUtil {
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss,SSS") 
    private static Date currentDate;

	/**
	 * 设置ESB请求头
	 * 
	 * @param request
	 * @return
	 */
	public static void ESBInit(String esbServName, String sysServName, String origSys, String destSys) {
		MyThreadContext.put(ESBConstants.HEARDER_FOR_ESBSVCNAME, esbServName);
		MyThreadContext.put(ESBConstants.HEADER_FOR_SYSSVCNAME, sysServName);
		MyThreadContext.put(ESBConstants.HEADER_FOR_ORISYS, origSys);
		MyThreadContext.put(ESBConstants.HEADER_FOR_DESTSYS, destSys);
		currentDate = new Date();
		MyThreadContext.put(ESBConstants.HEADER_FOR_TRANTIME, currentDate);
		MyThreadContext.put(ESBConstants.HEADER_FOR_TRANNO, MyThreadContext.getTraceId());
		MyThreadContext.put(ESBConstants.HEADER_FOR_BUSSNO, MyThreadContext.getTraceId());
	}
}
