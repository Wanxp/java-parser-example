import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.CompilationUnitContext;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.MemoryTypeSolver;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.File;

/**
 * 测试内存中获取类型
 */
public class TestMemoryType {

    private static final String FILE_PATH = "src/main/resources/a/b/c/Bar.java";

    @Test
    public void solveTypeInSamePackage() throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        AssertionFailedError failedError = new AssertionFailedError();
        ResolvedReferenceTypeDeclaration otherClass = EasyMock.createMock(ResolvedReferenceTypeDeclaration.class);
        EasyMock.expect(otherClass.getQualifiedName()).andReturn("a.b.c.Bar");
        /* Start of the relevant part */
        MemoryTypeSolver memoryTypeSolver = new MemoryTypeSolver();
        memoryTypeSolver.addDeclaration(
                "a.b.c.Bar", otherClass);
        Context context = new CompilationUnitContext(cu, memoryTypeSolver);
        /* End of the relevant part */
        EasyMock.replay(otherClass);
        SymbolReference<ResolvedTypeDeclaration> ref = context.solveType("Bar");
        Assertions.assertEquals(true, ref.isSolved());
        Assertions.assertEquals("a.b.c.Bar", ref.getCorrespondingDeclaration().getQualifiedName());
    }
}
