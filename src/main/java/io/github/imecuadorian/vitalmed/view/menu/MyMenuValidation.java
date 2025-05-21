package io.github.imecuadorian.vitalmed.view.menu;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.view.forms.*;
import io.github.imecuadorian.vitalmed.view.forms.auth.*;
import io.github.imecuadorian.vitalmed.view.forms.doctor.*;
import io.github.imecuadorian.vitalmed.view.forms.patient.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import lombok.*;
import raven.modal.drawer.item.*;
import raven.modal.drawer.menu.*;

import java.util.*;

public class MyMenuValidation extends MenuValidation {

    @Getter
    @Setter
    private static User user;
    @Setter
    private static MenuItem[] menuItems;

    @Override
    public boolean menuValidation(int[] index) {
        Class<?> clazz = getClassByIndex(index);

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


    public static boolean validation(Class<? extends Form> clazz) {
        if (user == null) return false;

        return switch (user.getRol()) {
            case ADMIN -> true;

            case DOCTOR -> (
                    clazz == FormDashboard.class ||
                    clazz == FormAppointmentManagement.class ||
                    clazz == FormMedicalHistory.class ||
                    clazz == FormUpdateData.class ||
                    clazz == FormResetPassword.class ||
                    clazz == FormLogout.class
            );

            case PATIENT -> (
                    clazz == FormDashboard.class ||
                    clazz == FormAppointmentScheduling.class ||
                    clazz == FormUpdateData.class ||
                    clazz == FormResetPassword.class ||
                    clazz == FormLogout.class
            );
        };
    }


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
