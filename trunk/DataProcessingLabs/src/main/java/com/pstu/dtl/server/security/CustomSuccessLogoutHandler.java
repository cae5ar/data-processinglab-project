package com.pstu.dtl.server.security;

import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class CustomSuccessLogoutHandler extends SimpleUrlLogoutSuccessHandler {

//    @Autowired
//    DatabaseLogger databaseLogger;
//
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//            throws IOException, ServletException {
//        try {
//            Message message = databaseLogger.createMessage();
//            CustomUserDetails tmp = (CustomUserDetails) authentication.getPrincipal();
//            message.setUserName(tmp.getUsername());
//            message.setMethod(ProtocolEvent.LOGOUT);
//            message.setStart(new Date());
//            message.setEnd(new Date());
//            databaseLogger.log(message);
//            super.handle(request, response, authentication);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
