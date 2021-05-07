package a.b.c;

import java.util.HashMap;
import java.util.List;

/**
 * 例子类
 */
public class Smaple extends SmapleAbstract implements SampleInterface{
    /**
     * 一百万
     */
    public static int ONE_BILLION = 1000000000;
    /**
     * 两百万
     */
    public static int TWO_BILLION = 2_000_000_000;

    /**
     * 内容
     */
    private Map<String, List<String>> content = new HashMap<>();

    /**
     * 实现方法1
     * @param name
     * @return
     */
    public List<String> getStrings(String name) {
        return content.get(name);
    }

    /**
     * 实现方法2
     * @param name
     * @return
     */
    public List<String> setStrings(String name,  List<String> lists) {
        content.put(name, lists);
        return lists;
    }

    /**
     * 实现方法3
     * @param name
     * @return
     */
    public void handle(String name) {
        content.get(name);
        System.out.println("handle :" + name);
    }

    /**
     * 实现方法4
     * @param name
     * @return
     */
    public List<String> getDefaultStrings() {
        return  content.get("default");
    }

    /**
     * 抽象方法实现
     * @return
     */
    public String getAbstractName() {
        return "抽象名称";
    }

}