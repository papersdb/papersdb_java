package edu.ualberta.cs.papersdb.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Collaboration implements Serializable {
    WITH_PDF(1),
    WITH_STUDENT(2),
    WITH_EXTERNAL_ML_COLLEAGUE(3),
    WITH_EXTERNAL_NON_ML_COLLEAGUE(4);

    private static final List<Collaboration> VALUES_LIST =
        Collections.unmodifiableList(Arrays.asList(values()));

    private final int id;

    private Collaboration(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static List<Collaboration> valuesList() {
        return VALUES_LIST;
    }

    public static Collaboration fromId(int id) {
        for (Collaboration item : values()) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }
}
