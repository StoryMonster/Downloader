import surface.*;
import utils.ConfigFileHelper;
import java.awt.Dimension;

public class Downloader {
    public static void main(String[] args) {
        ConfigFileHelper helper = new ConfigFileHelper("../config.xml");
        helper.showEntireTree();
        //helper.updateNode("lastRemoteAddr", "great");
        //helper.updateConfigFile();
        new MainSurface(new Dimension(1024, 600));
        //helper.updateConfigFile("Shabi");
    }
}