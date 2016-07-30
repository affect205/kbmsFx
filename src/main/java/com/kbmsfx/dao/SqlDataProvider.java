package com.kbmsfx.dao;

import com.kbmsfx.db.SQLiteDBConnection;
import com.kbmsfx.dto.CategoryDTO;
import com.kbmsfx.dto.NoticeDTO;
import com.kbmsfx.utils.DBUtils;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 11.07.16
 */
@Dependent
@Alternative
public class SqlDataProvider implements IDataProvider {

    @Inject
    SQLiteDBConnection dbConn;

    @Override
    public void addNotice(NoticeDTO notice) throws Exception {
        System.out.println("addNotice....");
    }

    @Override
    public void addCategory(CategoryDTO category) throws Exception {
        System.out.println("addCategory....");
        Statement stmt = null;
        try {
            stmt = dbConn.getConnection().createStatement();
            stmt.execute("INSERT INTO category ('name', 'order', 'parent') VALUES ('тестовая запись2', 1, 1); ");
        } finally {
            DBUtils.close(stmt);
        }
    }

    @Override
    public List<CategoryDTO> getCategoryList() throws Exception {
        System.out.println("getCategoryList....");
        List<CategoryDTO> result = new LinkedList<>();
        ResultSet rs = null;
        try {
            rs = dbConn.getConnection().createStatement().executeQuery("SELECT * FROM category ORDER BY sorting");
            while(rs.next()) {
                CategoryDTO dto = new CategoryDTO(rs.getInt("id"), rs.getString("name"), rs.getInt("parent"), rs.getInt("sorting"));
                result.add(dto);
            }
            return result;
        } finally {
            DBUtils.close(rs);
        }
    }

    @Override
    public List<NoticeDTO> getNoticeList() throws Exception {
        System.out.println("getNoticeList....");
        List<NoticeDTO> result = new LinkedList<>();
        ResultSet rs = null;
        try {
            rs = dbConn.getConnection().createStatement().executeQuery("SELECT * FROM notice ORDER BY sorting");
            while(rs.next()) {
                NoticeDTO dto = new NoticeDTO(rs.getInt("id"), rs.getString("name"), rs.getString("content"), rs.getInt("categoryid"), rs.getInt("sorting"));
                result.add(dto);
            }
            return result;
        } finally {
            DBUtils.close(rs);
        }
    }

    @Override
    public void updateCategory(CategoryDTO category) throws Exception {
        System.out.println("updateCategory....");
        PreparedStatement stmt = null;
        try {
            stmt = dbConn.getConnection().prepareStatement(
                    "UPDATE category SET " +
                            "name = ?, " +
                            "sorting = ?, " +
                            "parent = ? " +
                            "WHERE id = ?");
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getSorting());
            stmt.setInt(3, category.getParent());
            stmt.setInt(4, category.getId());
            stmt.execute();
        } finally {
            DBUtils.close(stmt);
        }
    }

    @Override
    public void updateNotice(NoticeDTO notice) throws Exception {
        System.out.println("updateCategory....");
        PreparedStatement stmt = null;
        try {
            stmt = dbConn.getConnection().prepareStatement(
                    "UPDATE notice SET " +
                    "name = ?, " +
                    "sorting = ?, " +
                    "categoryid = ? " +
                    "WHERE id = ?");
            stmt.setString(1, notice.getName());
            stmt.setInt(2, notice.getSorting());
            stmt.setInt(3, notice.getCategoryid());
            stmt.setInt(4, notice.getId());
            stmt.execute();
        } finally {
            DBUtils.close(stmt);
        }
    }
}
