package com.rivigo.sdk.database;

import com.rivigo.sdk.data.AllSensorData;

import java.util.List;

/**
 * Created by Atul on 27-03-2017.
 */

public interface CrudSensors {
    public void addSensor(SensorsModel SensorsModel);

    public SensorsModel getSensors(int id);

    public List<SensorsModel> getAllSensors();

    public int getSensorsCount();

    public int updateSensors(SensorsModel SensorsModel);

    public void deleteSensors(SensorsModel SensorsModel);

    public List<AllSensorData> getAllSensorsById(int id);
}
