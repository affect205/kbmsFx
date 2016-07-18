package com.kbmsfx.dao;

import com.kbmsfx.dto.CategoryDTO;
import com.kbmsfx.dto.NoticeDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 11.07.16
 */
public interface IDataProvider {
    void addCategory(CategoryDTO category) throws Exception;
    void addNotice(NoticeDTO notice) throws Exception;
    List<CategoryDTO> getCategoryList() throws Exception;
    List<NoticeDTO> getNoticeList() throws Exception;
}
