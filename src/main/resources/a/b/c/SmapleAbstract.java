package a.b.c;
import java.util.List;

/**
 * 抽象类类
 */
public abstract class SmapleAbstract {

    /**
     * 抽象方法
     * @return
     */
    public abstract String getAbstractName();

    /**
     * 父类方法
     * @return
     */
    public String getName() {
        return "名称"
    }
}