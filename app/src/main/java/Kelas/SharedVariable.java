package Kelas;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glory on 04/09/2018.
 */

public class SharedVariable {
    public static String nama = "Not Logged in";
    public static int jmlMotor = 0;
    public static String userID = "";
    public static List<String> list_motor = new ArrayList();
    public static Double latitudeMotor;
    public static Double longitudeMotor;
    public static LatLng latlonMotor = new LatLng(0,0);
    public static int notifChance = 3;
    public static String jarakKeMotor = "";
    public static String check;
}
