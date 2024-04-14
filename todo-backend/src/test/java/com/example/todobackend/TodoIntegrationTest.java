package com.example.todobackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.client.RestClient;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
class TodoIntegrationTest {

    RestClient restClient;

    @Autowired
    JdbcClient jdbcClient;

    @BeforeEach
    void beforeEach(@Autowired RestClient.Builder restClientBuilder, @LocalServerPort int port) {
        restClient = restClientBuilder
                .baseUrl("http://localhost:" + port)
                .build();
    }

    /**
     * ログインします。
     * @return セッションID（"JSESSIONID=xxxxxxxx"形式）とCSRFトークンを持つLoginResult
     */
    LoginResult login() {
        // CSRFトークン取得
        ResponseEntity<DefaultCsrfToken> csrfTokenResponse = restClient.get()
                .uri("/api/csrf")
                .retrieve()
                .toEntity(DefaultCsrfToken.class);
        String csrfToken = csrfTokenResponse.getBody().getToken();
        String sessionCookie = csrfTokenResponse.getHeaders().get("Set-Cookie").getFirst();
        String sessionId = Arrays.stream(sessionCookie.split(";"))
                .filter(element -> element.startsWith("JSESSIONID="))
                .findFirst()
                .get();

        // ログイン
        ResponseEntity<Void> loginResponse = restClient.post()
                .uri("/login")
                .body("""
                            {"username":"user","password":"user"}
                            """)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Cookie", sessionId)
                .header("X-CSRF-TOKEN", csrfToken)
                .retrieve()
                .toBodilessEntity();
        String newSessionCookie = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE).getFirst();
        String newSessionId = Arrays.stream(newSessionCookie.split(";"))
                .filter(element -> element.startsWith("JSESSIONID="))
                .findFirst()
                .get();
        return new LoginResult(newSessionId, csrfToken);
    }

    @Nested
    @DisplayName("TODOの登録")
    class Add {
        @Test
        @DisplayName("TODOを登録すると201")
        void success() {
            // CSRFトークン取得＋ログイン
            LoginResult loginResult = login();

            // 登録実行
            ResponseEntity<Void> response = restClient.post()
                    .uri("/api/todos")
                    .body("""
                            {"description":"Aさんと話す"}
                            """)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Cookie", loginResult.sessionId())
                    .header("X-CSRF-TOKEN", loginResult.csrfToken())
                    .retrieve()
                    .toBodilessEntity();

            // 検証
            assertAll(
                    () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                    () -> assertEquals("/api/todos/4", response.getHeaders().getLocation().toString()),
                    () -> assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcClient, "todo", "id = 4"))
            );
        }
    }
}
