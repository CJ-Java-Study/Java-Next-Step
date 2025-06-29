package next.dao.template;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcUtil;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public void insert(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(createQuery(true));
            setValues(user, pstmt, true);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert 실패", e);
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
    }
    public void update(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(createQuery(false));
            setValues(user, pstmt, false);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update 실패: " + user.getUserId(), e);
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
    }
    private void setValues(User user, PreparedStatement pstmt, boolean isInsert){
        try {
            if (isInsert) {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            } else {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("PreparedStatement 값 설정 실패: " + user.getUserId(), e);
        }
    }
    private String createQuery(boolean isInsert) {
        if (isInsert) {
            return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        } else {
            return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        }
    }
}
