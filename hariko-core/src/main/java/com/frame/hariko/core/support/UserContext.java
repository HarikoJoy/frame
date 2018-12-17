package com.frame.hariko.core.support;

public final class UserContext {
    public static final String NAME = UserContext.class.getName() + "_ADMIN_NAME_KEY";
    public static final String MOBILE = UserContext.class.getName() + "_MOBILE_KEY";
    public static final String ID = UserContext.class.getName() + "_USER_ID_KEY";
    public static final String COMPANY_ID = UserContext.class.getName() + "_COMPANY_ID";
    public static final String COMPANY_NAME = UserContext.class.getName() + "_COMPANY_NAME";
    public static final String ROLE_IDS = UserContext.class.getName() + "_ROLE_IDS";
    public static final String ROLE_NAMES = UserContext.class.getName() + "_ROLE_NAMES";
    public static final String HANDLE_CENTER_ID = UserContext.class.getName() + "_HANDLE_CENTER_ID";
    public static final String HANDLE_CENTER_NAME = UserContext.class.getName() + "_HANDLE_CENTER_NAME";
    /**
     * 用户名
     */
    public static String name() {
        return MyThreadContext.get(NAME) + "";
    }
    /**
     * 用户id
     */
    public static String id() {
        return (String) MyThreadContext.get(ID);
    }
    /**
     * 用户电话
     */
    public static String mobile() {
        return MyThreadContext.get(MOBILE) + "";
    }
    /**
     * 用户所属公司id
     */
    public static String companyId() {
        return MyThreadContext.get(COMPANY_ID) + "";
    }
    /**
     * 用户所属公司名
     */
    public static String companyName() {
        return MyThreadContext.get(COMPANY_NAME) + "";
    }
    /**
     * 用户角色id
     */
    public static String[] roleIds() {
        Object rolesIds = MyThreadContext.get(ROLE_IDS);
        if (rolesIds != null) {
            return rolesIds.toString().split(",");
        }
        return null;
    }
    /**
     * 用户角色名
     */
    public static String[] roleNames() {
        Object roleNames = MyThreadContext.get(ROLE_NAMES);
        if (roleNames != null) {
            return roleNames.toString().split(",");
        }
        return null;
    }
    /**
     * 处理中心ID
     */
    public static String handleCenterId() {
        return MyThreadContext.get(HANDLE_CENTER_ID) + "";
    }
    /**
     * 处理中心名称
     */
    public static String handleCenterName() {
        return MyThreadContext.get(HANDLE_CENTER_NAME) + "";
    }
}
