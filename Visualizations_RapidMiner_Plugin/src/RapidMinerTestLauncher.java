
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.ZipException;

import com.rapid_i.Launcher;


public class RapidMinerTestLauncher {
	public static void main(String[] args) throws SecurityException, IllegalArgumentException, ZipException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
		System.setProperty(Launcher.PROPERTY_RAPIDMINER_HOME, "D:/Data/Dokumenty/Skola/B102/Y36BP/rapidminer/rapidminer/Vega");
		Launcher.main(args);
	}
}
