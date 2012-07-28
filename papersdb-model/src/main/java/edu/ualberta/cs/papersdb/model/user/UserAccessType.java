package edu.ualberta.cs.papersdb.model.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum UserAccessType implements Serializable {
    UNVERIFIED_USER(0),
    EDITOR(1),
    ADMINISTRATOR(2);

    private static final List<UserAccessType> VALUES_LIST =
        Collections.unmodifiableList(Arrays.asList(values()));

    private final int id;

    private UserAccessType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static List<UserAccessType> valuesList() {
        return VALUES_LIST;
    }

    public static UserAccessType fromId(int id) {
        for (UserAccessType item : values()) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }
}
