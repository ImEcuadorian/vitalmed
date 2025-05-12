package io.github.imecuadorian.vitalmed.view.menu;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.view.forms.*;
import io.github.imecuadorian.vitalmed.view.forms.auth.*;
import io.github.imecuadorian.vitalmed.view.forms.doctor.*;
import io.github.imecuadorian.vitalmed.view.forms.patient.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import raven.modal.drawer.item.*;
import raven.modal.drawer.menu.*;

import java.util.*;

public class MyMenuValidation extends MenuValidation {

    private static User user;
    private static MenuItem[] menuItems;

    public static void setUser(User u) {
        user = u;
    }

    public static void setMenuItems(MenuItem[] items) {
        menuItems = items;
    }

    // Valida si el ítem de menú se debe mostrar
    @Override
    public boolean menuValidation(int[] index) {
        // Validación por clase del ítem
        Class<?> clazz = getClassByIndex(index);

        // Validación del contenedor de submenús
        if (clazz == null) {
            return hasVisibleSubMenu(index);
        }

        return validation((Class<? extends Form>) clazz);
    }

    private static boolean hasVisibleSubMenu(int[] parentIndex) {
        if (menuItems == null) return false;

        for (MenuItem mi : menuItems) {
            if (mi instanceof Item parent && Arrays.equals(parent.getIndex(), parentIndex)) {
                if (parent.isSubmenuAble()) {
                    for (Item sub : parent.getSubMenu()) {
                        Class<?> subClazz = sub.getItemClass();
                        if (subClazz != null && validation((Class<? extends Form>) subClazz)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    // Valida si una clase específica se puede acceder (usado en buscador, etc.)
    public static boolean validation(Class<? extends Form> clazz) {
        if (user == null) return false;

        return switch (user.getRol()) {
            case ADMIN -> true;  // El admin puede ver todo

            case DOCTOR -> (
                    clazz == FormDashboard.class ||
                    clazz == FormDoctorManagement.class ||
                    clazz == FormMedicalHistory.class ||
                    clazz == FormLogout.class
            );

            case PATIENT -> (
                    clazz == FormDashboard.class ||
                    clazz == FormAppointmentScheduling.class ||
                    clazz == FormLogout.class
            );
        };
    }


    // Obtener clase desde índice del menú
    private static Class<?> getClassByIndex(int[] index) {
        if (menuItems == null || index == null) return null;

        for (MenuItem mi : menuItems) {
            if (mi instanceof Item item && Arrays.equals(item.getIndex(), index)) {
                return item.getItemClass();
            }

            if (mi instanceof Item parent && parent.isSubmenuAble()) {
                for (Item sub : parent.getSubMenu()) {
                    if (Arrays.equals(sub.getIndex(), index)) {
                        return sub.getItemClass();
                    }
                }
            }
        }

        return null;
    }
}
