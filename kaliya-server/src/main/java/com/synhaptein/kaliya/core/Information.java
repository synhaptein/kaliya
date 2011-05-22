package com.synhaptein.kaliya.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the configuration container/reader.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class Information {

    private static Map<String, String> m_parameters = new LinkedHashMap<String, String>();
    private static String m_crossDomain = null;

    public static void initParameter(String p_confPath) {
        m_parameters.put("frameworkVersion", "Kaliya 1.0-SNAPSHOT");
        readXMLConf(p_confPath);
    }

    /**
     * Read the configuration file and fill the parameter map
     */
    private static void readXMLConf(String p_confPath) {
        try {
            File file = new File(p_confPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("parameter");
            System.out.println("Loading Kaliya server configuration...");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);
                String key;

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    key = getNodeValue((Element)fstNode, "key");
                    if(!m_parameters.containsKey(key)){
                        m_parameters.put(key, getNodeValue((Element)fstNode, "value"));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get the value of a specific child
     * @param fstNode node
     * @param p_name name of the child
     * @return The node value
     */
    private static String getNodeValue(Element fstNode, String p_name) {
        Element key = (Element) fstNode;
        NodeList keyLstValues = key.getElementsByTagName(p_name);
        Element fstNmElmnt = (Element) keyLstValues.item(0);
        NodeList fstNm = fstNmElmnt.getChildNodes();
        return (String)((Node) fstNm.item(0)).getNodeValue();
    }
    
    /**
     * Return a copy of the parameter map
     * @return parameter map
     */
    public static Map<String, String> getParameterMap() {
        return new LinkedHashMap<String, String>(m_parameters);
    }
 
    /**
     * Return the value of a parameter if it exists else it return null
     * @param p_parameter The parameter
     * @return The value
     */
    public static String getParameter(String p_parameter) {
        return m_parameters.get(p_parameter);
    }

    /**
     * Return the frameworkVersion
     * @return the frameworkVersion
     */
    public static String frameworkVersion() {
        return getParameter("frameworkVersion");
    }

    /**
     * Define if the application is running with debug output
     * @return the status of the debug option
     */
    public static boolean isDebug() {
        return Boolean.valueOf(getParameter("isDebug"));
    }

    /**
     * Return the absolute path to the crossDomain xml file
     * @return the path
     */
    public static String pathToCrossDomain() {
        return getParameter("pathToCrossDomain");
    }

    public static void setCrossDomain(String p_crossDomain) {
        m_crossDomain = p_crossDomain;
    }

    public static String getCrossDomain() {
        return m_crossDomain;
    }
}
