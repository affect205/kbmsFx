package com.kbmsfx.dao;

import com.kbmsfx.dto.CategoryDTO;
import com.kbmsfx.dto.NoticeDTO;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 11.07.16
 */
public interface IDataProvider {
    int addCategory(CategoryDTO category) throws Exception;
    int addNotice(NoticeDTO notice) throws Exception;
    List<CategoryDTO> getCategoryList() throws Exception;
    List<NoticeDTO> getNoticeList() throws Exception;
    void updateCategory(CategoryDTO category) throws Exception;
    void updateNotice(NoticeDTO notice) throws Exception;
    void deleteCategory(Collection<Integer> categoryIds) throws Exception;
    void deleteNotice(int id) throws Exception;
    void changeCategoryParent(int id, int parent) throws Exception;
    void changeNoticeParent(int id, int parent) throws Exception;
}
