package utils;

import bean.ViewBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * Created by wangzhiguo on 2019/1/29 0029.
 * 加载配置文件
 */
public class LoadWebConfig {

    private final static String CONFIG_PATH = "/config/webConfig.xml";
    private final static String CONSTANT_LABEL = "constants";
    private final static String ACTION_LABEL = "action";

    private final static String ACTION_PROP_NAME = "name";
    private final static String ACTION_PROP_METHOD = "method";
    private final static String ACTION_PROP_CLASS = "class";
    private final static String ACTION_PROP_TYPE = "type";
    private final static String ACTION_PROP_PAGE = "page";
    /**
     * 配置文件中基本参数
     */
    public  static Map<String, String> constants = new HashMap<>();

    public static List<ViewBean> resultViews = new ArrayList<>();

    public static void main(String [] args){
        LoadWebConfig loadWebConfig = new LoadWebConfig();
        loadWebConfig.init();
        MapUtils.debugPrint(System.out, 1,loadWebConfig.constants);
        System.out.println(StringUtils.join(resultViews,"###"));
    }
    public void init(){
        // 解析webConfig.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            String userPath = this.getClass().getClassLoader().getResource("").getPath();
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new File( userPath + CONFIG_PATH));
            Element configPackage = document.getRootElement();
            String packageName = configPackage.getName();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = configPackage.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                Element element = (Element) it.next();

                if(CONSTANT_LABEL.equals(element.getName())){
                    //解析常量
                    Iterator itt = element.elementIterator();
                    while (itt.hasNext()) {
                        Element subElement = (Element) itt.next();
                        constants.put(subElement.getName(),  subElement.getStringValue());
//                        System.out.println("节点名：" + subElement.getName() + "--节点值：" + subElement.getStringValue());
                    }
                }else if(ACTION_LABEL.equals(element.getName())){
                    //解析action的配置
                    //todo 判断是否为常量
                    // todo 若不为常量即为配置action的信息
                    // 获取action的属性名以及 属性值
                    List<Attribute> actionAttrs = element.attributes();
                    ViewBean viewMapper = setAttrByAttribute(actionAttrs);
                    //设置包名
                    viewMapper.setPackageName(packageName);
                    resultViews.add(viewMapper);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private ViewBean setAttrByAttribute(List<Attribute> actionAttrs){
        ViewBean viewBean = new ViewBean();
        if(CollectionUtils.isNotEmpty(actionAttrs)){
            //转化为对象
            for (Attribute attr : actionAttrs) {
                String propName = attr.getName();
                String propValue = attr.getValue();
                switch (propName){
                    case ACTION_PROP_NAME:
                        viewBean.setActionName(propValue);
                        break;
                    case ACTION_PROP_METHOD:
                        viewBean.setControllerClassMethod(propValue);
                        break;
                    case ACTION_PROP_CLASS:
                        viewBean.setControllerClass(propValue);
                        break;
                    case ACTION_PROP_TYPE:
                        viewBean.setViewTyp(propValue);
                        break;
                    case ACTION_PROP_PAGE:
                        viewBean.setViewName(propValue);
                        break;
                    default:break;
                }
            }
        }
        return viewBean;
    }


    public  Map<String, String> getConstants() {
        return constants;
    }

    public  void setConstants(Map<String, String> constants) {
        constants = constants;
    }

    public  List<ViewBean> getResultViews() {
        return resultViews;
    }

    public  void setResultViews(List<ViewBean> resultViews) {
        resultViews = resultViews;
    }
}
