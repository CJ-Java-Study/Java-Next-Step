package core.jdbc;

import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JdbcTemplate {

    public void update() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();
            pstmt = con.prepareStatement(sql);
            setValues(pstmt);

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

    public abstract void setValues(PreparedStatement pstmt) throws SQLException;

    public abstract String createQuery();
}
