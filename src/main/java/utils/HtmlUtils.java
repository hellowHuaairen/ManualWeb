package utils;

import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzhiguo on 2019/1/29 0029.
 */
public class HtmlUtils {

    private static final String URL_PROBLEM_STR = "?";
    private static final String URL_PARAM_SEPARATED_STR = "&";
    private static final String URL_PARAM_INNER_SEPARATED_STR = "=";

    /**
     * 解析Get请求的参数
     * @param url
     * @return
     */
    public static Map<String, Object> getUrlParams(String url){
        Map<String, Object> stringObjectMap = new HashMap<>();
        //?name=yjh&param1=wangxe
        if(StringUtils.isNotEmpty(url)){
            String strUrl = StringUtils.substring(url, url.indexOf(URL_PROBLEM_STR) + 1);
            int prombleCharIndex = url.indexOf(URL_PROBLEM_STR);
            //判断url是否含有"？"
            if(-1 < prombleCharIndex){
                String [] params = strUrl.split(URL_PARAM_SEPARATED_STR);
                if(null != params){
                    for(String param: params){
                        if(StringUtils.isNotEmpty(param)){
                            String [] paramsArray = param.split(URL_PARAM_INNER_SEPARATED_STR);
                            stringObjectMap.put(paramsArray[0], paramsArray[1]);
                        }
                    }
                }
            }
        }
        return  stringObjectMap;
    }

    /**
     * 返回控制器的action名称
     * @param url
     * @return
     */
    public static String getControllerMethod(String url){
        String actionUrl = "";
        if(StringUtils.isNotEmpty(url)){
            actionUrl = StringUtils.substring(url, 1, url.indexOf(URL_PROBLEM_STR));
        }
        return actionUrl;
    }

    /**
     * todo 解析POST请求的参数？？？
     */
}
