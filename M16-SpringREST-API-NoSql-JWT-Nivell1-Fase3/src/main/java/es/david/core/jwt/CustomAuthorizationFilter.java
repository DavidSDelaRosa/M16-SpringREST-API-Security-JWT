package es.david.core.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Arrays.stream;

public class CustomAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if(request.getServletPath().equals("/api/login") ||(request.getServletPath().equals("/api/token/refresh"))) {
			
			filterChain.doFilter(request, response);
			
		}else {
			
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			
			if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
				
				//if there is there is an authentication Header and it starts withe Bearer...
				
				try {
					
					String token = authorizationHeader.substring("Bearer ".length()); //removing the word 'bearer'
					
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					
					JWTVerifier verifier = JWT.require(algorithm).build();
					
					DecodedJWT decodedJWT = verifier.verify(token);
					
					String username = decodedJWT.getSubject();						  //Get the user through decoding the JWT
					
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					
					stream(roles).forEach(role->{
						authorities.add(new SimpleGrantedAuthority(role));
					});
					
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
					
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					
					filterChain.doFilter(request, response);
					
				}catch(Exception exception) {
					
					System.out.println("Error login in: " + exception.getMessage());
					response.setHeader("error", exception.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
					
					Map<String,String> error = new HashMap<>();
					
					error.put("error_message", exception.getMessage());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				}
				
			}else {
				
				filterChain.doFilter(request, response);
				
			}
		}
		
		
	}

}