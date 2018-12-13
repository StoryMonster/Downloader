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
import context.MainSurfaceContext;

public class ConfigHelper{
    String fileName;
    Element root = null;
    Document doc = null;
    public ConfigHelper(String filename)
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

    private Node findNodeByAttribute(NodeList lst, String attrName, String value)
    {
        int length = lst.getLength();
        for (int i = 0; i < length; ++i)
        {
            Node node = lst.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.hasAttributes())
            {
                NamedNodeMap attrs = node.getAttributes();
                Node attr = attrs.getNamedItem(attrName);
                if (attr != null && attr.getTextContent().compareTo(value) == 0)
                {
                    return node;
                }
            }
        }
        return null;
    }

    public MainSurfaceContext getMainSurfaceContext()
    {
        NodeList contextNodes = root.getElementsByTagName("context");
        Node contextNode = findNodeByAttribute(contextNodes, "owner", "MainSurface");
        if (contextNode == null) {
            System.out.println("Cannot find MainSurface context");
            return null;
        }
        MainSurfaceContext context = new MainSurfaceContext();
        NodeList contextMembers = contextNode.getChildNodes();
        for (int i = 0; i < contextMembers.getLength(); ++i)
        {
             Node member = contextMembers.item(i);
             if (member.getNodeType() == Node.ELEMENT_NODE)
             {
                 String text = member.getTextContent();
                 switch(member.getNodeName())
                 {
                     case "localSaveDir": context.localSaveDir = text; break;
                     case "remoteFilePath": context.remoteFilePath = text; break;
                     case "height": context.height = Integer.parseInt(text); break;
                     case "width": context.width = Integer.parseInt(text); break;
                 }
             }
        }
        return context;
    }

    public void updateMainSurfaceContext(MainSurfaceContext context)
    {
        NodeList contextNodes = root.getElementsByTagName("context");
        Node contextNode = findNodeByAttribute(contextNodes, "owner", "MainSurface");
        if (contextNode == null) {
            System.out.println("Cannot find MainSurface context");
            return ;
        }
        NodeList contextMembers = contextNode.getChildNodes();
        for (int i = 0; i < contextMembers.getLength(); ++i)
        {
             Node member = contextMembers.item(i);
             if (member.getNodeType() == Node.ELEMENT_NODE)
             {
                 String text = member.getTextContent();
                 switch(member.getNodeName())
                 {
                     case "localSaveDir": member.setTextContent(context.localSaveDir); break;
                     case "remoteFilePath": member.setTextContent(context.remoteFilePath); break;
                     case "height": member.setTextContent(String.format("%d", context.height)); break;
                     case "width": member.setTextContent(String.format("%d", context.width)); break;
                 }
             }
        }
    }
}