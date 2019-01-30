package utils;

import bean.ViewBean;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by wangzhiguo on 2019/1/29 0029.
 */
public class ReadWebTemplate {

    private static String templatePath = "/templates/";
    /**
     * controller的配置信息
     */
    private List<ViewBean> resultViews  = LoadWebConfig.resultViews;
    private final static String viewSuffix = LoadWebConfig.constants.get("viewSuffix");

    public static String readTemplate(String prefixName) throws Exception{

        if(StringUtils.isNotEmpty(prefixName)){
            InputStream inputStream = ReadWebTemplate.class.getResourceAsStream(templatePath + prefixName + viewSuffix);
            InputStreamReader reader = new InputStreamReader(inputStream); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            StringBuffer stringBuffer = new StringBuffer();
            String line  = br.readLine();
            stringBuffer.append(line);
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if(StringUtils.isNotEmpty(line)){
                    stringBuffer.append(line);
                }
            }
            return stringBuffer.toString();
        }
        return "";
    }

    public static void main(String [] args){
        try {
            String viewStr = readTemplate("index");
            System.out.println("网页内容："+viewStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
