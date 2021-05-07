package org.wanxp;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.CompilationUnitContext;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.*;
import org.wanxp.model.CommentReportEntry;
import org.wanxp.vister.FieldIntegerLiteralModifier;
import org.wanxp.vister.MethodNamePrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("1  ================ testMethodNamePrinter =================");
        //简单访问
        testMethodNamePrinter();
        System.out.println("\n2  ================ testFieldIntegerLiteralModifier =================");
        //修改属性
        testFieldIntegerLiteralModifier();
        System.out.println("\n3  ================ testGetAllContainedComments =================");
        //测试注释
        testGetAllContainedComments();
        System.out.println("\n4  ================ testGetTypeOfReference =================");
        //获取引用类型
        testGetTypeOfReference();
        System.out.println("\n5  ================ testUsingTypeSolver =================");
        //获取 引用类型2 方法内 反射方法
        testUsingTypeSolver();
        System.out.println("\n6  ================ testResolveTypeInContext =================");
        //获取 引用类型3 属性 源码上下文
        testResolveTypeInContext();
        System.out.println("\n7  ================ testResolveMethodCalls =================");
        //获取 引用类型4 入参
        testResolveMethodCalls();
        System.out.println("\n7  ================ testCombinedTypeResolve =================");
        //获取 引用类型5 负载复杂环境,既有jar、又有源码和生成后的源码
        testCombinedTypeResolve();
    }


    private static final String SIMPLE_FILE_PATH = "src/main/resources/a/b/c/Smaple.java";

    private static final String SYMBOL_FILE_PATH = "src/main/resources/a/b/c/Bar.java";

    private static final String SYMBOL_FOO_FILE_PATH = "src/main/resources/a/b/c/Foo.java";

    private static final String SYMBOL_A_FILE_PATH = "src/main/resources/a/b/c/A.java";


    private static final String SYMBOL_SRC_PATH = "src/main/resources";


    /**
     * 简单访问
     *
     * 访问方法并打印方法名
     * 输出:
     * method name printed:getStrings
     * method name printed:setStrings
     * method name printed:handle
     * method name printed:getDefaultStrings
     * method name printed:getAbstractName
     *
     * @throws FileNotFoundException
     */
    private static void testMethodNamePrinter() throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(SIMPLE_FILE_PATH));
        VoidVisitor<Void> printer = new MethodNamePrinter();
        printer.visit(cu, null);
    }

    /**
     * 修改属性
     *
     * 属性数字格式转换
     * 属性 1000000000 -> 1_000_000_000
     * @throws FileNotFoundException
     */
    private static void testFieldIntegerLiteralModifier() throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(SIMPLE_FILE_PATH));
        ModifierVisitor<Void> modifier = new FieldIntegerLiteralModifier();
        modifier.visit(cu, null);
        System.out.println(cu);
    }


    /**
     * 注释列表访问
     *
     * @throws FileNotFoundException
     */
    private static void testGetAllContainedComments() throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(SIMPLE_FILE_PATH));
        List<Comment> comments = cu.getAllComments();
        List<CommentReportEntry> reportEntries =
                comments
                        .stream()
                        .map(p -> new CommentReportEntry(p.getClass().getSimpleName(),
                                p.getContent(),
                                p.getRange().map(r -> r.begin.line).orElse(-1),
                                !p.getCommentedNode().isPresent()))
                        .collect(Collectors.toList());
        reportEntries.forEach(System.out::println);
    }


    /**
     * 获取 引用类型 基本
     * 输出
     * a = a + 1 is a: PrimitiveTypeUsage{name='int'}
     * @throws FileNotFoundException
     */
    private static void testGetTypeOfReference() throws FileNotFoundException {
        TypeSolver typeSolver = new CombinedTypeSolver();
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration()
                .setSymbolResolver(symbolSolver);
        CompilationUnit cu = StaticJavaParser.parse(new File(SYMBOL_FILE_PATH));
        cu.findAll(AssignExpr.class)
                .forEach(ae -> {
                    ResolvedType resolvedType = ae.calculateResolvedType();
                    System.out.println(ae + " is a: " + resolvedType);
                });
    }

    /**
     * 获取 引用类型 上下文依赖
     * 输出
     *
     *  fileds:
     *     ResolvedArrayType{PrimitiveTypeUsage{name='char'}} value
     *     PrimitiveTypeUsage{name='int'} hash
     *     PrimitiveTypeUsage{name='long'} serialVersionUID
     *     ResolvedArrayType{ReferenceType{java.io.ObjectStreamField, typeParametersMap=TypeParametersMap{nameToValue={}}}} serialPersistentFields
     *     ReferenceType{java.util.Comparator, typeParametersMap=TypeParametersMap{nameToValue={java.util.Comparator.T=ReferenceType{java.lang.String, typeParametersMap=TypeParametersMap{nameToValue={}}}}}} CASE_INSENSITIVE_ORDER
     * @throws FileNotFoundException
     */
    private static void testUsingTypeSolver()  {
        TypeSolver typeSolver = new ReflectionTypeSolver();
        showReferenceTypeDeclaration(typeSolver.solveType("java.lang.Object"));
        showReferenceTypeDeclaration(typeSolver.solveType("java.lang.String"));
        showReferenceTypeDeclaration(typeSolver.solveType("java.util.List"));
    }

    /**
     * 打印方法
     * @param r
     */
    private static void showReferenceTypeDeclaration(ResolvedReferenceTypeDeclaration r) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("== %s ==", r.getQualifiedName()));
        sb.append("\n fileds: \n");
        r.getAllFields().forEach(f ->
            sb.append(String.format("    %s %s", f.getType(), f.getName())).append("\n"));
        System.out.println(sb);
    }

    /**
     * 获取 引用类型 属性
     * 输出
     * Field type: a.b.c.Bar
     * @throws FileNotFoundException
     */
    private static void testResolveTypeInContext() throws FileNotFoundException {
        TypeSolver reflectionTypeSolver = new ReflectionTypeSolver();
        TypeSolver javaParserTypeSolver = new JavaParserTypeSolver(new File(SYMBOL_SRC_PATH));
//        reflectionTypeSolver.setParent(reflectionTypeSolver);
        CombinedTypeSolver combinedSolver = new CombinedTypeSolver();
        combinedSolver.add(reflectionTypeSolver);
        combinedSolver.add(javaParserTypeSolver);
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedSolver);
        StaticJavaParser.getConfiguration()
                .setSymbolResolver(symbolSolver);
        CompilationUnit cu = StaticJavaParser.parse(new File(SYMBOL_FOO_FILE_PATH));
        FieldDeclaration fieldDeclaration = Navigator.findNodeOfGivenClass(cu, FieldDeclaration.class);
        System.out.println("Field type: " + fieldDeclaration.getVariables().get(0).getType()
            .resolve().asReferenceType().getQualifiedName());
    }

    /**
     * 获取 引用类型 入参类型
     */
    private static void testResolveMethodCalls() throws FileNotFoundException {
        TypeSolver typeSolver = new ReflectionTypeSolver();
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration()
                .setSymbolResolver(symbolSolver);
        CompilationUnit cu = StaticJavaParser.parse(new File(SYMBOL_A_FILE_PATH));
        cu.findAll(MethodCallExpr.class).forEach(mce ->
                System.out.print(mce.resolve().getQualifiedSignature() + "\n"));

    }

    /**
     *  获取 引用类型 复杂上下文
     *  标准jre
     *  3个不同的jar包
     *  2个生成后的源码(比如lombok,生成后的源码)
     * @throws IOException
     */
    private static void testCombinedTypeResolve() throws IOException {
        TypeSolver myTypeSolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                JarTypeSolver.getJarTypeSolver("jars/library1.jar"),
                JarTypeSolver.getJarTypeSolver("jars/library2.jar"),
                JarTypeSolver.getJarTypeSolver("jars/library3.jar"),
                new JavaParserTypeSolver(new File("src/main/java")),
                new JavaParserTypeSolver(new File("generated_code"))
        );
        // using myTypeSolver
    }





}
