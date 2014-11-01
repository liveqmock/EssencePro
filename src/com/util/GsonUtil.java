package com.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    // Gson——序列化和反序列化
    // 对于复杂点的type，得到type方式：new
    // com.google.gson.reflect.TypeToken<List<User>>(){}.getType(
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不导出实体中没有用@Expose注解的属性
                .enableComplexMapKeySerialization()// 支持Map的key为复杂对象的形式
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")// 时间转化为特定格式
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)// 会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
                .setPrettyPrinting() // 对json结果格式化.
                .setVersion(1.0) // 有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                                 // @Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
                                 // @Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
                .create();
    }

}
