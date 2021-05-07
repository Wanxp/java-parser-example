### 说明
1. 根据《JavaParser: Visited》Nicholas Smith, Danny van Bruggen and Federico Tomassetti，2021-02-05， Learnpub
2. 使用书中案例
3. 具体方法调用与测试在Main类中
### 详情
```java
        //1. 简单访问
        testMethodNamePrinter();
        //2. 修改属性
        testFieldIntegerLiteralModifier();
        //3. 测试注释
        testGetAllContainedComments();
        //4. 获取引用类型
        testGetTypeOfReference();
        //5. 获取 引用类型2 方法内 反射方法
        testUsingTypeSolver();
        //6. 获取 引用类型3 属性 源码上下文
        testResolveTypeInContext();
        //7. 获取 引用类型4 入参
        testResolveMethodCalls();
        //8. 获取 引用类型5 负载复杂环境,既有jar、又有源码和生成后的源码
        testCombinedTypeResolve();
        //9. 最有单元测试有内存作为上下文的处理方式

```
### 书目
《JavaParser: Visited》在本项目doc文件夹