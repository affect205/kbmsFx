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
    public int addNotice(NoticeDTO notice) throws Exception {
        System.out.println("addNotice....");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = dbConn.getConnection().prepareStatement(
                    " INSERT INTO notice (name, content, categoryid, sorting) VALUES (?, ?, ?, ?); " +
                    " SELECT last_insert_rowid(); ");
            stmt.setString(1, notice.getName());
            stmt.setString(2, notice.getContent());
            stmt.setInt(3, notice.getCategoryid());
            stmt.setInt(4, notice.getSorting());
            stmt.execute();
            rs = dbConn.getConnection().createStatement().executeQuery("SELECT max(id) AS id FROM notice;");
            return rs.next() ? rs.getInt("id") : -1;
        } finally {
            DBUtils.close(rs);
            DBUtils.close(stmt);
        }
    }

    @Override
    public int addCategory(CategoryDTO category) throws Exception {
        System.out.println("addCategory....");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = dbConn.getConnection().prepareStatement("INSERT INTO category (name, parent, sorting) VALUES(?, ?, ?)");
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getParent());
            stmt.setInt(3, category.getSorting());
            stmt.execute();
            rs = dbConn.getConnection().createStatement().executeQuery("SELECT max(id) AS id FROM category;");
            return rs.next() ? rs.getInt("id") : -1;
        } finally {
            DBUtils.close(rs);
            DBUtils.close(stmt);
        }
    }

    @Override
    public void deleteCategory(int id) throws Exception {
        System.out.println("deleteCategory....");
        PreparedStatement stmt = null;
        try {
            stmt = dbConn.getConnection().prepareStatement("DELETE FROM category WHERE id = ?");
            stmt.setInt(1, id);
            stmt.execute();
        } finally {
            DBUtils.close(stmt);
        }
    }

    @Override
    public void deleteNotice(int id) throws Exception {
        System.out.println("deleteNotice....");
        PreparedStatement stmt = null;
        try {
            stmt = dbConn.getConnection().prepareStatement("DELETE FROM notice WHERE id = ?");
            stmt.setInt(1, id);
            stmt.execute();
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
