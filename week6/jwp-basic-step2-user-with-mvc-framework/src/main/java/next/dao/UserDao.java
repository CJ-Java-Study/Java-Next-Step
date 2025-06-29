package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.jdbc.ConnectionManager;
import next.controller.UpdateUserController;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public void insert(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Insert 실패 : "+ user.getUserId(), e);
        }finally {
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

    public void update(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Update 실패 : "+ user.getUserId(), e);
        }finally {
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

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
                );
                users.add(user);
            }
            return users;
        } catch (SQLException e){
            throw new RuntimeException("Select 실패 : findAll", e);
        }finally {
            try{
                if (rs != null) {
                    rs.close();
                }
            }catch(SQLException e){
                log.warn("ResultSet 닫기 실패", e);
            }
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

    public User findByUserId(String userId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

            return user;
        } catch (SQLException e){
            throw new RuntimeException("Select 실패 : findByUserId", e);
        }finally {
            try{
                if (rs != null) {
                    rs.close();
                }
            }catch(SQLException e){
                log.warn("ResultSet 닫기 실패", e);
            }
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
