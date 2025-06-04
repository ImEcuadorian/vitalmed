package io.github.imecuadorian.vitalmed.view.menu;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.view.forms.*;
import io.github.imecuadorian.vitalmed.view.forms.admin.*;
import io.github.imecuadorian.vitalmed.view.forms.auth.*;
import io.github.imecuadorian.vitalmed.view.forms.doctor.*;
import io.github.imecuadorian.vitalmed.view.forms.patient.*;
import raven.extras.*;
import raven.modal.drawer.*;
import raven.modal.drawer.item.*;
import raven.modal.drawer.item.MenuItem;
import raven.modal.drawer.menu.*;
import raven.modal.drawer.renderer.*;
import raven.modal.drawer.simple.*;
import raven.modal.drawer.simple.footer.*;
import raven.modal.drawer.simple.header.*;
import raven.modal.option.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyDrawerBuilder extends SimpleDrawerBuilder
        implements LanguageChangeListener {

    private static MyDrawerBuilder instance;

    /**
     * Keep a reference to the MenuOption you passed into super(…),
     * so that you can re-set its menus later on language change.
     */
    private final MenuOption menuOption;

    private User user;

    private static final int SHADOW_SIZE = 12;

    public static MyDrawerBuilder getInstance() {
        if (instance == null) {
            instance = new MyDrawerBuilder();
            // Register this builder as a listener so onLanguageChanged(…) will be called
            I18n.addListener(instance);
        }
        return instance;
    }

    private MyDrawerBuilder() {
        // Generate the initial MenuOption and store it
        super(null);
        this.menuOption = createSimpleMenuOption();

        // Optional: if you want to react to theme switches (dark/light), you can keep this
        LightDarkButtonFooter lightDarkButtonFooter = (LightDarkButtonFooter) getFooter();
        lightDarkButtonFooter.addModeChangeListener(isDarkMode -> {
            // no-op or update something if needed
        });
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        AvatarIcon icon = new AvatarIcon(
                new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/avatar_male.svg", 100, 100),
                50, 50, 3.5f
        );
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(2, 2);

        return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle("Vitalmed")
                .setDescription("admin@vitalmed.com");
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
                .setTitle("Vitalmed")
                .setDescription("Version 1.0");
    }

    @Override
    public Option createOption() {
        Option option = super.createOption();
        option.setOpacity(0.3f);
        option.getBorderOption()
                .setShadowSize(new Insets(0, 0, 0, SHADOW_SIZE));
        return option;
    }

    @Override
    public int getDrawerWidth() {
        return 270 + SHADOW_SIZE;
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80 + SHADOW_SIZE;
    }

    @Override
    public int getOpenDrawerAt() {
        return 1000;
    }

    @Override
    public boolean openDrawerAtScale() {
        return false;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(
                FlatClientProperties.STYLE,
                getDrawerBackgroundStyle()
        );
    }

    private static String getDrawerBackgroundStyle() {
        return "[light]background:tint($Panel.background,20%);" +
               "[dark]background:tint($Panel.background,5%);";
    }

    /**
     * Whenever the language changes, we:
     * 1. Re-generate the MenuItem[] (calling I18n.t(...) afresh),
     * 2. Call menuOption.setMenus(...) with the new items,
     * 3. Tell Swing to repaint/update the drawer’s component tree.
     */
    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        MenuItem[] updatedItems = buildMenuItems();
        menuOption.setMenus(updatedItems);

        // Update any validation rules or indexes again:
        MyMenuValidation.setMenuItems(updatedItems);

        // Refresh the UI
        SwingUtilities.updateComponentTreeUI(getDrawerMenu());
    }

    /**
     * Expose a public setter so the rest of your application can tell
     * the drawer which User is logged in. If the role changed, rebuild the menu.
     */
    public void setUser(User user) {
        boolean updateMenuItem = this.user == null
                                 || !this.user.getRol().equals(user.getRol());
        this.user = user;
        MyMenuValidation.setUser(user);

        // Update header’s displayed name/email
        SimpleHeader header = (SimpleHeader) getHeader();
        SimpleHeaderData data = header.getSimpleHeaderData();
        data.setTitle(user.getFullName());
        data.setDescription(user.getEmail());
        header.setSimpleHeaderData(data);

        // If role changed, force a full menu rebuild (with new role-specific items)
        if (updateMenuItem) {
            MenuItem[] items = buildMenuItems();
            menuOption.setMenus(items);
            MyMenuValidation.setMenuItems(items);
            SwingUtilities.updateComponentTreeUI(getDrawerMenu());
        }
    }

    /**
     * Generate a fresh array of MenuItem[...] using I18n.t(...) calls.
     * This method is called both from the constructor (via createSimpleMenuOption)
     * and from onLanguageChanged(...) to refresh all labels.
     */
    private static MenuItem[] buildMenuItems() {
        // Note: we do NOT cache any of these label Strings; always call I18n.t(...)
        // in buildMenuItems(). That way, when the locale changes, I18n.t(...) returns
        // the new translation.

        return new MenuItem[]{
                // Top-level label
                new Item.Label(I18n.t("dashboard.menuItem.general.title")),
                // Single menu item (no submenu)
                new Item(
                        I18n.t("dashboard.menuItem.information"),
                        "dashboard.svg",
                        FormDashboard.class
                ),

                // Appointment Management Group
                new Item.Label(I18n.t("dashboard.menuItem.appointmentManagement.title")),
                new Item(
                        I18n.t("dashboard.menuItem.appointments"),
                        "calendar.svg"
                )
                        .subMenu(
                                I18n.t("dashboard.menuItem.scheduleAppointment"),
                                FormAppointmentScheduling.class
                        )
                        .subMenu(
                        I18n.t("dashboard.menuItem.appointmentManagement"),
                        FormAppointmentManagement.class
                ),

                // Medical History Group
                new Item.Label(I18n.t("dashboard.menuItem.medicalHistory.title")),
                new Item(
                        I18n.t("dashboard.menuItem.seeClinicalHistory"),
                        "page.svg",
                        FormMedicalHistory.class
                ),

                // Administration Group
                new Item.Label(I18n.t("dashboard.menuItem.administration.title")),
                new Item(
                        I18n.t("dashboard.menuItem.administer"),
                        "forms.svg"
                )
                        .subMenu(
                                I18n.t("dashboard.menuItem.registerDoctors"),
                                FormDoctorManagementAdmin.class
                        )
                        .subMenu(
                                I18n.t("dashboard.menuItem.administrationPatients"),
                                FormPatientManagement.class
                        )
                        .subMenu(
                        I18n.t("dashboard.menuItem.assignmentOfSchedules"),
                        FormScheduleAssignment.class
                ),

                // Account Group
                new Item.Label(I18n.t("dashboard.menuItem.account.title")),
                new Item(
                        I18n.t("dashboard.menuItem.updateData"),
                        FormUpdateData.class
                ),
                new Item(
                        I18n.t("dashboard.menuItem.restartPassword"),
                        FormResetPassword.class
                ),
                new Item(
                        I18n.t("dashboard.menuItem.logout"),
                        "logout.svg",
                        FormLogout.class
                )
        };
    }

    /**
     * createSimpleMenuOption() is only used once, at startup, to pass
     * the very first MenuOption into super(...). After that, onLanguageChanged
     * will re-use the same menuOption instance to impose a new MenuItem[].
     */
    public static MenuOption createSimpleMenuOption() {
        MenuOption simpleMenuOption = new MenuOption();

        // Build the initial items (in whatever locale is active at startup)
        MenuItem[] items = buildMenuItems();

        // Assign indexes (just like you did before)
        int visibleIndex = 0;
        for (MenuItem mi : items) {
            if (mi instanceof Item item) {
                item.initIndexOnNull(new int[]{visibleIndex}, true);
                if (item.isSubmenuAble()) {
                    int subIndex = 0;
                    for (Item sub : item.getSubMenu()) {
                        sub.initIndexOnNull(new int[]{visibleIndex, subIndex}, true);
                        subIndex++;
                    }
                }
                visibleIndex++;
            }
        }

        // Remember these items for any validation logic
        MyMenuValidation.setMenuItems(items);

        // Apply custom styling, drawer background, etc.
        simpleMenuOption.setMenuStyle(new MenuStyle() {
            @Override
            public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
                boolean isTopLevel = index.length == 1;
                if (isTopLevel) {
                    menu.putClientProperty(FlatClientProperties.STYLE, "margin:-1,0,-1,0;");
                }
            }

            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(
                        FlatClientProperties.STYLE,
                        getDrawerBackgroundStyle()
                );
            }
        });

        simpleMenuOption.getMenuStyle()
                .setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());

        simpleMenuOption.setMenus(items)
                .setBaseIconPath("io/github/imecuadorian/vitalmed/icon/drawer")
                .setIconScale(0.45f)
                .setMenuValidation(new MyMenuValidation());

        return simpleMenuOption;
    }

    // Utility method to fetch the actual JComponent of the drawer’s menu,
    // used in SwingUtilities.updateComponentTreeUI(...)
    public DrawerMenu getDrawerMenu() {
        // SimpleDrawerBuilder provides getMenu() or similar; if not,
        // you can retrieve it from the DrawerPanel or keep a reference.
        return (DrawerMenu) super.getMenu();
    }
}
