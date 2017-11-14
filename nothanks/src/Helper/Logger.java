package Helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private static Logger logger;

    private static String dataPath = null;

    private Logger() {
        dataPath = System.getProperty("user.dir") + "\\Data\\logs.txt";
        System.out.println("Logging to file: " + dataPath);
    }

    public static void write(String message) {
        if (dataPath != null) {
            try {
                FileWriter fw = new FileWriter(dataPath, true);
                BufferedWriter writer = new BufferedWriter(fw);
                writer.write(message);
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger getInstance() {
        if (logger == null) {
            return (logger = new Logger());
        } else {
            return logger;
        }
    }
}
