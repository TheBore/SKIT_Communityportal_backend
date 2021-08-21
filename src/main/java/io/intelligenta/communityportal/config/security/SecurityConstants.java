package io.intelligenta.communityportal.config.security;

public class SecurityConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String REQUEST_PARAM_TOKEN = "access_token";
    public static final String SECRET = "AuditSSH@AutTHT0ken";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String LOGIN_URL = "/rest/login";
    public static final String RESET_PASSWORD_URL = "/rest/user/resetpassword";
    public static final String RESET_PASSWORD_VERIFICATION_URL = "/rest/user/reset-password-verification";
    public static final String RESET_PASSWORD_SUCCESS_URL = "/rest/user/reset-password-success";
    public static final String DOWNLOAD = "/rest/attachment/download/*";
    public static final String SHOWFAQ = "/rest/faq/download/*";
    public static final String EXPORT_EXCEL = "/rest/yearlyreport/exportToXlsx";
    public static final String FAQ_URL = "/rest/faq/all";
    public static final String EXPORT_JSON = "/rest/feedback/analyse/*/export";
    public static final String INSTITUTIONS = "/rest/institution/all/moderators";
    public static final String USERS = "/rest/user/*/password";
}
