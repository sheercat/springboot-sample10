package net.vg4;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.val;

@Configuration
@EnableWebSecurity
@val
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/css/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/loginForm", "/api/**", "/hello", "/metrics/**", "/info", "/health", "/env/**",
						"/mappings", "/beans", "/configprops", "/trace", "/flyway", "/dump", "/autoconfig")
				.permitAll().anyRequest().authenticated();

		// まちがい
		// val mcr = http.antMatcher("/api/**").csrf();
		// System.out.println("!!!" + ToStringBuilder.reflectionToString(mcr));
		// http.antMatcher("/api/**").csrf().disable();

		// 正解
		http.csrf().ignoringAntMatchers("/api/**");
		// System.out.println("!!!" + ToStringBuilder.reflectionToString(mcr2));
		
		http.formLogin().loginProcessingUrl("/login").loginPage("/loginForm").failureUrl("/loginForm?error")
				.defaultSuccessUrl("/customers", true).usernameParameter("username").passwordParameter("password");
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/loginForm");
	}

	@Configuration
	static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
		@Autowired
		UserDetailsService userDetailsService;

		@Bean
		PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

	}

}
