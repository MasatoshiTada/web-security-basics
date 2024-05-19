package com.example.sqlinjection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    /**
     * SQLインジェクションが起こり得る例
     */
    @Nested
    @DisplayName("findByUsernameAndPasswordBad()")
    class FindByUsernameAndPasswordBad {
        @Test
        @DisplayName("正しいメールアドレス・パスワードでユーザーを取得できる")
        void success() {
            Optional<User> actual = userRepository.findByUsernameAndPasswordBad("user02@mail.com", "user02");
            assertEquals(new User("user02@mail.com", "ユーザー02", "user02"), actual.get());
        }

        @Test
        @DisplayName("正しくないメールアドレス・パスワードでnull")
        void failure() {
            Optional<User> actual = userRepository.findByUsernameAndPasswordBad("user02@mail.com", "xxx");
            assertTrue(actual.isEmpty());
        }

        @Test
        @DisplayName("SQLインジェクションでユーザーを取得できる")
        void injection() {
            Optional<User> actual = userRepository.findByUsernameAndPasswordBad("", "' OR 1 = 1 --");
            assertEquals(new User("user01@mail.com", "ユーザー01", "user01"), actual.get());
        }
    }

    /**
     * SQLインジェクションが起こらない例
     */
    @Nested
    @DisplayName("findByUsernameAndPassword()")
    class FindByUsernameAndPassword {
        @Test
        @DisplayName("正しいメールアドレス・パスワードでユーザーを取得できる")
        void success() {
            Optional<User> actual = userRepository.findByUsernameAndPassword("user02@mail.com", "user02");
            assertEquals(new User("user02@mail.com", "ユーザー02", "user02"), actual.get());
        }

        @Test
        @DisplayName("正しくないメールアドレス・パスワードでnull")
        void failure() {
            Optional<User> actual = userRepository.findByUsernameAndPassword("user02@mail.com", "xxx");
            assertTrue(actual.isEmpty());
        }

        @Test
        @DisplayName("SQLインジェクションでユーザーを取得できない")
        void injection() {
            Optional<User> actual = userRepository.findByUsernameAndPassword("", "' OR 1 = 1 --");
            assertTrue(actual.isEmpty());
        }
    }
}
