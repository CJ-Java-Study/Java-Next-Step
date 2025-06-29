package next.dao.template;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcUtil;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    public void update(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();
            pstmt = con.prepareStatement(sql);
            setValues(user, pstmt);
            pstmt.executeUpdate();
        }  catch (SQLException e) {
            throw new RuntimeException("JDBC 오류", e);
        }finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
    }

    protected abstract String createQuery();

    protected abstract void setValues(User user, PreparedStatement pstmt) throws SQLException;
}

