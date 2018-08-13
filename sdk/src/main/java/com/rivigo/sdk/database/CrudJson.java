package com.rivigo.sdk.database;

import java.util.List;

/**
 * Created by Atul on 28-03-2017.
 */

public interface CrudJson {
    public void addJson(String jsonModel);

    public JsonModel getJson(int id);

    public List<JsonModel> getAllJson();

    public int getJsonCount();

    public int updateJson(JsonModel jsonModel);

    public void deleteJson(int deleteUptoId);

    public void deleteJsonById(int deleteId);

    public List<JsonModel> getTopJson();
}
