package com.example.todobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@SpringBootApplication
public class TodoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoBackendApplication.class, args);
    }

    /**
     * Spring Sessionを入れない場合のデフォルトはSameSite指定なし（ブラウザによって挙動が異なる）。
     * Spring Sessionを入れた場合のデフォルトはSameSite=Lax。
     * フロントエンドからSESSION Cookieが送信できるようにSameSite=Noneに設定します。
     */
    @Bean
    public DefaultCookieSerializerCustomizer defaultCookieSerializerCustomizer() {
        return (defaultCookieSerializer) -> {
            defaultCookieSerializer.setSameSite("None");
        };
    }
}
