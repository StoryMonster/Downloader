package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.*;

public class ConfigFileHelper{
    String fileName;
    Element root = null;
    Document doc = null;
    public ConfigFileHelper(String filename)
    {
        this.fileName = filename;
        try {
            InputStream f = new FileInputStream(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(f);
            doc.getDocumentElement().normalize();
            root = doc.getDocumentElement();
            f.close();
        } catch (Exception e)
        {
            System.out.println("Cannot open file");
        }
        
    }

    public void updateNode(String nodeName, String value)
    {
        NodeList childs = root.getChildNodes();
        for (int index = 0; index < childs.getLength(); ++index)
        {
            Node node = childs.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                String name = node.getNodeName();
                if (name == nodeName)
                {
                    System.out.println("Change " + nodeName + ": " + node.getTextContent() + "->" + value);
                    node.setTextContent(value);
                }
            }
        }
    }

    public void updateConfigFile()
    {
        try {
            OutputStream out = new FileOutputStream(fileName);
            TransformerFactory trFactory = TransformerFactory.newInstance();
            Transformer transformer = trFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(out);
            transformer.transform(domSource, streamResult);
            out.close();
        } catch (Exception e)
        {
            System.out.println("Cannot write to " + fileName);
        }
    }

    public void showEntireTree()
    {
        showNodes(root);
    }

    public void showNodes(Node rt)
    {
        NodeList childs = rt.getChildNodes();
        for (int index = 0; index < childs.getLength(); ++index)
        {
            Node node = childs.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                if (node.hasAttributes())
                {
                    String str = node.getNodeName();
                    NamedNodeMap attrs = node.getAttributes();
                    for (int i = 0; i < attrs.getLength(); ++i)
                    {
                        Node nd = attrs.item(i);
                        str += "  " + nd.getNodeName() + ":" + nd.getTextContent();
                    }
                    System.out.println(str);
                    showNodes(node);
                }
                else
                {
                    System.out.println("  " + node.getNodeName() + ":" + node.getTextContent());
                }
            }
        }
    }

    public Context getMainSurfaceContext()
    {
        NodeList contexts = root.getElementsByTagName("context");
        int contextsAmount = contexts.getLength();
        for (int i = 0; i < contextsAmount; ++i)
        {
            
        }
    }
}