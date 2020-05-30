package com.xu.database;

/**
 * It extends form TableManager
 * It was write for a special table, which only read. They only (or sometime only) use 2 column is ID and field Name
 */
public abstract class TableReadOnlyManager extends TableManager {

    /**
     * getLine a field's id by name
     *
     * @param name name of field
     * @return id of field
     */
    public abstract String getID(String name);

    /**
     * getLine a field's name by id
     *
     * @param id id of field
     * @return name of field
     */
    public abstract String getName(String id);

    /**
     * Check if a field is existed
     *
     * @param name field
     * @return is existed
     */
    public abstract boolean isExist(String name);
}
