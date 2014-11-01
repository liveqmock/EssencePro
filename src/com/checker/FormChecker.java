package com.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.model.CheckerModel;
import com.util.ClassUtil;
import com.util.StringUtil;

public abstract class FormChecker {
    private List<CheckerModel> checkList = new ArrayList<CheckerModel>();
    protected HttpServletRequest request;

    public FormChecker() {
    }

    public void init(HttpServletRequest request) {
        this.request = request;
    }

    public abstract void setCheck();

    // 是否为空
    protected void addCheckEmpty(String key, String showMessage) {
        CheckerModel km = new CheckerModel(key, "notnull", showMessage);
        checkList.add(km);
    }

    // 检测最大长度
    protected void addCheckMaxLen(String key, String showMesage, int maxlen) {
        CheckerModel km = new CheckerModel(key, "maxlen", showMesage, maxlen);
        checkList.add(km);
    }

    // 检测长度范围
    protected void addCheckRangeLen(String key, String showMesage, int minlen, int maxlen) {
        CheckerModel km = new CheckerModel(key, "rangelen", showMesage, minlen, maxlen);
        checkList.add(km);
    }

    // 检测是否是整数
    protected void addCheckIsNum(String key, String showMesage) {
        CheckerModel km = new CheckerModel(key, "isnum", showMesage);
        checkList.add(km);
    }

    // 检测是否是有效的电子邮箱
    protected void addCheckIsEamil(String key, String showMesage) {
        CheckerModel km = new CheckerModel(key, "email", showMesage);
        checkList.add(km);
    }

    public String getErrorStr() throws ServletException, IOException {
        for (CheckerModel checkModel : checkList) {
            String key = checkModel.getKey();
            String value = checkModel.getValue();
            String showMessage = checkModel.getShowMessage();
            String param = request.getParameter(key);
            if (param == null || param.isEmpty()) {
                if (value.equals("notnull")) {
                    return showMessage + "不能为空";
                }
            } else {
                if (value.equals("maxlen")) {
                    int maxlen = checkModel.getMaxlen();
                    int intmaxlen = ClassUtil.obj2int(StringUtil.length(param));
                    if (intmaxlen > maxlen) {
                        return showMessage + "超出最大长度" + maxlen;
                    }
                } else if (value.equals("rangelen")) {
                    int minlen = checkModel.getMinlen();
                    int maxlen = checkModel.getMaxlen();
                    int intmaxlen = ClassUtil.obj2int(StringUtil.length(param));
                    if (intmaxlen > maxlen || intmaxlen < minlen) {
                        return showMessage + "最少" + minlen + " 最多 " + maxlen;
                    }
                } else if (value.equals("isnum")) {
                    Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
                    if (!pattern.matcher(param).matches()) {
                        return showMessage + "不是整数";
                    }
                } else if (value.equals("email")) {
                    // 邮箱的正则表达式
                    Pattern pattern = Pattern.compile("[a-zA-Z][\\w_]+@\\w+(\\.\\w+)+");
                    if (!pattern.matcher(param).matches()) {
                        return showMessage + "不是有效的电子邮箱";
                    }
                }
            }
        }
        return null;
    }
}
