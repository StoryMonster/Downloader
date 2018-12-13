import surface.*;
import utils.ConfigHelper;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import context.MainSurfaceContext;

public class Downloader {
    public static void main(String[] args) {
        ConfigHelper helper = new ConfigHelper("../config.xml");
        MainSurfaceContext context = helper.getMainSurfaceContext();
        if (context != null)
        {
            MainSurface mainSurface = new MainSurface(context);
            mainSurface.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    helper.updateMainSurfaceContext(context);
                    helper.updateConfigFile();
                    e.getWindow().dispose();
                }
            });
        }
    }
}