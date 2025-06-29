package next.dao.template;

import core.jdbc.ConnectionManager;
import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateJdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(UpdateJdbcTemplate.class);

    public void update(User user, UserDao dao) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(dao.createQueryForUpdate());
            dao.setValuesForUpdate(user, pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update 실패: " + user.getUserId(), e);
        } finally {
            try{
                if (pstmt != null) {
                    pstmt.close();
                }
            }catch(SQLException e){
                log.warn("PreparedStatement 닫기 실패", e);
            }
            try{
                if (con != null) {
                    con.close();
                }
            }catch(SQLException e){
                log.warn("Connnection 닫기 실패", e);
            }
        }
    }
}
