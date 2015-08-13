package struts.config.parser;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jeecg.learning.chapter3.annotation.Action;
import org.jeecg.learning.chapter3.annotation.RequestMapping;
import org.jeecg.learning.chapter3.annotation.Result;
import org.jeecg.learning.chapter3.util.StringUtils;

import struts.action.ActionMapping;

public class XmlParser {

    private static String config = "/WEB-INF/struts-config.xml";

    private static Map<String, String> formBeanMap = new HashMap<String, String>();

    private static Map<String, ActionMapping> actionMap = new HashMap<String, ActionMapping>();

    private static Map<String, String> controllerMap = new HashMap<String, String>();

    private static final String INCLUDE_FILTER_ELEMENT = "include-filter";

    /** 
     * 根据xml文件路径取得document对象 
     * @param xmlPath 
     * @return 
     * @throws DocumentException 
     */
    public static Document getDocument(String xmlPath) {

        if(xmlPath==null || xmlPath.equals("")) {
            return null;
        }

        File file = new File(xmlPath);
        if(file.exists() == false) {
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(xmlPath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    /** 
     *  
     * @方法功能描述:获取指定节点指定属性的值 
     * @方法名:attrValue 
     * @param e 
     * @param attrName 
     * @返回类型：String 
     * @时间：2011-4-14下午02:36:48 
     */
    public static String attrValue(Element e,String attrName) {
        attrName = attrName.trim();
        if(e == null) {
            return null;
        }
        if (attrName== null || attrName.equals("")) {
            return null;
        }
        return e.attributeValue(attrName);
    }

    /** 
     *  
     * @方法功能描述：得到指定节点的属性的迭代器 
     * @方法名:getAttrIterator 
     * @param e 
     * @返回类型：Iterator<Attribute> 
     * @时间：2011-4-14下午01:42:38 
     */
    @SuppressWarnings("unchecked")  
    public static Iterator<Attribute> getAttrIterator(Element e) {
        if(e==null) {
            return null;
        }
        Iterator<Attribute> attrIterator = e.attributeIterator();
        return attrIterator;
    }

    /** 
     *  
     * @方法功能描述：遍历指定节点的所有属性 
     * @方法名:getAttributeList 
     * @param e 
     * @return 节点属性的list集合 
     * @返回类型：List<Attribute> 
     * @时间：2011-4-14下午01:41:38 
     */
    public static List<Attribute> getAttributeList(Element e) {
        if(e==null) {
            return null;
        }
        List<Attribute> attributeList = new ArrayList<Attribute>();
        Iterator<Attribute> atrIterator = getAttrIterator(e);
        if(atrIterator == null) {
            return null;
        }
        while (atrIterator.hasNext()) {
            Attribute attribute = atrIterator.next();
            attributeList.add(attribute);
        }
        return attributeList;
    }

    /** 
     *  
     * @方法功能描述：得到指定节点的所有属性及属性值 
     * @方法名:getNodeAttrMap 
     * @return 属性集合 
     * @返回类型：Map<String,String> 
     * @时间：2011-4-15上午10:00:26 
     */
    public static Map<String,String> getNodeAttrMap(Element e) {
        Map<String,String> attrMap = new HashMap<String, String>();
        if (e == null) {
            return null;
        }
        List<Attribute> attributes = getAttributeList(e);
        if (attributes == null) {
            return null;
        }
        for (Attribute attribute:attributes) {
            String attrValueString = attrValue(e, attribute.getName());
            attrMap.put(attribute.getName(), attrValueString);
        }
        return attrMap;
    }

    /** 
     *  
     * @方法功能描述：得到根节点 
     * @方法名:getRootEleme 
     * @param DOC对象 
     * @返回类型：Element 
     * @时间：2011-4-8下午12:54:02 
     */
    public static Element getRootNode(Document document) {
        if(document==null) {
            return null; 
        }
        Element root = document.getRootElement();
        return root;
    }

    /** 
     *  
     * @方法功能描述:得到指定元素的迭代器 
     * @方法名:getIterator 
     * @param parent 
     * @返回类型：Iterator<Element> 
     * @时间：2011-4-14上午11:29:18 
     */
    @SuppressWarnings("unchecked")
    public static Iterator<Element> getIterator(Element parent) {
        if(parent == null)
            return null;
        Iterator<Element> iterator = parent.elementIterator();
        return iterator;
    }

    /** 
     *  
     * @方法功能描述：获取某节点下子节点列表
     * @方法名:getChildList 
     * @param node 
     * @return @参数描述 : 
     * @返回类型：List<Element> 
     * @时间：2011-4-14下午12:21:52 
     */
    public static  List<Element> getChildList(Element node) {
        if (node==null) {
            return null;
        }
        Iterator<Element> itr = getIterator(node);
        if(itr==null) {
            return null;
        }
        List<Element> childList = new ArrayList<Element>();
        while(itr.hasNext()) {
            Element kidElement = itr.next();
            if(kidElement!=null){
                childList.add(kidElement);
            }
        }
        return childList;
    }

    public static Map<String, ActionMapping> parse(String xmlPath) {
        if (xmlPath == null || xmlPath.equals("")) {
            xmlPath = config;
        }
        Document document = null;
        document = getDocument(xmlPath);
        parseFormBean(document);
        parseActionMapping(document);
        return actionMap;
    }

    public static Map<String, ActionMapping> parseStrutsConfig(String xmlPath) {

        if (xmlPath == null || xmlPath.length() == 0) {
            xmlPath = config;
        }
        Document document = null;
        document = getDocument(xmlPath);
        actionMap = scanBasePackage(document);
        return actionMap;
    }
 
    /**
     * 扫描包文件的类
     * 
     * @param root
     * @return
     */
    private static Map<String, ActionMapping> scanBasePackage(Document document) {
        // 获取所有包
        String[] packages = getPackages(document);
        // 获取包里所有类
        List<Class<?>> classes = getPackageClass(packages);
        List<Class<?>> actionLst = parseTypeFilters(document, classes);
        // 解析类的注解
        return doScan(actionLst);
    }

    private static Map<String, ActionMapping> doScan(List<Class<?>> list) {

        Map<String, String> forwardMap = new HashMap<String, String>();
        for (Class<?> clazz : list) {

            String cont = clazz.getAnnotation(Action.class).value();

            Method[] methods = clazz.getMethods();

            for (Method m : methods) {

                RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    String[] value = requestMapping.value();
                    Result[] resultList = requestMapping.results();

                    Class<?> para = m.getParameterTypes()[0];
                    if (para != null) {
                        ActionMapping actionMapping = new ActionMapping();
                        actionMapping.setName(cont);
                        actionMapping.setFormType(para.getName());
                        actionMapping.setPath(value[0]);
                        String actionType = controllerMap.get(cont);
                        if(actionType != null){
                            actionMapping.setType(actionType);
                        }
                        for(Result result : resultList){
                            String name = result.name();
                            String location = result.location();
                            forwardMap.put(name, location);
                        }
                        actionMapping.setForwardProperties(forwardMap);
                        actionMap.put(value[0], actionMapping);
                    }

                }

            }
        }
        return actionMap;
    }

    @SuppressWarnings("unchecked")
    private static List<Class<?>> parseTypeFilters(Document document, List<Class<?>> list) {
        List<Class<?>> actionClassLst = new ArrayList<Class<?>>();
        Element root = getRootNode(document);
        Element actions = root.element("component-scan");
        List<Element> filterList = getChildList(actions);
        for(Element filter : filterList) {
            String localName = filter.getName();
            if (INCLUDE_FILTER_ELEMENT.equals(localName)) {
                String type = attrValue(filter, "type");
                String expression = attrValue(filter, "expression");
                for (Class<?> clazz : list) {
                    if ("annotation".equals(type)) {
                        Class<Action> a;
                        try {
                            a = (Class<Action>) Class.forName(expression);
                            Action action = (Action) clazz.getAnnotation(a);
                            if (action != null) {
                                actionClassLst.add(clazz);
                                controllerMap.put(action.value(), clazz.getName());
                            }

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new IllegalArgumentException("Unsupported filter type: " + type);
                    }
                }
            }

        }
        return actionClassLst;

    }

    private static String[] getPackages(Document document) {
        Element root = getRootNode(document);
        Element e = root.element("component-scan");
        if (e == null) {
            return null;
        }
        String[] packages = StringUtils.tokenizeToStringArray(e
                .attributeValue("base-package"), ",");
        return packages;
    }

    private static List<Class<?>> getPackageClass(String[] packages) {
        List<Class<?>> list = new ArrayList<Class<?>>();
        String classpath = Thread.currentThread().getContextClassLoader()
                .getResource("").getPath();
        for (String pack : packages) {
            File file = new File(classpath + pack.replace(".", "/"));
            File[] files = file.listFiles();
            for (File f : files) {
                if (!f.isDirectory()) {
                    try {
                        String fname = f.getName().substring(0, f.getName()
                                .lastIndexOf("."));
                        list.add(Class.forName(pack + "." + fname));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    private static void parseActionMapping(Document document){
        Element root = getRootNode(document);
        Element actions = root.element("action-mappings");
        List<Element> actionList = getChildList(actions);
        for(Element action : actionList){
            String name = attrValue(action, "name");
            String type = attrValue(action, "type");
            String path = attrValue(action, "path");
            ActionMapping actionMapping = new ActionMapping();
            actionMapping.setName(name);
            actionMapping.setType(type);
            actionMapping.setPath(path);
            String formType = formBeanMap.get(name);
            if(formType != null){
                actionMapping.setFormType(formType);
            }

            List<Element> forwardList = getChildList(action);
            Map<String, String> forwardMap = new HashMap<String, String>();
            for(Element forward : forwardList){
                String forwardName = attrValue(forward, "name");
                String forwardValue = attrValue(forward, "value");
                forwardMap.put(forwardName, forwardValue);
            }
            actionMapping.setForwardProperties(forwardMap);
            actionMap.put(path, actionMapping);
        }
    }

    /** 
     *  
     * @方法功能描述：解析form-bean
     * @方法名:parseFormBean 
     * @param document 
     */
    private static void parseFormBean(Document document){
        Element root = getRootNode(document);
        Element form = root.element("form-beans");
        List<Element> forms = getChildList(form);
        for(Element f : forms){
            String name = attrValue(f, "name");
            String clazz = attrValue(f, "type");
            formBeanMap.put(name, clazz);
        }
    }
}
