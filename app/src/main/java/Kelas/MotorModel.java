package Kelas;

/**
 * Created by Glory on 04/09/2018.
 */

public class MotorModel {
    String deviceId;
    String nama;
    String plat;
    Double lat;
    Double lon;
    String mesin;
    String sen;
    String klakson;

    public MotorModel(String deviceId, String nama, String plat, Double lat, Double lon, String mesin, String sen, String klakson) {
        this.deviceId = deviceId;
        this.nama = nama;
        this.plat = plat;
        this.lat = lat;
        this.lon = lon;
        this.mesin = mesin;
        this.sen = sen;
        this.klakson = klakson;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getMesin() {
        return mesin;
    }

    public void setMesin(String mesin) {
        this.mesin = mesin;
    }

    public String getSen() {
        return sen;
    }

    public void setSen(String sen) {
        this.sen = sen;
    }

    public String getKlakson() {
        return klakson;
    }

    public void setKlakson(String klakson) {
        this.klakson = klakson;
    }
}
