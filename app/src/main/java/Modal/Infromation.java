package Modal;

public class Infromation
{

    private int id;
    private static String OperatorName ,longitude,latitude;

    public Infromation()
    {

    }

    public Infromation(int id,String longitude, String latitude, String operatorName) {
        this.longitude = longitude;
        this.latitude = latitude;
        OperatorName = operatorName;
    }

    public Infromation(String OperatorName,String longitude, String latitude) {
        this.OperatorName=OperatorName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Infromation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public static String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public static String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }
}
