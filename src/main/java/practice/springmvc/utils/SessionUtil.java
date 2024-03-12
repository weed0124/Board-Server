package practice.springmvc.utils;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class SessionUtil {
    private static final String LOGIN_MEMBER_ID = "LOGIN_MEMBER_ID";

    public SessionUtil() {
    }

    public static String getLoginMemberId(HttpSession session) {
        return (String) session.getAttribute(LOGIN_MEMBER_ID);
    }

    public static void setLoginMemberId(HttpSession session, String id) {
        session.setAttribute(LOGIN_MEMBER_ID, id);
    }

    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
