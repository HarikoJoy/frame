package com.frame.hariko.web.base.constant;

public interface WebConstants {

    String HEADER_FOR_TRACE_ID = "X-TRACE-ID";

    String HEADER_FOR_USER_TOKEN = "X-USER-TOKEN";
    /**
     * 请求调用方应用名
     */
    String HEADER_FOR_REQ_NAME = "X-REQ-NAME";

    /**
     * 用户id
     */
    String HEADER_FOR_USER_ID = "X-USER-ID";
    /**
     * 用户名
     */
    String HEADER_FOR_USER_NAME = "X-USER-NAME";
    /**
     * 用户电话
     */
    String HEADER_FOR_USER_MOBILE = "X-USER-MOBILE";
    /**
     * 用户所属公司id
     */
    String HEADER_FOR_USER_COMPANY_ID = "X-USER-COMPANY-ID";
    /**
     * 用户所属公司名
     */
    String HEADER_FOR_USER_COMPANY_NAME = "X-USER-COMPANY-NAME";
    /**
     * 用户角色id
     * 多个角色用","分割
     */
    String HEADER_FOR_USER_ROLE_IDS = "X-USER-ROLE-IDS";
    /**
     * 用户角色名
     * 多个角色用","分割
     */
    String HEADER_FOR_USER_ROLE_NAMES = "X-USER-ROLE-NAMES";
    /**
     * 处理中心ID
     */
    String HEADER_FOR_USER_HANDLE_CENTER_ID = "X-USER-HANDLE-CENTER-ID";

    /**
     * 处理中心名称
     */
    String HEADER_FOR_USER_HANDLE_CENTER_NAME = "X-USER-HANDLE-CENTER-NAME";
    /**
     * 日志级别
     */
    String HEADER_FOR_LOG_LEVEL = "X-LOG-LEVEL";
    
    String HEADER_FOR_SYSTEM_TYPE = "X-SYSTEM-TYPE";

    /**
     *  保存subject到request  attribute中
     */
    String REQUEST_ATTR_FOR_SUBJECT = "REQ-ATTR-FOR-SUBJECT";

}
