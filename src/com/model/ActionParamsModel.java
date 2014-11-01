package com.model;

public class ActionParamsModel {

    private ActionModel actionModel;
    private String uriKey;
    private String[] params;

    public ActionParamsModel(ActionModel actionModel, String uriKey) {
        super();
        this.actionModel = actionModel;
        this.uriKey = uriKey;
    }

    public ActionModel getActionModel() {
        return actionModel;
    }

    public void setActionModel(ActionModel actionModel) {
        this.actionModel = actionModel;
    }

    public String getUriKey() {
        return uriKey;
    }

    public void setUriKey(String uriKey) {
        this.uriKey = uriKey;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

}
