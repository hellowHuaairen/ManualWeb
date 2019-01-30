package bean;

/**
 * Created by wangzhiguo on 2019/1/29 0029.
 */
public class ViewBean {

    /**
     * action的包名
     */
    private String packageName;
    /**
     * 控制器class名称
     */
    private String controllerClass;
    /**
     * action名称
     */
    private String actionName;
    /**
     * 控制器对应action的方法名
     */
    private String controllerClassMethod;
    /**
     * 返回视图的类型
     */
    private String viewTyp;
    /**
     * 返回视图的名称
     */
    private String viewName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(String controllerClass) {
        this.controllerClass = controllerClass;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getControllerClassMethod() {
        return controllerClassMethod;
    }

    public void setControllerClassMethod(String controllerClassMethod) {
        this.controllerClassMethod = controllerClassMethod;
    }

    public String getViewTyp() {
        return viewTyp;
    }

    public void setViewTyp(String viewTyp) {
        this.viewTyp = viewTyp;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String toString() {
        return "ViewBean{" +
                "packageName='" + packageName + '\'' +
                ", controllerClass='" + controllerClass + '\'' +
                ", actionName='" + actionName + '\'' +
                ", controllerClassMethod='" + controllerClassMethod + '\'' +
                ", viewTyp='" + viewTyp + '\'' +
                ", viewName='" + viewName + '\'' +
                '}';
    }
}
