package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.checker.FormChecker;

public class Annotation {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ColumnName {
        public String value() default "";
    }

    /**
     * 默认表单需要进行过滤html标签，用于指定无需过滤字段
     * 
     * @author 玄承勇
     * 
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NoFilterHtmlTag {
        public boolean value() default true;
    }

    /**
     * 用于开启事务
     * 
     * @author 玄承勇
     * 
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Transaction {
        public String value() default "";
    }

    /**
     * 用于MVC界面映射，类
     * 
     * @author 玄承勇
     * 
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ActionName {
        public String value() default "";
    }

    /**
     * 
     * @author 用于MVC界面映射，方法
     * 
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MethodName {
        public String value() default "";
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Checker {
        public Class<? extends FormChecker> value();
    }

    /**
     * 保存或修改时整个对象时该属性对应的图片需要索引
     * 
     * @author 玄承勇
     * 
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IndexImg {
        public boolean value() default true;

        /** 描述 */
        public String desp() default "";
    }

    /**
     * 保存或修改时整个对象时该属性对应的图片需要索引
     * 
     * @ClassName NoFilterHtmlTag
     * @author <a href="mailto:donggongai@126.com" target="_blank">kevin</a>
     * @date 2014-4-21 上午9:29:40
     * 
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IndexImgArchives {
        public boolean value() default true;

        /** 描述 */
        public String desp() default "";
    }

    /**
     * 保存或修改时整个对象时该属性对应的图片需要索引
     * 
     * @ClassName NoFilterHtmlTag
     * @author <a href="mailto:donggongai@126.com" target="_blank">kevin</a>
     * @date 2014-4-21 上午9:29:40
     * 
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IndexImgCompany {
        public boolean value() default true;

        /** 描述 */
        public String desp() default "";
    }

    /**
     * 保存或修改时整个对象时该属性对应的附件需要索引
     * 
     * @ClassName NoFilterHtmlTag
     * @author <a href="mailto:donggongai@126.com" target="_blank">kevin</a>
     * @date 2014-4-21 上午9:29:40
     * 
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IndexAffix {
        public boolean value() default true;

        /** 描述 */
        public String desp() default "";
    }

}
