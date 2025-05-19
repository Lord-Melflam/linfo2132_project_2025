package compiler.CodeGeneration;

import compiler.CodeGeneration.Utils.DescriptorUtils;
import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap.SimpleEntry;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class Return {

  private Table table;
  LocalIndexAllocator allocator;
  Label start, end;

  public Return(Table table, LocalIndexAllocator allocator, Label start, Label end) {
    this.table = table;
    this.allocator = allocator;
  }

  public Table checkReturn(MethodVisitor cw, ASTNode astNode, String functionType,
      SimpleEntry<String, String> stringStringSimpleEntry) throws OperatorError {
    MainNode expression = (MainNode) astNode;
    for (ASTNode expressionNode : expression.getChildrenList()) {
      if (expressionNode.getName().contains("Expression")) {
        ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
            allocator, start, end);
        Table index = expressionCodeGenerator.checkExpression((MainNode) expressionNode, cw, false);

        Function.table.resolve(stringStringSimpleEntry.getKey()).setIndex(index.index);
        table.getSymbols().put(stringStringSimpleEntry.getKey(),
            new Table(stringStringSimpleEntry.getKey(),
                DescriptorUtils.getTypeFromString(stringStringSimpleEntry.getValue())
                    .getDescriptor(), false, false, false, null, index.index));
        Utils.execute("load", cw, new SimpleEntry<String, String>("", functionType), index.index);
        Utils.execute("return", cw, new SimpleEntry<String, String>("", functionType), -1);
        return new Table("unk", DescriptorUtils.getTypeFromString(functionType).getDescriptor(),
            false, false, false, null, index.index);
        //cw.visitMaxs(0, 0);

       /* switch (functionType) {
          case "int", "float" -> {
            cw.visitVarInsn(FLOAD, index);
            cw.visitInsn(FRETURN);
          }
          case "string" -> {
            cw.visitVarInsn(ALOAD, index);
            cw.visitInsn(ARETURN);
          }
        }*/
      } else if (expressionNode.getName().contains("BuiltInFunctionCall")) {
        BuiltInFunctionCall builtInFunctionCall = new BuiltInFunctionCall(table, "", allocator,
            start, end);
        Table table1 = builtInFunctionCall.CheckBuiltInFunctionCall(cw, expressionNode, false);
        Utils.execute("load", cw, new SimpleEntry<String, String>("", table1.descriptor),
            table1.index);

        Utils.execute("return", cw, new SimpleEntry<String, String>("", table1.descriptor), -1);
        return new Table("unk", table1.descriptor, false, false, false, null, table1.index);
      }
    }
    return null;
  }
}
