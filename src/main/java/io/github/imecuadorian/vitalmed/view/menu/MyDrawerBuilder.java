package io.github.imecuadorian.vitalmed.view.menu;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.view.forms.*;
import io.github.imecuadorian.vitalmed.view.forms.admin.*;
import io.github.imecuadorian.vitalmed.view.forms.admin.FormDoctorManagementAdmin;
import io.github.imecuadorian.vitalmed.view.forms.auth.*;
import io.github.imecuadorian.vitalmed.view.forms.doctor.*;
import io.github.imecuadorian.vitalmed.view.forms.patient.*;
import io.github.imecuadorian.vitalmed.view.system.*;
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

public class MyDrawerBuilder extends SimpleDrawerBuilder {

    private static MyDrawerBuilder instance;
    private User user;


    public static MyDrawerBuilder getInstance () {
        if (instance == null) {
            instance = new MyDrawerBuilder();
        }
        return instance;
    }


    public void setUser(User user) {
        boolean updateMenuItem = this.user == null || !this.user.getRol().equals(user.getRol());

        this.user = user;

        MyMenuValidation.setUser(user);

        SimpleHeader header = (SimpleHeader) getHeader();
        SimpleHeaderData data = header.getSimpleHeaderData();

        data.setTitle(user.getFullName());
        data.setDescription(user.getEmail());
        header.setSimpleHeaderData(data);

        if (updateMenuItem) {
            rebuildMenu();
        }
    }

    private static final int SHADOW_SIZE = 12;

    private MyDrawerBuilder() {
        super(createSimpleMenuOption());
        LightDarkButtonFooter lightDarkButtonFooter = (LightDarkButtonFooter) getFooter();
        lightDarkButtonFooter.addModeChangeListener(isDarkMode -> {
        });
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        AvatarIcon icon = new AvatarIcon(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/avatar_male.svg", 100, 100), 50, 50, 3.5f);
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

    public static MenuOption createSimpleMenuOption() {

        MenuOption simpleMenuOption = new MenuOption();

        MenuItem[] items = new MenuItem[]{
                new Item.Label(I18n.t("dashboard.menuItem.general.title")),                                       // index[0]
                new Item(I18n.t("dashboard.menuItem.information"), "dashboard.svg", FormDashboard.class), // [0]

                new Item.Label(I18n.t("dashboard.menuItem.appointmentManagement.title")),                              // index[1]
                new Item(I18n.t("dashboard.menuItem.appointments"), "calendar.svg")                                // index[2]
                        .subMenu(I18n.t("dashboard.menuItem.scheduleAppointment"), FormAppointmentScheduling.class)     // [2,0]
                        .subMenu(I18n.t("dashboard.menuItem.appointmentManagement"), io.github.imecuadorian.vitalmed.view.forms.doctor.FormDoctorManagement.class),      // [2,1]

                new Item.Label(I18n.t("dashboard.menuItem.medicalHistory.title")),                              // index[3]
                new Item(I18n.t("dashboard.menuItem.seeClinicalHistory"), "page.svg", FormMedicalHistory.class),   // [4]

                new Item.Label(I18n.t("dashboard.menuItem.administration.title")),                                // index[5]
                new Item(I18n.t("dashboard.menuItem.administer"), "forms.svg")                             // index[6]
                        .subMenu(I18n.t("dashboard.menuItem.registerDoctors"), FormDoctorManagementAdmin.class)               // [6,0]
                        .subMenu(I18n.t("dashboard.menuItem.administrationPatients"), FormPatientManagement.class)     // [6,1]
                        .subMenu(I18n.t("dashboard.menuItem.assignmentOfSchedules"), FormScheduleAssignment.class),    // [6,2]

                new Item.Label(I18n.t("dashboard.menuItem.account.title")),
                new Item("Actualizar Datos", FormUpdateData.class),
                new Item("Reiniciar Contraseña", FormResetPassword.class),// index[7]
                new Item(I18n.t("dashboard.menuItem.logout"), "logout.svg", FormLogout.class)                      // [8]
        };
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
        MyMenuValidation.setMenuItems(items);


        MyMenuValidation.setMenuItems(items);
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
                component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
            }
        });

        simpleMenuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
        simpleMenuOption.setMenuValidation(new MyMenuValidation());

        simpleMenuOption.addMenuEvent((action, index) -> {
            Class<?> itemClass = action.getItem().getItemClass();
            int i = index[0];
            if (i == 8) {
                action.consume();
                return;
            } else if (i == 9) {
                action.consume();
                int option = JOptionPane.showConfirmDialog(null, I18n.t("dashboard.menuItem.youWantToLogOut"), I18n.t("dashboard.menuItem.confirmLogout"), JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                return;
            }
            if (itemClass == null || !Form.class.isAssignableFrom(itemClass)) {
                action.consume();
                return;
            }
            Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
            FormManager.showForm(AllForms.getForm(formClass));
        });


        simpleMenuOption.setMenus(items)
                .setBaseIconPath("io/github/imecuadorian/vitalmed/icon/drawer")
                .setIconScale(0.45f).setMenuValidation(new MyMenuValidation());
        return simpleMenuOption;
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
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
    }

    private static String getDrawerBackgroundStyle() {
        return "[light]background:tint($Panel.background,20%);" +
               "[dark]background:tint($Panel.background,5%);";
    }
}
