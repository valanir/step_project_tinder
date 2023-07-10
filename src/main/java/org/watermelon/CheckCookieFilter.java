package org.watermelon;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class CheckCookieFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request0, ServletResponse response0, FilterChain filterChain) throws IOException, ServletException {
        if (request0 instanceof HttpServletRequest &&
        response0 instanceof HttpServletResponse){
            HttpServletRequest request = (HttpServletRequest) request0;
            HttpServletResponse response = (HttpServletResponse) response0;

            Cookie[] cs = request.getCookies();
            Optional<Cookie> cookieId = Optional.ofNullable(cs)
                    .flatMap(cc -> Arrays.stream(cc).filter(c -> c.getName().equals("id")).findFirst()
                    );

            if (cookieId.isPresent()){
                filterChain.doFilter(request0, response0);
            }else response.sendRedirect("/login");
        } else {
            filterChain.doFilter(request0, response0);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
