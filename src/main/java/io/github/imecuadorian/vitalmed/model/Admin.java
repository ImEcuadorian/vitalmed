package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.vitalmed.util.*;

public class Admin extends User {

    private static Admin instance;

    public Admin() {
        super("0000000000", "Administrador del Sistema", Constants.ADMIN_EMAIL, Constants.ADMIN_PASSWORD, "", "", "", Rol.ADMIN);
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }
}
