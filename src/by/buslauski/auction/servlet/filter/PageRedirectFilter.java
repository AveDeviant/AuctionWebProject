package by.buslauski.auction.servlet.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Acer on 29.03.2017.
 */
@WebFilter(urlPatterns = {"/jsp/*"}, initParams = {@WebInitParam(name = "INDEX_PAGE", value = "/index.jsp")})
public class PageRedirectFilter implements Filter {
    private String indexPage;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        indexPage = filterConfig.getInitParameter("INDEX_PAGE");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String responsePath = request.getContextPath() + indexPage;
        response.sendRedirect(responsePath);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
