package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;

import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Lexer.Symbols.Literal;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map.Entry;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class FunctionCall {

  private Table table;
  LocalIndexAllocator allocator;
  Label start, end;
  Table func;

  public FunctionCall(Table table, LocalIndexAllocator allocator, Label start, Label end) {
    this.table = table;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
    func = null;
  }

  public Table checkFunction(MethodVisitor mv, MainNode astNode) {
    GenericNode<?> functionName = (GenericNode<?>) astNode.getChildrenList().getFirst();
    MainNode params = (MainNode) astNode.getChildrenList().get(1);
    Table function = Utils.searchIdentifier(mv, table, functionName.getValue(), false, false);
    HashMap<String, String> functionArgs = new HashMap<>();
    if (!params.getChildrenList().isEmpty()) {
      getParams(mv, params, functionArgs);

    } else {
      System.out.println();
//      Table funcNoArgs = table.resolve(functionName.getValue());
//      setFunc(funcNoArgs);
//      mv.visitMethodInsn(INVOKESPECIAL, table.getClassName(),
//          functionName.getValue(),
//          function.descriptorFunction, false);
//      if (!funcNoArgs.descriptor.equals("V")) {
//        int index = allocator.allocate();
//        //methodVisitor.visitVarInsn(Opcodes.ISTORE, index);
//        Utils.execute("store", mv,
//            new SimpleEntry<String, String>("", function.descriptor), index);
//        StringBuilder stringBuilder = new StringBuilder("_");
//        String res = function.name + stringBuilder.substring(0, stringBuilder.length() - 1);
//        table.getSymbols()
//            .put(res, new Table(res, function.descriptor, false, false, false, null, index));
//        functionArgs.put("Function", String.valueOf(index));
//      }
    }
    mv.visitVarInsn(ALOAD, 1);

    for (Entry<String, String> entry : functionArgs.entrySet()) {
      if (!functionArgs.getOrDefault("Function", "").isEmpty()) {
        Utils.execute("load", mv, new SimpleEntry<String, String>("", func.descriptor),
            Integer.parseInt(functionArgs.get("Function")));
      }
      if (!functionArgs.getOrDefault("Literal", "").isEmpty() && functionArgs.getOrDefault(
          "Identifier", "").isEmpty() && functionArgs.getOrDefault("Function", "").isEmpty()) {
        Utils.loadLitField(mv, new SimpleEntry<>("", new Literal().typeOfLiteral(entry.getValue())),
            entry.getValue());
      } else if (!functionArgs.getOrDefault("Identifier", "").isEmpty()
          && functionArgs.getOrDefault(
          "Literal", "").isEmpty() && functionArgs.getOrDefault("Function", "").isEmpty()) {
        Utils.searchIdentifier(mv, table, functionArgs.get("Identifier"), true, false);

       /* try {
          Utils.searchIdentifier(mv, table, functionArgs.get("Identifier"), true, false);
        } catch (Exception e) {
          int index = allocator.allocate();
          table.getSymbols()
              .put(functionArgs.get("Identifier"), new Table(functionArgs.get("Identifier"),
                  DescriptorUtils.getTypeFromString(variableType).getDescriptor(), false, false,
                  false,
                  null, index));
          Utils.searchIdentifier(mv, table, functionArgs.get("Identifier"), false, false);
        }*/
      }
    }
    mv.visitMethodInsn(INVOKESPECIAL, table.getClassName(), functionName.getValue(),
        function.descriptorFunction, false);
    if (function.descriptor.equals("V")) {
      return new Table("void_res", function.descriptor, false, false, false, null, -1);
    }
    int ind = allocator.allocate();
    Utils.execute("store", mv, new SimpleEntry<>("", function.descriptor), ind);
    return new Table("unk", function.descriptor, false, false, false, null, ind);
  }

  public Table getFunc() {
    return func;
  }

  public void setFunc(Table func) {
    this.func = func;
  }

  private void getParams(MethodVisitor methodVisitor, MainNode astNode,
      HashMap<String, String> functionArgs) {
    for (ASTNode node : astNode.getChildrenList()) {
      MainNode mainNode = (MainNode) node;
      if (node.getName().equals("Expression")) {
        for (ASTNode expressionNode : mainNode.getChildrenList()) {
          if (expressionNode.getName().equals("Identifier")) {
            String type = ((GenericNode<?>) expressionNode).getValue();
            functionArgs.put("Identifier", type);
          } else {
            String type = ((GenericNode<?>) expressionNode).getValue();
            functionArgs.put("Literal", type);
          }
        }
      } else if (node.getName().equals("FunctionCall")) {
        GenericNode<?> functionName = (GenericNode<?>) ((MainNode) node).getChildrenList()
            .getFirst();
        getParams(methodVisitor, ((MainNode) ((MainNode) node).getChildrenList().get(1)),
            functionArgs);
        Table function = table.resolve(functionName.getValue());
        setFunc(function);
        methodVisitor.visitVarInsn(ALOAD, 1);
        if (!functionArgs.isEmpty()) {
          StringBuilder stringBuilder = new StringBuilder("_");
          for (Entry<String, String> entry : functionArgs.entrySet()) {
            stringBuilder.append(entry.getValue());
            stringBuilder.append("_");
            if (entry.getKey().equals("Literal")) {
              Utils.loadLitField(methodVisitor,
                  new SimpleEntry<>("", new Literal().typeOfLiteral(entry.getValue())),
                  entry.getValue());
            } else {
              Table arg = Utils.searchIdentifier(methodVisitor, table, entry.getValue(), true,
                  false);
            }
          }
          methodVisitor.visitMethodInsn(INVOKESPECIAL, table.getClassName(),
              functionName.getValue(),
              function.descriptorFunction, false);
          int index = allocator.allocate();
          //methodVisitor.visitVarInsn(Opcodes.ISTORE, index);
          Utils.execute("store", methodVisitor,
              new SimpleEntry<String, String>("", function.descriptor), index);
          String res = function.name + stringBuilder.substring(0, stringBuilder.length() - 1);
          table.getSymbols()
              .put(res, new Table(res, function.descriptor, false, false, false, null, index));
          functionArgs.put("Function", String.valueOf(index));
        }
      }
    }
  }
}
