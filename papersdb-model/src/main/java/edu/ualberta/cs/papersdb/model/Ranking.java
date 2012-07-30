package edu.ualberta.cs.papersdb.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Ranking implements Serializable {
    TOP_TIER(1),
    SECOND_TIER(2),
    REFEREED(3),
    UNREFEREED(4),
    UNDEFINED(5),
    OTHER(6);

    private static final List<Ranking> VALUES_LIST =
        Collections.unmodifiableList(Arrays.asList(values()));

    private final int id;

    private Ranking(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static List<Ranking> valuesList() {
        return VALUES_LIST;
    }

    public static Ranking fromId(int id) {
        for (Ranking item : values()) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }

}
