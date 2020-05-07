package NewGreenhouse;

import java.io.Serializable;
import java.sql.Timestamp;

public class Phvalues implements Serializable {
    private int id;
    private int hum;
    private float temp;
    private int lum;
    private float kwhPDay;
    private Timestamp time;
    private String recordType;

    public Phvalues() {
    }

    public Phvalues(int id, int hum, float temp, int lum, float kwhPDay, Timestamp time, String recordType) {
        this.id = id;
        this.hum = hum;
        this.temp = temp;
        this.lum = lum;
        this.kwhPDay = kwhPDay;
        this.time = time;
        this.recordType = recordType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getLum() {
        return lum;
    }

    public void setLum(int lum) {
        this.lum = lum;
    }

    public float getKwhPDay() {
        return kwhPDay;
    }

    public void setKwhPDay(float kwhPDay) {
        this.kwhPDay = kwhPDay;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
}
