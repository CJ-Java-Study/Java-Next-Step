package next.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import core.jdbc.ConnectionManager;
import next.model.User;

public class UserDaoTest {
    @Before
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertEquals(expected, actual);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertEquals(expected, actual);
    }

    @Test
    public void findAll() throws Exception {
        UserDao userDao = new UserDao();
        List<User> users = userDao.findAll();
        assertEquals(1, users.size());
    }

    @Test
    public void update() throws Exception {
        UserDao userDao = new UserDao();

        // Given: 초기 사용자 생성 및 저장
        User originalUser = new User("testUser", "originalPass", "OriginalName", "original@test.com");
        userDao.insert(originalUser);

        // When: 업데이트할 새 정보 생성 및 업데이트 실행
        User updatedUser = new User(
                "testUser",
                "newPassword",
                "UpdatedName",
                "updated@test.com"
        );
        userDao.update(updatedUser);

        // Then: DB에서 조회한 값과 업데이트 예상값 비교
        User actual = userDao.findByUserId("testUser");

        assertNotNull("업데이트 후 사용자 정보는 null이 아니어야 함", actual);
        assertEquals("비밀번호 업데이트 검증", "newPassword", actual.getPassword());
        assertEquals("이름 업데이트 검증", "UpdatedName", actual.getName());
        assertEquals("이메일 업데이트 검증", "updated@test.com", actual.getEmail());
    }

}