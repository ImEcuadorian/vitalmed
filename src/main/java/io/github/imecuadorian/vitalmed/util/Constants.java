package io.github.imecuadorian.vitalmed.util;

import raven.modal.*;
import raven.modal.option.*;
import raven.modal.toast.option.*;

public class Constants {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public static final String CEDULA_REGEX = "^(?:[0-1]\\d|2[0-4]|30)[0-5]\\d{6}-?\\d$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[@\\-/])[A-Za-z\\d@\\-/]{8,32}$";
    public static final String NAMES_SURNAMES_REGEX = "^([A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)( [A-ZÁÉÍÓÚÑ][a-záéíóúñ]+){0,1}$";
    public static final String PHONE_REGEX = "^09[0-9]{8}$";
    public static final int MAX_DAYS = 5;
    public static final int MAX_TURNS_PER_DAY = 4;
    public static final String ADMIN_EMAIL = "admin@vitalmed.com";
    public static final String ADMIN_PASSWORD = "Admin@2024";

    private Constants() {
        throw new UnsupportedOperationException("Constants is a utility class and cannot be instantiated.");
    }

    public static ToastOption getOption() {
        ToastOption option = Toast.createOption();
        option.setAnimationEnabled(true).setPauseDelayOnHover(true).setAutoClose(true);
        option.getLayoutOption().setLocation(ToastLocation.TOP_TRAILING);
        option.getStyle().setBackgroundType(ToastStyle.BackgroundType.GRADIENT)
                .setShowLabel(true)
                .setIconSeparateLine(true)
                .setShowCloseButton(true)
                .getBorderStyle().setBorderType(ToastBorderStyle.BorderType.TRAILING_LINE);
        return option;
    }

    public static Option getSelectedOption() {
        Option option = ModalDialog.createOption();
        float scale = 0.1f;
        Location h = Location.CENTER;
        Location v = Location.CENTER;
        Option.BackgroundClickType backgroundClickType = raven.modal.option.Option.BackgroundClickType.BLOCK;
        option.setAnimationEnabled(true)
                .setCloseOnPressedEscape(false)
                .setBackgroundClickType(backgroundClickType)
                .setOpacity(0.5f);
        option.getBorderOption()
                .setBorderWidth(1)
                .setShadow(BorderOption.Shadow.EXTRA_LARGE);
        option.getLayoutOption().setLocation(h, v);
        option.getLayoutOption().setAnimateDistance(0, 0)
                .setAnimateScale(scale);
        return option;
    }
}
