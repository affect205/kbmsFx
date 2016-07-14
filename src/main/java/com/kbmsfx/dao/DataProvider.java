package com.kbmsfx.dao;

import com.kbmsfx.db.DBConnection;
import com.kbmsfx.dto.CategoryDTO;
import com.kbmsfx.dto.NoticeDTO;
import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.utils.DBUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 11.07.16
 */
@Dependent
public class DataProvider {

    @Inject
    DBConnection dbConn;

    public void getCategoryTree() {
        System.out.println("I do something...");
    }

    public void addCategory() throws Exception {
        System.out.println("addCategory....");
        Statement stmt = null;
        try {
            stmt = dbConn.getConnection().createStatement();
            stmt.execute("INSERT INTO category ('name', 'order', 'parent') VALUES ('тестовая запись2', 1, 1); ");
        } finally {
            DBUtils.close(stmt);
        }
    }

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
}
