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

    // category cache
    Map<Integer, Category> categoryCache;
    // notice cache
    Map<Integer, Notice> noticeCache;
    // tree cache
    List<TreeItem<TItem>> treeCache;
    // selected tree categories
    Deque<TreeItem<TItem>> categoryQACache;
    // selected tree notices
    Deque<TreeItem<TItem>> noticeQACache;

    public CacheData() {}

    @PostConstruct
    public void init() throws Exception {
        System.out.println("init CacheData...");
        updateCategoryCache();
        updateNoticeCache();
        updateTreeCache();
        categoryQACache = new LinkedList<>();
        noticeQACache  = new LinkedList<>();
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
                .map(item -> EntityUtils.buildTreeItem(item))
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
            if (value.customEquals(item)) {
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

    public Deque<TreeItem<TItem>> getNoticeQACache() {
        return noticeQACache;
    }

    public void addNoticeQACache(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null || ti.getValue().getKind() != TreeKind.NOTICE) return;
        if (!noticeQACache.contains(ti)) {
            noticeQACache.addFirst(ti);
        } else {
            noticeQACache.remove(ti);
            noticeQACache.addFirst(ti);
        }
        if (noticeQACache.size() >= 10) noticeQACache.removeLast();
    }

    public TItem updateNoticeQACache(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null || ti.getValue().getKind() != TreeKind.NOTICE) return null;
        TItem item =  ti.getValue();
        for (TreeItem<TItem> cacheTi : noticeQACache) {
            if (ti.getValue().customEquals(cacheTi.getValue())) {
                ti.getValue().setName(item.getName());
                return cacheTi.getValue();
            }
        }
        return null;
    }

    public void removeNoticeQACache(Integer noticeId) {
        System.out.printf("removeNoticeQACache: %s\n", noticeId);
        TreeItem<TItem> removed = null;
        for (TreeItem<TItem> ti : noticeQACache) {
            if (ti.getValue().getId() == noticeId) removed = ti;
        }
        if (removed != null) noticeQACache.remove(removed);
    }

    public Deque<TreeItem<TItem>> getCategoryQACache() {
        return categoryQACache;
    }

    /**
     * @return -1: no addition, 0: addition first, 1: addition first, removal last
     */
    public int addCategoryQACache(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null || ti.getValue().getKind() != TreeKind.CATEGORY) return -1;
        if (!categoryQACache.contains(ti)) {
            categoryQACache.addFirst(ti);
        } else {
            categoryQACache.remove(ti);
            categoryQACache.addFirst(ti);
        }
        if (categoryQACache.size() >= 10) {
            categoryQACache.removeLast();
            return 1;
        }
        return 0;
    }

    public void removeCategoryQACache(Integer noticeId) {
        System.out.printf("removeNoticeQACache: %s\n", noticeId);
        TreeItem<TItem> removed = null;
        for (TreeItem<TItem> ti : categoryQACache) {
            if (ti.getValue().getId() == noticeId) removed = ti;
        }
        if (removed != null) categoryQACache.remove(removed);
    }

    public Set<TreeItem<TItem>> updateCategoryQACache(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null) return null;
        return categoryQACache.stream()
                .filter(cacheTi -> updateCategoryQACache(cacheTi, ti))
                .collect(Collectors.toSet());
    }

    protected boolean updateCategoryQACache(TreeItem<TItem> parent, TreeItem<TItem> ti) {
        TItem item =  ti.getValue();
        if (item.customEquals(parent.getValue())) {
            parent.getValue().setName(item.getName());
            return true;
        }
        if (parent.getValue().getKind() == TreeKind.CATEGORY) {
            for (TreeItem<TItem> cacheTi : parent.getChildren()) {
                if (updateCategoryQACache(cacheTi, ti)) return true;
            }
        }
        return false;
    }
}
