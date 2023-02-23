package com.edu.oauth2service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@SuppressWarnings("deprecation")
//@Order(SecurityProperties.BASIC_AUTH_ORDER - 6)
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig /*extends WebSecurityConfigurerAdapter*/ {


    @Bean   //  authentication handler
    public UserDetailsService userDetailsService(/*PasswordEncoder passwordEncoder*/) {
//        UserDetails john = User.withUsername("john")
//                .password(passwordEncoder.encode("passw0rd123"))
//                .roles("USER").authorities("read").build();
//        UserDetails user = User.withUsername("user")
//                .password("{bcrypt}$2b$12$tcoaaq3PeqkerTb8OS2t5eTBKZFzrnFPJy.s1Fk8OcIZg0aQvUyeq")
//                .roles("USER").authorities("read").build();
//        UserDetails asksef = User.withUsername("AskSef")
//                .password("{bcrypt}$2b$12$tcoaaq3PeqkerTb8OS2t5eTBKZFzrnFPJy.s1Fk8OcIZg0aQvUyeq")
//                .roles("USER").authorities("read").build();
//        UserDetails admin = User.withUsername("admin")
//                .password("{bcrypt}$2b$12$tcoaaq3PeqkerTb8OS2t5eTBKZFzrnFPJy.s1Fk8OcIZg0aQvUyeq")
//                .roles("USER", "ADMIN")
//                .authorities("read").build();
        //
        return new UserInfoUserDetailsService();//InMemoryUserDetailsManager(john, user, asksef, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean       //   authorization handler
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity       .authorizeExchange().anyExchange().authenticated()               .and()                .oauth2ResourceServer().jwt();
        return httpSecurity.csrf().disable()
                .authorizeHttpRequests().requestMatchers("/welcome", "/addUser", "/login").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/book/**", "/issuer/**").authenticated()
                .and().build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    // @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()// disable for this application
//                .authorizeRequests()
////                .antMatchers("/oauth/token").permitAll()
////                .antMatchers("/actuator").permitAll()
////                .antMatchers("/swagger-ui.html").permitAll()
////                .antMatchers("/eureka/**").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .anonymous().disable(); //  disable anonymous
//
//                //      http.csrf().disable()                .authorizeRequests()
//                .anyRequest().permitAll();
//    }
}
