package com.example.visites.manager;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7);

			if (jwt.isEmpty() || jwt.isBlank()) {
				System.out.println("Ne Contient pas le token");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invlaid JWT token in Bearer Header");
			} else {
				try {
					String email = jwtUtil.validateTokenAndRetrieveSubject(jwt);

					UserDetails userDetails = userDetailsService.loadUserByUsername(email);

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
							new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());

					if (SecurityContextHolder.getContext().getAuthentication() == null) {
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				} catch (AccessDeniedException e) {
					System.out.println("Non autorise");
				} catch (TokenExpiredException e) {
					System.out.println("Expired token");
				} catch (JWTVerificationException e) {
					System.out.println("Erreur de validation token ");
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}