package com.action;

import com.annotation.Annotation.ActionName;
import com.annotation.Annotation.MethodName;

@ActionName("")
public class IndexAction {

    @MethodName("login")
    public void login() {
        System.out.println("进来了");

    }

}
