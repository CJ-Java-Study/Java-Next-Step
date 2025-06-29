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

public class UpdateJdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(UpdateJdbcTemplate.class);

    public void update(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(createQueryForUpdate());
            setValuesForUpdate(user, pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update 실패: " + user.getUserId(), e);
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
    }
    private void setValuesForUpdate(User user, PreparedStatement pstmt){
        try {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException("PreparedStatement 값 설정 실패: " + user.getUserId(), e);
        }
    }

    private String createQueryForUpdate() {
        return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
    }
}
