package com.thesaan.gameengine.android.handler;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.thesaan.beyonduniverse.R;
import com.thesaan.beyonduniverse.gamecontent.Race;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by mknoe on 02.05.2015.
 */
public class XmlHandler {

    Context context;
    XmlResourceParser rParser;


    //the xml tag attribute constants
    public static final int ATTR_MAX_AMOUNT = 6;
    public static final int ATTR_DEFAULT_PRICE = 0;
    public static final int ATTR_RACE = 1;
    public static final int ATTR_ITEMTYPE = 1;
    public static final int ATTR_ARRAYINDEX = 3;

    public final static String FILENAME_GAMECONTENT_XML = "gamecontent.xml";


    public XmlHandler(String xmlAssetFileName, Context context) throws XmlPullParserException {

        FilesHandler fh = new FilesHandler();
        this.context = context;

/*

        factory = XmlPullParserFactory.newInstance();
        rParser = factory.newPullParser();
*/


        try {
            rParser = context.getResources().getXml(R.xml.gamecontent);
        } catch (Exception io) {
            System.err.println(io);
        }

    }//constructors end

    private void getFoods(Race race) {

    }

    public float getFloatAttribute(String itemName, int attributeIndex) throws XmlPullParserException {
        int start = XmlPullParser.START_TAG;
        int end = XmlPullParser.END_TAG;

        if (rParser != null) {
            int event = rParser.getEventType();

            while (event != rParser.END_DOCUMENT) {
//                System.out.println("Show name (" + rParser.getName() + ")->\n");

                if (event == start && rParser.getName().equalsIgnoreCase(itemName)) {
                    try {
                        return Float.parseFloat(rParser.getAttributeValue(attributeIndex));
                    } catch (Exception e) {
                        System.err.println("Attr read ex\n" + e);
                    }
                } else {
                }
                try {
                    event = rParser.next();
                } catch (IOException io) {
                    System.err.println("IO: " + io);
                }
            }
        } else {
            System.err.println("ResourceParser is null");
        }
        return -1;
    }

    public int getIntAttribute(String itemName, int attributeIndex) throws XmlPullParserException {
        int start = XmlPullParser.START_TAG;
        int end = XmlPullParser.END_TAG;

        if (rParser != null) {
            int event = rParser.getEventType();

            while (event != rParser.END_DOCUMENT) {
//                System.out.println("Show name (" + rParser.getName() + ")->\n");

                if (event == start && rParser.getName().equalsIgnoreCase(itemName)) {
                    try {
                        return Integer.parseInt(rParser.getAttributeValue(attributeIndex));
                    } catch (Exception e) {
                        System.err.println("Attr read ex\n" + e);
                    }
                } else {
                }
                try {
                    event = rParser.next();
                } catch (IOException io) {
                    System.err.println("IO: " + io);
                }
            }
        } else {
            System.err.println("ResourceParser is null");
        }
        return -1;
    }

    public double getDoubleAttribute(String itemName, int attributeIndex) throws XmlPullParserException {
        int start = XmlPullParser.START_TAG;
        int end = XmlPullParser.END_TAG;

        if (rParser != null) {
            int event = rParser.getEventType();

            while (event != rParser.END_DOCUMENT) {
//                System.out.println("Show name (" + rParser.getName() + ")->\n");

                if (event == start && rParser.getName().equalsIgnoreCase(itemName)) {
                    try {
                        return Double.parseDouble(rParser.getAttributeValue(attributeIndex));
                    } catch (Exception e) {
                        System.err.println("Attr read ex\n" + e);
                    }
                } else {
                }
                try {
                    event = rParser.next();
                } catch (IOException io) {
                    System.err.println("IO: " + io);
                }
            }
        } else {
            System.err.println("ResourceParser is null");
        }
        return -1;
    }

    public String getStringAttribute(String itemName, int attributeIndex) throws XmlPullParserException {
        int start = XmlPullParser.START_TAG;
        int end = XmlPullParser.END_TAG;

        if (rParser != null) {
            int event = rParser.getEventType();

            while (event != rParser.END_DOCUMENT) {
//                System.out.println("Show name (" + rParser.getName() + ")->\n");

                if (event == start && rParser.getName().equalsIgnoreCase(itemName)) {
                    try {
                        return rParser.getAttributeValue(attributeIndex).toString();
                    } catch (Exception e) {
                        System.err.println("Attr read ex\n" + e);
                    }
                } else {
                }
                try {
                    event = rParser.next();
                } catch (IOException io) {
                    System.err.println("IO: " + io);
                }
            }
        } else {
            System.err.println("ResourceParser is null");
        }
        return null;
    }

    public void write(String category, String tag, String[] attributes, String[] attributesValues) throws SAXException, IOException, ParserConfigurationException, TransformerException {


        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("server.xml");
        Element root = document.getDocumentElement();

        for (int k = 0; k < root.getChildNodes().getLength(); k++) {
            Element rootNode = (Element) root.getChildNodes().item(k);

            if (rootNode.getTagName().equalsIgnoreCase(category)) {
                Element newItem = document.createElement(tag);
                for (int i = 0; i < attributes.length; i++) {

                    newItem.setAttribute(attributes[i], attributesValues[i]);
                }

                rootNode.appendChild(newItem);
            }

        }

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult("server.xml");
        transformer.transform(source, result);
    }
}
