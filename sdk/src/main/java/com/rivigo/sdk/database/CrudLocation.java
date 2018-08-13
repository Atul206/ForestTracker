package com.rivigo.sdk.database;

import java.util.List;

/**
 * Created by Atul on 27-03-2017.
 */

public interface CrudLocation {
    public void addLocation(LocationModel locationModel);

    public LocationModel getLocation(int id);

    public List<LocationModel> getAllLocation();

    public int getLocationCount();

    public int updateLocation(LocationModel locationModel);

    public void deleteLocation(LocationModel locationModel);

    public LocationModel lastInsertLocation();
}
