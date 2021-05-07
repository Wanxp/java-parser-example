package org.wanxp.vister;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import java.util.regex.Pattern;

/**
 * 属性 数字格式 修改
 * 属性 1000000000 -> 1_000_000_000
 */
public class FieldIntegerLiteralModifier extends ModifierVisitor<Void> {

    /**
     * 格式匹配
     */
    private static final Pattern LOOK_AHEAD_THREE = Pattern.compile("(\\d)(?=(\\d{3})+$)");

    @Override
    public Visitable visit(FieldDeclaration fd, Void arg) {
        super.visit(fd, arg);
        fd.getVariables().forEach(v ->
                v.getInitializer().ifPresent(i ->
                        i.ifIntegerLiteralExpr(i1 ->
                                v.setInitializer(formatWithUnderscores(i1.getValue())))));
        return fd;
    }

    /**
     * 替换下划线
     * @param value
     * @return
     */
    static String formatWithUnderscores(String value) {
        String withoutUnderscores = value.replace("_", "");
        return  LOOK_AHEAD_THREE.matcher(withoutUnderscores).replaceAll("$1_");
    }
}
