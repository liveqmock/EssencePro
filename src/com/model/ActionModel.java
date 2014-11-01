package com.model;

import java.lang.reflect.Method;

import com.action.BaseAction;
import com.checker.FormChecker;

public class ActionModel {
    private String actionPath;// 用于对应处理方法
    private Class<? extends BaseAction> actionCl;
    private Method method;
    private Class<? extends FormChecker> formChecker;

    public ActionModel(String actionPath, Class<? extends BaseAction> actionCl, Method method, Class<? extends FormChecker> formChecker) {
        super();
        this.actionPath = actionPath;
        this.actionCl = actionCl;
        this.method = method;
        this.formChecker = formChecker;
    }

    public String getActionPath() {
        return actionPath;
    }

    public void setActionPath(String actionPath) {
        this.actionPath = actionPath;
    }

    public Class<? extends BaseAction> getActionCl() {
        return actionCl;
    }

    public void setActionCl(Class<? extends BaseAction> actionCl) {
        this.actionCl = actionCl;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<? extends FormChecker> getFormChecker() {
        return formChecker;
    }

    public void setFormChecker(Class<? extends FormChecker> formChecker) {
        this.formChecker = formChecker;
    }
}
