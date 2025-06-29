package next.dao;

import core.jdbc.ConnectionManager;
import next.dao.UserDao;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class InsertJdbcTemplate {

    public void insert(User user) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQuestForInsert();
            pstmt = con.prepareStatement(sql);
            setValuesForInsert(user, pstmt);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    protected abstract void setValuesForInsert(User user, PreparedStatement pstmt) throws SQLException;

    protected abstract String createQuestForInsert();

}
