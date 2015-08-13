package struts.action;

import java.util.HashMap;
import java.util.Map;

public class ActionMapping {

    private String name = null;

    private String type = null;

    private String path = null;

    private String formType = null;

    private Map<String, String> forwardProperties = new HashMap<String, String>();

    private Object[] params = null;

    public ActionMapping() {

    }

    public ActionMapping(String name, String type, String path,
            String formType, Map<String, String> forwardProperties, Object[] params) {
        super();
        this.name = name;
        this.type = type;
        this.path = path;
        this.formType = formType;
        this.forwardProperties = forwardProperties;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public Map<String, String> getForwardProperties() {
        return forwardProperties;
    }

    public void setForwardProperties(Map<String, String> forwardProperties) {
        this.forwardProperties = forwardProperties;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("ActionMapping[");

        sb.append("name=");
        sb.append(this.name);
        sb.append(",type=");
        sb.append(this.type);
        sb.append(",path=");
        sb.append(this.path);
        sb.append(",formType=");
        sb.append(this.formType);
        sb.append(",forwardProperties=");
        sb.append(this.forwardProperties);
        sb.append(",params=");
        sb.append(this.params);
        sb.append("]");

        return (sb.toString());
    }
}
