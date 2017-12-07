package Helper;

public class Config {

    public static String logpath = "";

    public Config () {
        String systemPath = System.getProperty("user.dir");
        if (systemPath.contains("nothanks")) {
            logpath = systemPath + "";
        } else {
            logpath = systemPath + "/nothanks";
        }
    }
}