package thinkpad;

import java.util.Map;

import com.model.Zddl;
/**
 * 用内嵌map来实现层次结构：三层，类似字典。
 * 图例： a1
 *       -b1
 *         -c1
 *       -b2
 *         -c2
 *     a2
 *       -b3
 *         -c3    
 * @author 玄承勇
 *
 */
public class Dictionary {
    

    public static Map<Zddl, Map<String, Object>> instance;

    /**
     * 查询每一分类所有信息
     * 
     * @param z
     *            大类编码
     * @return
     */
    public static Map<String, Object> getInfoList(Zddl z) {
        return instance != null ? instance.get(z) : null;
    }

    /**
     * 查询每一分类所有信息
     * 
     * @param z
     *            大类编码
     * @return
     */
    public static Map<String, Object> getInfoList(String z) {
        return getInfoList(new Zddl(z));
    }

    /**
     * 根据分类和key，查询具体字典信息
     */
    public static Object getInfo(Zddl z, String key) {
        return getInfoList(z) != null ? getInfoList(z).get(key) : null;
    }

    /**
     * 根据分类和key，查询具体字典信息
     */
    public static Object getInfo(String z, String key) {
        return getInfoList(z) != null ? getInfoList(z).get(key) : null;
    }
}
