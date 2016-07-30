package com.kbmsfx.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.kbmsfx.db.CassandraDBConnection;
import com.kbmsfx.dto.CategoryDTO;
import com.kbmsfx.dto.NoticeDTO;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abalyshev on 18.07.16.
 */
@Dependent
@Alternative
public class CassandraDataProvider implements IDataProvider {

    @Inject
    CassandraDBConnection casDbConn;

    @Override
    public void addNotice(NoticeDTO notice) throws Exception {
        System.out.println("addNotice....");
    }

    @Override
    public void addCategory(CategoryDTO category) throws Exception {
        System.out.println("addCategory....");
    }

    @Override
    public List<CategoryDTO> getCategoryList() throws Exception {
        System.out.println("getCategoryList....");
        List<CategoryDTO> result = new LinkedList<>();
        ResultSet rs = casDbConn.getConnection().execute("SELECT * FROM category");
        if (rs != null) {
            Iterator<Row> it = rs.iterator();
            while (it.hasNext()) {
                Row row = it.next();
                CategoryDTO dto = new CategoryDTO(row.getInt("id"), row.getString("name"), row.getInt("parent"), row.getInt("sorting"));
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<NoticeDTO> getNoticeList() throws Exception {
        System.out.println("getNoticeList....");
        List<NoticeDTO> result = new LinkedList<>();
        ResultSet rs = casDbConn.getConnection().execute("SELECT * FROM notice");
        if (rs != null) {
            Iterator<Row> it = rs.iterator();
            while (it.hasNext()) {
                Row row = it.next();
                NoticeDTO dto = new NoticeDTO(row.getInt("id"), row.getString("name"), row.getString("content"), row.getInt("categoryid"), row.getInt("sorting"));
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public void updateCategory(CategoryDTO category) throws Exception {

    }

    @Override
    public void updateNotice(NoticeDTO notice) throws Exception {

    }
}
