package com.kbmsfx.dao;

import com.kbmsfx.entity.Category;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 11.07.16
 * To change this template use File | Settings | File Templates.
 */
public interface IDataProvider {
    Category getCategoryTree();
}
