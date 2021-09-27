package es.david.core.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	//The UserPasswordAuthenticationFilter acts when a POST request is sent to the endpoint : "/login"
	
	private final AuthenticationManager authenticationManager;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {

		this.authenticationManager = authenticationManager;

	}

	@Override
	public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws org.springframework.security.core.AuthenticationException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			org.springframework.security.core.Authentication authResult) throws IOException, ServletException {
		
		User user = (User) authResult.getPrincipal();
		
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		
		String accessToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+ 10 * 60 * 1000))
				.withIssuer(request.getRequestURI().toString())
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);

		String refreshToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+ 30 * 60 * 1000))
				.withIssuer(request.getRequestURI().toString())
				.sign(algorithm);
		
		Map<String,String> tokens = new HashMap<>();
		
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}
}