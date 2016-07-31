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
import java.util.*;
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

    public CacheData() {}

    @PostConstruct
    public void init() throws Exception {
        System.out.println("init CacheData...");
        updateCategoryCache();
        updateNoticeCache();
        updateTreeCache();
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

        List<TreeItem<TItem>> children = items.stream()
                .filter(p::test)
                .map(item -> {
                    TreeItem<TItem> ti = new TreeItem<>(item);
                    if (item.getKind() == TreeKind.CATEGORY) ti.setExpanded(true);
                    return ti;
                })
                .collect(Collectors.toList());
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

    public void printTree() {
        System.out.println("printTree...");
        treeCache.forEach(c -> printTree(c));
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

    public void editTreeItem(TItem item) {
        System.out.println("editTreeItem....");
        if (item == null) return;
        try {
            if (item.getKind() == TreeKind.CATEGORY) {
                dataProvider.updateCategory(EntityUtils.toCategoryDTO(item));
                if (categoryCache.containsKey(item.getId())) {
                    categoryCache.put(item.getId(), (Category)item);
                    updateTreeCache(treeCache, item);
                }

            } else if (item.getKind() == TreeKind.NOTICE) {
                dataProvider.updateNotice(EntityUtils.toNoticeDTO(item));
                if (noticeCache.containsKey(item.getId())) {
                    noticeCache.put(item.getId(), (Notice)item);
                    updateTreeCache(treeCache, item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TItem addTreeItem(TItem item) throws Exception {
        System.out.println("addTreeItem....");
        int recId = -1;
        if (item == null) return null;
        if (item.getKind() == TreeKind.CATEGORY) {
            recId = dataProvider.addCategory(EntityUtils.toCategoryDTO(item));
        } else if (item.getKind() == TreeKind.NOTICE) {
            recId = dataProvider.addNotice(EntityUtils.toNoticeDTO(item));
        }
        item.setId(recId);
        updateCache(item);
        return item;
    }

    public void removeTreeItem(TreeItem<TItem> ti) throws Exception {
        System.out.println("removeTreeItem....");
        if (ti == null || ti.getValue() == null) return;
        if (ti.getValue().getKind() == TreeKind.CATEGORY) {
            Collection<Integer> categoryIds = EntityUtils.extractCatIds(ti);
            dataProvider.deleteCategory(categoryIds);
        } else if (ti.getValue().getKind() == TreeKind.NOTICE) {
            dataProvider.deleteNotice(ti.getValue().getId());
        }
    }

    protected void updateCategoryCache() throws Exception {
        System.out.println("Update categories....");

        List<CategoryDTO> categoryDTOs = dataProvider.getCategoryList();
        categoryCache = new HashMap<>();
        categoryDTOs.forEach(dto -> {
            categoryCache.put(dto.getId(), new Category(dto.getId(), dto.getName(), dto.getParent() > 0 ? new Category(dto.getParent()) : null, dto.getSorting()));
        });

        categoryCache.keySet().forEach(key -> {
            Category category = categoryCache.get(key);
            if (category.getParent() != null) category.setParent(categoryCache.get(category.getParent().getId()));
            System.out.println(category.toString());
        });
    }

    protected void updateNoticeCache() throws Exception {
        List<NoticeDTO> noticeDTOs = dataProvider.getNoticeList();
        noticeCache = new HashMap<>();
        noticeDTOs.forEach(dto -> {
            noticeCache.put(dto.getId(), new Notice(dto.getId(), dto.getName(), dto.getContent(), new Category(dto.getCategoryid()), dto.getSorting()));
        });

        System.out.println("Update notices....");
        noticeCache.keySet().forEach(key -> {
            Notice notice = noticeCache.get(key);
            if (notice.getCategory() != null) notice.setCategory(categoryCache.get(notice.getCategory().getId()));
            System.out.println(notice.toString());
        });
    }

    protected void updateTreeCache() throws Exception {
        System.out.println("Update tree cache....");
        List<TItem> nodeList = new LinkedList<TItem>(){{
            addAll(categoryCache.values());
            addAll(noticeCache.values());
        }};
        treeCache = getItemChildren(nodeList, null);
        treeCache.forEach(c -> printTree(c));
    }

    protected void updateTreeCache(List<TreeItem<TItem>> tree, TItem item) throws Exception {
        if (item == null) return;
        tree.forEach(ti -> {
            TItem value = ti.getValue();
            if (value.baseEquals(item)) {
                ti.setValue(item);
                System.out.printf("Tree item %s has changed\n", item.getId());
                return;
            } else {
                try {
                    updateTreeCache(ti.getChildren(), item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void addTreeCache(List<TreeItem<TItem>> tree, TItem parent, TItem item) throws Exception {
        if (item == null || (item.getKind() == TreeKind.NOTICE && parent == null)) return;
        tree.forEach(ti -> {
            TItem value = ti.getValue();
            if (value.baseEquals(parent) && value.getKind() == TreeKind.CATEGORY) {
                List<TreeItem<TItem>> children = ti.getChildren();
                TreeItem<TItem> newTi = new TreeItem<>(item);
                if (!children.contains(newTi)) children.add(newTi);
                System.out.printf("Tree item %s has added\n", item.getId());
                return;
            } else {
                try {
                    addTreeCache(ti.getChildren(), parent, item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void updateCache(TItem item) throws Exception {
        System.out.printf("updateCache id: %s...\n", item.getId());
        if (item == null) return;
        boolean isNew = false;
        if (item.getKind() == TreeKind.CATEGORY) {
            categoryCache.put(item.getId(), (Category)item);
        } else if (item.getKind() == TreeKind.NOTICE) {
            noticeCache.put(item.getId(), (Notice)item);
        }
    }
}
