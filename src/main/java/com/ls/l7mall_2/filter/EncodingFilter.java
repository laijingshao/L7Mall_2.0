package com.ls.l7mall_2.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author laijs
 * @date 2020-3-30-20:35
 */
public class EncodingFilter implements Filter {
    
    private String encoding="UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (filterConfig.getInitParameter("ENCODING") != null){
            encoding = filterConfig.getInitParameter("ENCODING");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
