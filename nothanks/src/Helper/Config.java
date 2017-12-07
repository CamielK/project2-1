package Helper;

public class Config {

    public static String logpath = "";

    public Config () {
        System.out.println("test" + logpath);
        String systemPath = System.getProperty("user.dir");
        if (systemPath.contains("nothanks")) {
            logpath = systemPath + "";
        } else {
            logpath = systemPath + "/nothanks";
        }
    }
}