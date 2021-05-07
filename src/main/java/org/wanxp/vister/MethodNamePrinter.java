package org.wanxp.vister;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * 简单的方法遍历
 * 打印名称
 */
public class MethodNamePrinter extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodDeclaration md, Void arg) {
        System.out.println("method name printed:" + md.getName());
    }
}