package io.github.imecuadorian.vitalmed.view.menu;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import raven.modal.*;
import raven.modal.drawer.menu.*;

public class MyMenuValidation extends MenuValidation {

    public static void setUser(User user) {
        MyMenuValidation.user = user;
    }

    public static User user;

    @Override
    public boolean menuValidation(int[] index) {
        return validation(index);
    }

    private static boolean checkMenu(int[] index, int[] indexHide) {
        if (index.length == indexHide.length) {
            for (int i = 0; i < index.length; i++) {
                if (index[i] != indexHide[i]) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean validation(Class<? extends Form> itemClass) {
        int[] index = Drawer.getMenuIndexClass(itemClass);
        if (index == null) {
            return false;
        }
        return validation(index);
    }

    public static boolean validation(int[] index) {
        if (user == null || index == null || index.length == 0) return false;

        int i = index[0];

        switch (user.getRol()) {
            case ADMIN:
                return true;

            case DOCTOR:
                return i == 1 || i == 2 || i == 9;

            case PATIENT:
                return i == 0 || i == 9;

            default:
                return false;
        }
    }
}
