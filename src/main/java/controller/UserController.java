package controller;

import annotation.Action;
import bean.ControllerContext;

/**
 * Created by wangzhiguo on 2019/1/29 0029.
 */
public class UserController {

    public String index(ControllerContext context){

        context.addParams("key", "this is index method!");
        return "index";
    }
    public String login(ControllerContext context){

        context.addParam("key", "this is login method!");
        return "login";
    }

    /**
     * 通过注解实现action
     * @param context
     * @return
     */
    
    @Action("annotationTest")
    public String annotation(ControllerContext context){
        context.addParam("key", "this is login method!");
        return "annotationTest";
    }
}
