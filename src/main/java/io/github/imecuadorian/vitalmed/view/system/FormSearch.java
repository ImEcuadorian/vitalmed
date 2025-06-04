package io.github.imecuadorian.vitalmed.view.system;

import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.component.*;
import io.github.imecuadorian.vitalmed.view.forms.search.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import org.jetbrains.annotations.*;
import raven.modal.*;
import raven.modal.drawer.item.*;
import raven.modal.option.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import static java.awt.event.KeyEvent.*;

public enum FormSearch {
    INSTANCE;
    public static final String ID = "buscar";
    private final Map<SystemForm, Class<? extends Form>> formsMap = new HashMap<>();
    private FormSearchPanel searchPanel;

    FormSearch() {
        for (Class<? extends Form> cls : getClassForms()) {
            if (cls.isAnnotationPresent(SystemForm.class)) {
                SystemForm f = cls.getAnnotation(SystemForm.class);
                formsMap.put(f, cls);
            }
        }
    }

    private Class<? extends Form> @NotNull [] getClassForms() {
        MenuItem[] menuItems = MyDrawerBuilder.getInstance().getSimpleMenuOption().getMenus();
        List<Class<?>> formClass = new ArrayList<>();
        getMenuClass(menuItems, formClass);
        return formClass.toArray(new Class[0]);
    }

    private void getMenuClass(MenuItem @NotNull [] menuItems, List<Class<?>> formClass) {
        for (MenuItem menu : menuItems) {
            if (menu.isMenu()) {
                Item item = (Item) menu;
                if (item.getItemClass() != null) {
                    formClass.add(item.getItemClass());
                }
                if (item.isSubmenuAble()) {
                    getMenuClass(item.getSubMenu().toArray(new Item[0]), formClass);
                }
            }
        }
    }

    public void installKeyMap(@NotNull JComponent component) {
        ActionListener key = e -> showSearch();
        component.registerKeyboardAction(key, KeyStroke.getKeyStroke(VK_F, CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void showSearch() {
        if (ModalDialog.isIdExist(ID)) {
            return;
        }
        Option option = ModalDialog.createOption();
        option.setAnimationEnabled(false);
        option.getLayoutOption().setMargin(20, 10, 10, 10).setLocation(Location.CENTER, Location.TOP);

        ModalDialog.showModal(FormManager.getFrame(), new EmptyModalBorder(getSearchPanel(), (controller, action) -> {
            if (action == EmptyModalBorder.OPENED) {
                searchPanel.searchGrabFocus();
            }
        }), option, ID);
    }

    private JPanel getSearchPanel() {
        if (searchPanel == null) {
            searchPanel = new FormSearchPanel(formsMap);
        }
        searchPanel.formCheck();
        searchPanel.clearSearch();
        return searchPanel;
    }
}
