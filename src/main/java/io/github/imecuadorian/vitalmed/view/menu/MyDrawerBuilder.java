package io.github.imecuadorian.vitalmed.view.menu;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.view.forms.*;
import io.github.imecuadorian.vitalmed.view.forms.admin.*;
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


    public static MyDrawerBuilder getInstance() {
        if (instance == null) {
            instance = new MyDrawerBuilder();
        }
        return instance;
    }


    public void setUser(User user) {
        boolean updateMenuItem = this.user == null;

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
                new Item.Label(I18n.t("dashboard.menuItem.patient")),
                new Item(I18n.t("dashboard.menuItem.information"),"dashboard.svg", FormDashboard.class),

                new Item.Label(I18n.t("dashboard.menuItem.appointment")),
                new Item(I18n.t("dashboard.menuItem.scheduleAppointment"),"calendar.svg", FormAppointmentScheduling.class), // Paciente
                new Item(I18n.t("dashboard.menuItem.appointmentManagement"), "components.svg"), // Doctor y Admin

                new Item.Label(I18n.t("dashboard.menuItem.medicalRecord")),
                new Item(I18n.t("dashboard.menuItem.seeClinicalHistory"), "page.svg"), // Doctor

                new Item.Label(I18n.t("dashboard.menuItem.administration")),
                new Item(I18n.t("dashboard.menuItem.registerDoctors"),"forms.svg", FormRegisterDoctor.class), // Admin
                new Item(I18n.t("dashboard.menuItem.administrationPatients"), "chart.svg", FormPatientManagement.class), // Admin
                new Item(I18n.t("dashboard.menuItem.assignmentOfSchedules"), "calendar.svg"), // Admin

                new Item(I18n.t("dashboard.menuItem.logout"), "logout.svg")
        };

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
                .setIconScale(0.45f);

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
