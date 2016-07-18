package com.kbmsfx.utils;

import com.kbmsfx.dao.IDataProvider;
import com.kbmsfx.dto.CategoryDTO;
import com.kbmsfx.dto.NoticeDTO;
import com.kbmsfx.entity.Category;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import javafx.scene.control.TreeItem;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
@Singleton
public class CacheData {

    @Inject
    IDataProvider dataProvider;

    Map<Integer, Category> categoryCache;
    Map<Integer, Notice> noticeCache;
    List<TreeItem<TItem>> treeCache;

    public CacheData() {

    }

    @PostConstruct
    public void init() throws Exception {
        System.out.println("init CacheData...");

        List<NoticeDTO> noticeDTOs = dataProvider.getNoticeList();
        List<CategoryDTO> categoryDTOs = dataProvider.getCategoryList();

        categoryCache = new HashMap<>();
        categoryDTOs.forEach(dto -> {
            categoryCache.put(dto.getId(), new Category(dto.getId(), dto.getName(), dto.getParent() > 0 ? new Category(dto.getParent()) : null, dto.getSorting()));
        });

        noticeCache = new HashMap<>();
        noticeDTOs.forEach(dto -> {
            noticeCache.put(dto.getId(), new Notice(dto.getId(), dto.getName(), dto.getContent(), new Category(dto.getCategoryid()), dto.getSorting()));
        });

        System.out.println("Categories....");
        categoryCache.keySet().forEach(key -> {
            Category category = categoryCache.get(key);
            if (category.getParent() != null) category.setParent(categoryCache.get(category.getParent().getId()));
            System.out.println(category.toString());
        });

        System.out.println("Notices....");
        noticeCache.keySet().forEach(key -> {
            Notice notice = noticeCache.get(key);
            if (notice.getCategory() != null) notice.setCategory(categoryCache.get(notice.getCategory().getId()));
            System.out.println(notice.toString());
        });

        System.out.println("Tree cache....");
        List<TItem> nodeList = new LinkedList<TItem>(){{
            addAll(categoryCache.values());
            addAll(noticeCache.values());
        }};
        treeCache = getItemChildren(nodeList, null);
        treeCache.forEach(c -> printTree(c));
    }



    private List<TreeItem<TItem>> getItemChildren(List<TItem> items, TItem parent) {
        if (parent != null && parent.getKind() != TreeKind.CATEGORY) return new LinkedList<>();
        Predicate<TItem> p = (item) -> {
            if (TreeKind.CATEGORY == item.getKind()) {
                Category category = (Category)item;
                if (parent == null) return category.getParent() == null;
                int parentid = category.getParent() == null ? -1 : category.getParent().getId();
                return parentid == parent.getId() && parent.getId() != category.getId();

            } else if (TreeKind.NOTICE == item.getKind()){
                Notice notice = (Notice)item;
                if (parent == null) return false;
                int parentid = notice.getCategory() == null ? -1 : notice.getCategory().getId();
                return parentid == parent.getId();
            }
            return false;
        };

        List<TreeItem<TItem>> children = items.stream().filter(p::test).map(TreeItem::new).collect(Collectors.toList());
        children.forEach(item -> {
            item.getChildren().setAll(getItemChildren(items, item.getValue()));
        });
        return children;
    }

    private void printTree(TreeItem<TItem> item) {
        System.out.println(item.getValue().toString());
        item.getChildren().forEach(child -> {
            printTree(child);
        });
    }

    public List<TreeItem<TItem>> getTreeCache() {
        return treeCache;
    }

    public Map<Integer, Category> getCategoryCache() {
        return categoryCache;
    }

    public Map<Integer, Notice> getNoticeCache() {
        return noticeCache;
    }
}
