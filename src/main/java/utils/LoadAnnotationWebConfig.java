package utils;

import annotation.Action;
import bean.ControllerContext;
import bean.ViewBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by wangzhiguo on 2019/1/29 0029.
 * 加载配置文件
 */
public class LoadAnnotationWebConfig {

    private final static String JAVA_FILE_TAIL = ".class";
    private final static String JAVA_FILE_STORAGE = "classes";
    private final static String JAVA_FILE_PATH_SEPARATE = "\\";
    private final static String JAVA_CLASS_PATH_SEPARATE = ".";
    /**
     * 配置文件中基本参数
     */
    public  static Map<String, String> constants = new HashMap<>();

    public static List<ViewBean> resultViews = new ArrayList<>();

    private static List<File> filelist = new ArrayList<>();

    public static void main(String [] args){
        LoadAnnotationWebConfig loadWebConfig = new LoadAnnotationWebConfig();
        loadWebConfig.init();
        MapUtils.debugPrint(System.out, 1,loadWebConfig.constants);
        System.out.println(StringUtils.join(resultViews,"###"));
    }

    /**
     * 递归调用获取路径下的所有文件
     * @param sourcePath
     */
    public static List<File> getFileList(String sourcePath) {

        File dir = new File(sourcePath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                } else if (fileName.endsWith(JAVA_FILE_TAIL)) { // 判断文件名是否以.java结尾
                    String strFileName = files[i].getAbsolutePath();
                    System.out.println("---" + strFileName);
                    filelist.add(files[i]);
                } else {
                    continue;
                }
            }

        }
        return filelist;
    }

    public void init(){
        ControllerContext context = new ControllerContext();
        //获取源码路径
        String userPath = this.getClass().getClassLoader().getResource("").getPath();
        //1.解析路径下的所有class,是否包含Action的注解
        if(StringUtils.isNotEmpty(userPath)){
            List<File> files = getFileList(userPath);
            //递归调用，后去文件夹下的所有文件
            for(File file: files){
                //过滤文件，末尾为*.java，并且不是文件夹
                if(null != file){
                    System.out.println("file path: " + file.getPath());
                    System.out.println("file name: " + file.getName());
                    String filePath = file.getAbsolutePath();
                    try {
                        String [] classNameAndPackage = packageName(filePath);
                        String packageName = classNameAndPackage[0];
                        String className = classNameAndPackage[1];
                        if(null != classNameAndPackage){
                            //读取文件第一行包名，+文件名 = 完整class的路径
                            Class clazz = Class.forName(packageName + JAVA_CLASS_PATH_SEPARATE + className);
                            Method[] methods = clazz.getMethods();
                            for(Method method: methods){
                                if(method.isAnnotationPresent(Action.class)){
                                    //2.若有Action注解，解析内容，并组装ViewBean对象
                                    ViewBean viewBean = new ViewBean();
                                    viewBean.setPackageName(packageName);
                                    Action action = method.getAnnotation(Action.class);
                                    viewBean.setActionName(action.value());
                                    viewBean.setControllerClass(packageName + JAVA_CLASS_PATH_SEPARATE + className);
                                    viewBean.setControllerClassMethod(method.getName());
                                    Object controller = clazz.newInstance();
                                    String returnView = (String)method.invoke(controller,context);
                                    viewBean.setViewName(returnView);//annotationTest
                                    //3.将组装好的ViewBean对象添加到resultViews
                                    resultViews.add(viewBean);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * 获取class的包名及类名
     * @param fullFileName
     * @return
     */
    public String[] packageName(String fullFileName) throws Exception{
        String [] classNameAndPackage = new String[2];
        if(StringUtils.isNotEmpty(fullFileName)){
            //例如这样的文件路径：D:\devEnv\technologyStudy\remoteGitProject\ManualWeb\target\classes\annotation\Action.class
            int classPackageIndex = fullFileName.indexOf(JAVA_FILE_STORAGE);
            String fullClassName = StringUtils.substring(fullFileName, classPackageIndex + JAVA_FILE_STORAGE.length() + 1);
            // 处理完路径是这样的：annotation\Action.class
            if(StringUtils.isNotEmpty(fullClassName)){
                int lastSepateIndex = fullClassName.lastIndexOf(JAVA_FILE_PATH_SEPARATE);
                classNameAndPackage[1] = fullClassName.substring(lastSepateIndex + 1, fullClassName.length() - JAVA_FILE_TAIL.length());
                classNameAndPackage[0] = fullClassName.substring(0, lastSepateIndex).replace(JAVA_FILE_PATH_SEPARATE, JAVA_CLASS_PATH_SEPARATE);
            }
        }
        return classNameAndPackage;
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
