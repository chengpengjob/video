package com.cp.video.configure;

import com.cp.video.model.RespBean;
import com.cp.video.model.User;
import com.cp.video.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Action;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author feipeng
 * @site www.gcp168.cn
 * @create 2020-04-28 22:08
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")
                .loginPage("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        User user = (User) authentication.getPrincipal();
                        user.setPassword(null);
                        RespBean ok = RespBean.ok("登录成功！", user);
                        String s = new ObjectMapper().writeValueAsString(ok);
                        out.write(s);
                        out.flush();
                        out.close();

                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        RespBean respBean = RespBean.error("登录失败！");
                        if (e instanceof LockedException) {
                            respBean.setMsg("账户被锁定，请联系管理员！");
                        } else if (e instanceof CredentialsExpiredException) {
                            respBean.setMsg("密码过期，请联系管理员！");
                        } else if (e instanceof AccountExpiredException) {
                            respBean.setMsg("账户过期，请联系管理员！");
                        } else if (e instanceof DisabledException) {
                            respBean.setMsg("账户被禁用，请联系管理员！");
                        } else if (e instanceof BadCredentialsException) {
                            respBean.setMsg("用户名或者密码输入错误，请重新输入！");
                        }
                        out.write(new ObjectMapper().writeValueAsString(respBean));
                        out.flush();
                        out.close();

                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功！")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .csrf().disable();

    }
}
