package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.vitalmed.util.*;

public class Admin extends User {

    private static Admin instance;

    private Admin() {
        super("0000000000", "Administrador del Sistema", Constants.ADMIN_EMAIL, Constants.ADMIN_PASSWORD, "", "", "");
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }
}
