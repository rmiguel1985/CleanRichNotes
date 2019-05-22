package com.adictosalainformatica.cleanrichnotes.features.list.data.database;

/**
 * Database naming schema.
 */
public final class DataNotesName {

    public static final String TABLE_NAME = "note";

    /**
     * Column name for unicode and used as primary.
     */
    public static final String COL_ID = "id";

    /**
     * Column name for Note description.
     */
    public static final String COL_TEXT = "text";

    /**
     * Column name for emoji character.
     */
    public static final String COL_TITLE = "title";

    public static final String COL_IS_PINNED = "isPinned";
}
