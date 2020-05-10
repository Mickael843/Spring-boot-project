package br.com.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetaisService implementacaoUserDetaisService;

	@Override //Configura as solicitações de acesso por Http
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable() //Desabilita as configurações padrões de memoria do spring
		.authorizeRequests() //Permite restringir acessos
		.antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuario acesa a página principal
		.antMatchers(HttpMethod.GET, "/cadastroPessoa").hasRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().permitAll() // permite qualquer usuario
		.loginPage("/login")
		.defaultSuccessUrl("/cadastroPessoa")
		.failureUrl("/login?error=true")
		.and().logout() //Mapeia a URL de Logout e invalida o usuário autenticado
		.logoutSuccessUrl("/login")
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
	}
	
	@Override // Cria a autenticação do usuário com o banco de dados ou em memoria
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(implementacaoUserDetaisService)
			.passwordEncoder(new BCryptPasswordEncoder());
		
		
//		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//		.withUser("mickael")
//		.password("$2a$10$2j6RS1MUYJfP6cVGAPqmte95WyAEpX5BmY53frFzOi/GByehYBJeK")//mickaelluiz321
//		.roles("ADMIN");
	}
	
	@Override // Ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
}
