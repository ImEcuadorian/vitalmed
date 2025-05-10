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


    public static boolean validation(Class<? extends Form> itemClass) {
        int[] index = Drawer.getMenuIndexClass(itemClass);
        if (index == null) {
            return false;
        }
        return validation(index);
    }

    public static boolean validation(int[] index) {
        if (user == null || index == null || index.length == 0) return false;

        int mainIndex = index[0];

        switch (user.getRol()) {
            case ADMIN:
                return true;

            case DOCTOR:
                return switch (mainIndex) {
                    case 1, 3, 4, 6, 11 -> true;  // dashboard, agendar cita, gestionar cita, ver historial, logout
                    default -> false;
                };

            case PATIENT:
                return switch (mainIndex) {
                    case 1, 3, 11 -> true; // dashboard, agendar cita, logout
                    default -> false;
                };

            default:
                return false;
        }
    }


}
