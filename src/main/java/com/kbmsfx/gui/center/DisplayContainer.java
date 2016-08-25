package com.kbmsfx.gui.center;

import com.kbmsfx.annotations.QAEvent;
import com.kbmsfx.entity.Notice;
import com.kbmsfx.entity.TItem;
import com.kbmsfx.enums.TreeKind;
import com.kbmsfx.events.RefreshQAEvent;
import com.kbmsfx.events.RefreshTreeEvent;
import com.kbmsfx.events.ShowNoticeQAEvent;
import com.kbmsfx.utils.CacheData;
import com.kbmsfx.utils.UrlUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Date;

/**
 * Created by Alex on 16.07.2016.
 */
@Dependent
public class DisplayContainer extends VBox {

    private VBox wrap;
    private TextField nameTf;
    private HTMLEditor contentHE;
    private TextArea contentTa;
    private TitledPane titledPane;
    private TabPane tabPane;
    private HBox toolbar;
    private TextField urlTf;
    private Button loadBtn;
    private Button saveBtn;

    private TreeItem<TItem> currentTI;

    @Inject
    private Event<RefreshTreeEvent> refreshTreeEvent;

    @Inject
    private Event<ShowNoticeQAEvent> noticeQAEvent;

    @Inject @QAEvent(QAEvent.QAТуре.CATEGORY)
    private Event<RefreshQAEvent> refreshCQAEvent;

    @Inject @QAEvent(QAEvent.QAТуре.NOTICE)
    private Event<RefreshQAEvent> refreshNQAEvent;

    @Inject
    private CacheData dataProvider;

    public DisplayContainer() {
        super();
        setAlignment(Pos.TOP_CENTER);
        System.out.println("create DisplayContainer " + new Date().getTime());
    }

    @PostConstruct
    protected void init() {

        toolbar = buildToolbar();

        nameTf = new TextField("");
        nameTf.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        contentTa = new TextArea();
        contentTa.setWrapText(true);

        contentHE = new HTMLEditor();

        Tab viewTab = new Tab("Просмотр");
        viewTab.setContent(contentHE);
        viewTab.setClosable(false);
        Tab editTab = new Tab();
        editTab.setText("Редактирование");
        editTab.setContent(contentTa);
        editTab.setClosable(false);

        tabPane = new TabPane();
        tabPane.getTabs().add(viewTab);
        tabPane.getTabs().add(editTab);
        tabPane.setSide(Side.LEFT);
        tabPane.getStylesheets().add("tab-header-background");
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    System.out.printf("old: %s, new: %s\n", t.getText(), t1.getText());
                    if (viewTab.equals(t1)) {
                        contentHE.setHtmlText(contentTa.getText());
                    } else if (editTab.equals(t1)) {
                        contentTa.setText(contentHE.getHtmlText());
                    }
                }
        );

        wrap = new VBox();
        wrap.getChildren().setAll(nameTf, tabPane);
        VBox.setMargin(nameTf, new Insets(0, 0, 10, 0));

        titledPane = new TitledPane("Просмотр", wrap);
        titledPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (titledPane.isCollapsible()) titledPane.setExpanded(true);
        });

        setMargin(titledPane, new Insets(10, 10, 0, 10));

        getChildren().add(titledPane);
        getChildren().add(toolbar);
    }

    public void setItem(TreeItem<TItem> ti) {
        if (ti == null || ti.getValue() == null) return;
        currentTI = ti;
        TItem currentItem = ti.getValue();
        nameTf.setText(currentItem.getName());
        urlTf.setText("http://");
        if (currentItem.getKind() == TreeKind.NOTICE) {
            Notice notice = (Notice)currentItem;
            contentHE.setHtmlText(notice.getContent());
            contentTa.setText(notice.getContent());
            tabPane.setDisable(false);
            loadBtn.setDisable(false);
            noticeQAEvent.fire(new ShowNoticeQAEvent(ti));
        } else {
            contentHE.setHtmlText("");
            contentTa.setText("");
            tabPane.setDisable(true);
            loadBtn.setDisable(true);
        }
    }

    protected HBox buildToolbar() {
        loadBtn = new Button("Загрузить");
        loadBtn.setOnAction(actionEvent -> {
            loadData();
        });

        urlTf = new TextField("http://");
        urlTf.setPrefWidth(210);
        urlTf.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                loadBtn.fire();
            }
        });

        saveBtn = new Button("Сохранить");
        saveBtn.setOnAction(actionEvent -> {
            saveData();
        });
        saveBtn.setMaxWidth(120);
        HBox spacer = new HBox();
        HBox topToolbar = new HBox(urlTf, loadBtn, spacer, saveBtn);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox.setMargin(topToolbar, new Insets(10, 10, 0, 10));
        return topToolbar;
    }

    public void saveData() {
        System.out.println("Save data...");
        if (currentTI == null || currentTI.getValue() == null) return;
        currentTI.getValue().setName(nameTf.getText().trim());
        if (currentTI.getValue().getKind() == TreeKind.NOTICE) {
            ((Notice)currentTI.getValue()).setContent(contentHE.getHtmlText());
        }
        dataProvider.editTreeItem(currentTI.getValue());
        refreshTreeEvent.fire(new RefreshTreeEvent(currentTI.getValue()));
        if (currentTI.getValue().getKind() == TreeKind.CATEGORY) {
            refreshCQAEvent.fire(new RefreshQAEvent(currentTI));
        } else if (currentTI.getValue().getKind() == TreeKind.NOTICE) {
            refreshNQAEvent.fire(new RefreshQAEvent(currentTI));
            refreshCQAEvent.fire(new RefreshQAEvent(currentTI));
        }
    }

    protected void loadData() {
        String html = UrlUtils.loadHtml(urlTf.getText().trim());
        contentHE.setHtmlText(html);
        contentTa.setText(html);
    }
}
