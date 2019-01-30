package bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzhiguo on 2019/1/29 0029.
 */
public class ControllerContext {
    private Map<String, Object> params = new HashMap<String, Object>();

    public void addParam(String paramKey,String paramValue){
        params.put(paramKey, paramValue);
    }
    public void addParams(String paramKey,Object paramValue){
        params.put(paramKey, paramValue);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
