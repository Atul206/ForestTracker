package com.rivigo.sdk.data;

import java.io.Serializable;

/**
 * Created by gauravk on 27/6/16.
 */
public abstract class DataPoint
        implements Serializable, ITimeStamp {
    public long timestamp;

    public DataPoint() {
    }

    public DataPoint(DataPoint paramDataPoint) {
        this.timestamp = paramDataPoint.timestamp;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof DataPoint)) {
            DataPoint localDataPoint = (DataPoint) paramObject;
            return this.timestamp == localDataPoint.timestamp;
        }
        return false;
    }

    public int hashCode() {
        return Long.valueOf(this.timestamp).hashCode();
    }



}
