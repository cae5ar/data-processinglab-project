package com.pstu.dtl.server.security;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//            Authentication authentication) throws ServletException, IOException {
//        Message message = databaseLogger.createMessage();
//        CustomUserDetails tmp = (CustomUserDetails) authentication.getPrincipal();
//        message.setUserName(tmp.getUsername());
//        message.setMethod(ProtocolEvent.LOGIN);
//        message.setStart(new Date());
//        message.setEnd(new Date());
//        databaseLogger.log(message);
//        super.onAuthenticationSuccess(request, response, authentication);
//    }

}
