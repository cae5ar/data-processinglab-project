package com.pstu.dtl.server.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class CustomAutoLoginFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }
//    public final Logger log = Logger.getLogger(CustomAutoLoginFilter.class.getName());
//
//    private String principalRequestHeader = "iv-user";
//    private static final String GET_CURRENT_USER_URL = "https://login.mos.ru/eaidit/eaiditoauthweb/oauth/users/data?access_token=%1$s";
//
//    protected String getTokenFromSession(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null) return null;
//        return (String) session.getAttribute("token");
//    }
//
//    protected String getPrincipal(HttpServletRequest request) {
//        // log.error("getPrincipal currentUser = " + CurrentUser.getLogin());
//        // проверка на iv-user
//        String user = request.getHeader(principalRequestHeader);
//        if (!StringUtils.isEmpty(user)) return user;
//
//        // Проверка на Authorization Oauth {token}
//        String authorizationHeader = request.getHeader("Authorization");
//        // authorizationHeader = "Oauth c2e36654-f542-4230-b68d-75b2b0ad5a5c";
//        if (authorizationHeader != null) {
//            String[] params = authorizationHeader.split(" ");
//            if (params.length > 1) {
//                String token = params[1];
//
//                // log.error("getPrincipal token = " + token);
//                try {
//                    // Проверка на текущего пользователя
//                    if (token.equals(getTokenFromSession(request)) && CurrentUser.getLogin() != null)
//                        return CurrentUser.getLogin();
//
//                    String getUserUrl = String.format(GET_CURRENT_USER_URL, token);
//                    HttpClient httpClient = new DefaultHttpClient();
//                    httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("82.138.16.125", Integer.valueOf(8080)));
//                    HttpGet httpGet = new HttpGet(getUserUrl);
//
//                    HttpResponse httpResponse = httpClient.execute(httpGet);
//
//                    HttpEntity responseEntity = httpResponse.getEntity();
//                    String responseString = IOUtils.toString(responseEntity.getContent());
//                    // log.error("getPrincipal response = " + responseString);
//                    // {"access_token":{access_token},"token_type":"bearer","refresh_token":{refresh_token},"expires_in":{exp},"scope":"read write"}
//
//                    String sss = responseString.split(",")[0].split(":")[1].replace("\"", "");
//
//                    request.getSession().setAttribute("token", token);
//                    if ("FilimonovEA".equals(sss)) {
//                        return "fea";
//                    }
//
//                    return sss;
//
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return "";
//    }
//
//    @Override
//    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
//        String user = getPrincipal(request);
//        if (!StringUtils.isEmpty(user)) {
//            return CurrentUser.ROLE_USER;
//        }
//        else {
//            throw new PreAuthenticatedCredentialsNotFoundException(principalRequestHeader
//                    + "header not found in request.");
//        }
//    }
//
//    @Override
//    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
//        String user = getPrincipal(request);
//        if (!StringUtils.isEmpty(user)) {
//            return user;
//        }
//        else {
//            throw new PreAuthenticatedCredentialsNotFoundException(principalRequestHeader
//                    + " header not found in request.");
//        }
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
//            ServletException {
//        try {
//            super.doFilter(request, response, chain);
//        }
//        catch (PreAuthenticatedCredentialsNotFoundException ex) {
//            // SecurityContextHolder.clearContext();
//            chain.doFilter(request, response);
//        }
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//            AuthenticationException failed) {
//        SecurityContextHolder.clearContext();
//        if (failed instanceof UsernameNotFoundException) {
//            try {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                request.getRequestDispatcher("/WEB-INF/jsp/deniedpage.jsp").forward(request, response);
//            }
//            catch (Exception e) {
//                logger.debug("Cleared security context due to exception", e);
//            }
//        }
//        if (logger.isDebugEnabled()) {
//            logger.debug("Cleared security context due to exception", failed);
//        }
//        request.getSession().setAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY, failed);
//    }

}