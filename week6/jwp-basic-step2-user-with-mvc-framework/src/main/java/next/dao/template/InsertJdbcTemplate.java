package next.dao.template;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcUtil;
import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(InsertJdbcTemplate.class);

    public void insert(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(createQueryForInsert());
            setValuesForInsert(user, pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert 실패", e);
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
    }

    private void setValuesForInsert(User user, PreparedStatement pstmt){
        try {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException("PreparedStatement 값 설정 실패: " + user.getUserId(), e);
        }
    }

    private String createQueryForInsert() {
        return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    }
}
