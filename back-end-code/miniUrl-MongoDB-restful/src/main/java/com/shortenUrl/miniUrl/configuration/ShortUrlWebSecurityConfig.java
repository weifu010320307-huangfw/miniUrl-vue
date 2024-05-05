package com.shortenUrl.miniUrl.configuration;

import java.io.IOException;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
public class ShortUrlWebSecurityConfig 
{





    /**
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/{*}", "/shortUrl", "/resources/**", "/registration/**", "/webjars/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/roleHierarchy")
                .hasRole("STAFF")
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout((logout) -> logout.        
			invalidateHttpSession(true) // Invalidate the user's HttpSession
			.deleteCookies("JSESSIONID") // Remove the session cookie
			.logoutSuccessUrl("/login?logout") // Redirect to the logout success URL
			.permitAll()	)

			.csrf(csrf -> {
				// document.cookie can't access cookie withhttponly
				// secure use https to ensure message transfer safe
				csrf
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())   
				.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler());
			})
			.addFilterAfter(new CsrfCookieFilter(), UsernamePasswordAuthenticationFilter.class)
			.cors((cors) -> corsConfigurationSource())
			.headers((headers) -> { 
			headers.frameOptions((frame) -> frame.sameOrigin());
			headers.contentSecurityPolicy(
				csp -> csp.policyDirectives("script-src 'self'; form-action 'self';  report-uri /report;") //report-to csp-violation-report;
			);
			});


		return http.build(); 
	}









	@Bean
	public PasswordEncoder encoder() 
	{
		return new BCryptPasswordEncoder();
	}
    

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
		roleHierarchy.setHierarchy(hierarchy);
		return roleHierarchy;
	}



	


	@Bean
	CorsConfigurationSource corsConfigurationSource() 
	{
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.addAllowedHeader("Content-Type");
		configuration.addAllowedHeader("X-XSRF-TOKEN");
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();


		Map<String, CorsConfiguration> corsConfigurations = new HashMap<>();

		corsConfigurations.put("/shortUrl", configuration);
		corsConfigurations.put("/login", configuration);
		corsConfigurations.put("/logout", configuration);
		corsConfigurations.put("/", configuration);
		corsConfigurations.put("/registration/userStatus", configuration);
		corsConfigurations.put("/registration", configuration);
		corsConfigurations.put("/userdata", configuration);
		corsConfigurations.put("/csrf", configuration);
		corsConfigurations.put("/report", configuration);
		corsConfigurations.put("/loginStatus", configuration);








		source.setCorsConfigurations(corsConfigurations);

		
		return source;
	}


}


// other neccessary class needed for Single page application csrf process
// Configure CookieCsrfTokenRepository with HttpOnly set to false so the cookie can be read by the JavaScript application.
// Configure a custom CsrfTokenRequestHandler that resolves the CSRF token based on 
// whether it is an HTTP request header (X-XSRF-TOKEN) or request parameter (_csrf).
// Configure a custom Filter to load the CsrfToken on every request, which will return a new cookie if needed.
final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler 
{
	private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
		/*
		 * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
		 * the CsrfToken when it is rendered in the response body.
		 */
		this.delegate.handle(request, response, csrfToken);
	}

	@Override
	public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) 
	{




		/*
		 * If the request contains a request header, use CsrfTokenRequestAttributeHandler
		 * to resolve the CsrfToken. This applies when a single-page application includes
		 * the header value automatically, which was obtained via a cookie containing the
		 * raw CsrfToken.
		 */
		if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
			return super.resolveCsrfTokenValue(request, csrfToken);
		}
		/*
		 * In all other cases (e.g. if the request contains a request parameter), use
		 * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
		 * when a server-side rendered form includes the _csrf request parameter as a
		 * hidden input.
		 */
		return this.delegate.resolveCsrfTokenValue(request, csrfToken);
	}
}

final class CsrfCookieFilter extends OncePerRequestFilter 
{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
		// Render the token value to a cookie by causing the deferred token to be loaded
		csrfToken.getToken();

		filterChain.doFilter(request, response);
	}
}