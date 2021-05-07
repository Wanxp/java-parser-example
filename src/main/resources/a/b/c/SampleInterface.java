package a.b.c;

import java.util.List;

/**
 * 接口
 */
public interface SampleInterface {
    /**
     * 接口方法1
     * @param name
     * @return
     */
    List<String> getStrings(String name);

    /**
     * 接口方法2
     * @param name
     * @return
     */
    List<String> setStrings(String name,  List<String> lists);

    /**
     * 接口方法3
     * @param name
     * @return
     */
    void handle(String name);

    /**
     * 接口方法4
     * @param name
     * @return
     */
    List<String> getDefaultStrings();

}