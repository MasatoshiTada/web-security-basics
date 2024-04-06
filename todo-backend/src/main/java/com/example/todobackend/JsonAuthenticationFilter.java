package com.example.todobackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

/**
 * https://fintan.jp/page/373/ を参考に作成しました。
 */
public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonAuthenticationFilter(AuthenticationManager authenticationManager, HttpSessionSecurityContextRepository securityContextRepository) {
        super(new AntPathRequestMatcher("/login", HttpMethod.POST.name()));
        this.setAuthenticationManager(authenticationManager);
        // ログイン後のリダイレクトを抑制
        this.setAuthenticationSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()));
        // ログイン失敗時のリダイレクトを抑制
        this.setAuthenticationFailureHandler((req, res, ex) -> res.setStatus(HttpStatus.UNAUTHORIZED.value()));
        // SecurityContextをHttpSessionに保存する（デフォルトだとRequestAttributeSecurityContextRepositoryが使われるので上書き設定が必要）
        this.setSecurityContextRepository(securityContextRepository);
        // ログイン後のchangeSessionId()を有効化する（デフォルトだと）
        this.setSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        // 認証実行（失敗時は例外）
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
