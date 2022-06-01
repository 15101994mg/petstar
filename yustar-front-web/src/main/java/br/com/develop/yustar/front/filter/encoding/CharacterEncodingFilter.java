package br.com.develop.yustar.front.filter.encoding;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = {"*/*"})
public class CharacterEncodingFilter implements Filter, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		chain.doFilter(request, response);
 		
	}

}
