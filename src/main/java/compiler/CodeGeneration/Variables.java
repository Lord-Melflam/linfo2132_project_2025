package compiler.CodeGeneration;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.PUTFIELD;

import compiler.CodeGeneration.Utils.DescriptorUtils;
import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class Variables {

  private Table table;
  LocalIndexAllocator allocator;
  Label start, end;

  public Variables(Table table, LocalIndexAllocator allocator, Label start, Label end) {
    this.table = table;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
  }

  public void CheckVariables(ClassWriter cw, MainNode child, MethodVisitor constructor)
      throws OperatorError {
    String variableIdentifier = "";
    String variableType = "";
    ASTNode expression = null;
    boolean isFinal = false;
    for (ASTNode astNode : child.getChildrenList()) {
      if (astNode.getName().equals("Keyword")) {
        GenericNode<?> keyword = ((GenericNode<?>) astNode);
        isFinal = keyword.getValue().equals("final");
        /*if (keyword.getValue().equals("array")) {
          break;
        }*/
      }
      if (astNode.getName().equals("Identifier")) {
        GenericNode<?> variableIdentifierNode = ((GenericNode<?>) astNode);
        variableIdentifier = variableIdentifierNode.getValue();
      }
      if (astNode.getName().equals("TypeSpecifier")) {
        GenericNode<?> variableTypeNode = ((GenericNode<?>) astNode);
        if (variableType.isEmpty()) {
          variableType = variableTypeNode.getValue();
        }
       /* if (variableType.contains("[]")) {
          checkArray(cw, child);
          break;
        }*/
      }
      if (astNode.getName().equals("Expression") || astNode.getName()
          .equals("BuiltInFunctionCall")) {
        expression = (MainNode) astNode;
      }
    }
    String descriptor = DescriptorUtils.getTypeFromString(variableType).getDescriptor();
    table.getSymbols().put(variableIdentifier,
        new Table(variableIdentifier, descriptor, true, false, false, null, -1));
    int access = ACC_PUBLIC + ACC_FINAL;
    if (expression != null) {
      LinkedList<String> rightPart = new VariableRightPart(table, variableIdentifier, allocator,
          start, end).checkVariableRightPart(constructor, expression, true);

      if (rightPart != null && !rightPart.isEmpty() && rightPart.getLast() != null) {

        if (child.getName().equals("Constant") || (child.getName().equals("Initialisation")
            && !rightPart.getLast().isEmpty())) {
          if (!variableType.contains("[]")) {
            constructor.visitVarInsn(ALOAD, 0);
            if (!isFinal) {
              access -= ACC_FINAL;
            }
            Utils.InitAttribute(cw, "variableType", variableIdentifier, access, descriptor, null);
            Utils.execute("load", constructor, new SimpleEntry<>("", variableType),
                Integer.parseInt(rightPart.getLast()));
            constructor.visitFieldInsn(PUTFIELD, table.getClassName(), variableIdentifier,
                DescriptorUtils.getTypeFromString(variableType).getDescriptor());
          } else {
            constructor.visitVarInsn(ALOAD, 0);
            if (!isFinal) {
              access -= ACC_FINAL;
            }
            Utils.InitAttribute(cw, variableType, variableIdentifier, access, descriptor, null);
            Utils.execute("load", constructor, new SimpleEntry<>("", "int"),
                Integer.parseInt(rightPart.getLast()));// this
            String elementType = variableType.substring(0, variableType.length() - 2);// 9

            Utils.execute("array", constructor, new SimpleEntry<>("", elementType),
                -1);      // new int[9]
            constructor.visitFieldInsn(PUTFIELD, table.getClassName(), variableIdentifier,
                descriptor); // this.test = new int[9]

          }
        } else if (child.getName().equals("Initialisation") && rightPart.isEmpty()) {
          Utils.InitAttribute(cw, variableType, variableIdentifier, access, descriptor,
              rightPart.getLast());
        }
       /* if (variableType.contains("[]")) {
          MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
          mv.visitCode();
          mv.visitVarInsn(ALOAD, 0);                 // this
          mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
          mv.visitVarInsn(ALOAD, 0);                 // this
          mv.visitLdcInsn(Integer.parseInt(rightPart.getLast()));
          String elementType = variableType.substring(0, variableType.length() - 2);// 9
          Utils.execute("array", mv, new SimpleEntry<>("", elementType), -1);      // new int[9]
          mv.visitFieldInsn(PUTFIELD, "Main", variableIdentifier,
              descriptor); // this.test = new int[9]

        }*/
      }
    } else {
      if ((child.getName().equals("Declaration"))) {
        if (table == null) {
          cw.visitField(access, variableIdentifier, descriptor, null, null);
          return;
        }
        /*todo error a int; a=3;*/
      }
    }
  }

  private void checkArray(ClassWriter cw, LinkedList<String> rightPart) {
    /*String variableIdentifier = "";
    String variableType = "";
    ASTNode expression = null;
    LinkedList<ASTNode> childrenList = child.getChildrenList();
    for (int i = 0; i < childrenList.size(); i++) {
      ASTNode astNode = childrenList.get(i);
      if (astNode.getName().equals("Identifier")) {
        GenericNode<?> variableIdentifierNode = ((GenericNode<?>) astNode);
        variableIdentifier = variableIdentifierNode.getValue();
      }
      if (astNode.getName().equals("TypeSpecifier")) {
        GenericNode<?> variableTypeNode = ((GenericNode<?>) astNode);
        variableType = variableTypeNode.getValue();

      }
      if (astNode.getName().equals("SpecialSymbol")) {
        GenericNode<?> openBracket = (GenericNode<?>) astNode;
        if (openBracket.getValue().equals(TokenType.LBRACKET.getValue())) {
          expression = childrenList.get(i + 1);
        }
      }
    }
    LinkedList<String> rightPart = new VariableRightPart(table, variableIdentifier, allocator,
        start, end).checkVariableRightPart(null, expression);
    System.out.println();*/
   /* if (rightPart.isEmpty()) {
      throw new IllegalArgumentException("BIG PRoB");
    }
    if (rightPart.getFirst().equals("Literal")) {
      int access = ACC_PUBLIC + ACC_STATIC + ACC_FINAL;
      Utils.InitAttribute(cw, variableType, variableIdentifier, access,
          DescriptorUtils.getTypeFromString(variableType).getDescriptor(),
          rightPart.getLast());

    }*/
  }


  public void CheckVariables2(MethodVisitor cw, MainNode mainNode) throws OperatorError {
    String variableIdentifier = "";
    String variableType = "";
    ASTNode expression = null;
    LinkedList<ASTNode> childrenList = mainNode.getChildrenList();
    for (int i = 0; i < childrenList.size(); i++) {
      ASTNode astNode = childrenList.get(i);
      if (astNode.getName().equals("Identifier")) {
        GenericNode<?> variableIdentifierNode = ((GenericNode<?>) astNode);
        variableIdentifier = variableIdentifierNode.getValue();
      }
      if (astNode.getName().equals("TypeSpecifier")) {
        GenericNode<?> variableTypeNode = ((GenericNode<?>) astNode);
        if (variableType.isEmpty()) {
          variableType = variableTypeNode.getValue();
        }
      }
      if (astNode.getName().equals("Assignment")) {
        if (!childrenList.get(i + 1).getName().equals("Keyword")) {
          expression = (MainNode) childrenList.get(i + 1);
          break;
        } else {
          GenericNode<?> genericNode = ((GenericNode<?>) childrenList.get(i + 1));
          if (genericNode.getValue().equals("array")) {
            expression = (MainNode) childrenList.get(i + 3);
            break;
          }
        }
      }
    }
    if (mainNode.getName().equals("Initialisation") && variableType.isEmpty()) {
      try {
        Table table1 = Utils.searchIdentifier(cw, table, variableIdentifier, false, false);
        variableType = DescriptorUtils.descriptorToString(table1.descriptor);
      } catch (Exception e) {

      }

    }
    if (expression != null) {
      if (expression.getName().equals("BinaryExpression")) {
        ExpressionCodeGenerator expressionCodeGenerator = new ExpressionCodeGenerator(table,
            allocator, start, end);
        Table res = expressionCodeGenerator.checkExpression(expression, cw, false);
        Utils.execute("load", cw, new SimpleEntry<String, String>("", res.descriptor), res.index);
        Table ind;
        try {
          ind = Utils.searchIdentifier(cw, table, variableIdentifier, false, false);
        } catch (Exception e) {
          int index = allocator.allocate();
          table.getSymbols().put(variableIdentifier, new Table(variableIdentifier,
              DescriptorUtils.getTypeFromString(variableType).getDescriptor(), false, false, false,
              null, index));
          ind = Utils.searchIdentifier(cw, table, variableIdentifier, false, false);
        }

        Utils.execute("store", cw, new SimpleEntry<String, String>("", ind.descriptor), ind.index);
        return;
      }
      LinkedList<String> rightPart = new VariableRightPart(table, variableIdentifier, allocator,
          start, end).checkVariableRightPart(cw, expression, false);
      if (rightPart != null) {
        if (mainNode.getName().equals("Constant") || (mainNode.getName().equals("Initialisation")
            && !rightPart.isEmpty())) {
          String descriptor = DescriptorUtils.getTypeFromString(variableType).getDescriptor();
          int index = allocator.allocate();
          if (rightPart.getFirst().equals("Identifier")) {
            //Utils.searchIdentifier(cw, table, rightPart.getLast(), true);

            if (variableType.contains("[]")) {
              if (Integer.parseInt(rightPart.get(1)) == -1) {
                Utils.searchIdentifier(cw, table, rightPart.getLast(), true, false);
              } else {
                Utils.execute("load", cw, new SimpleEntry<String, String>("", "int"),
                    Integer.parseInt(rightPart.getLast()));
              }
              String elementType = variableType.substring(0, variableType.length() - 2);// 9
              Utils.execute("array", cw, new SimpleEntry<>("", elementType), -1);
              if (mainNode.getName().equals("Initialisation")) {
                try {
                  Table table1 = Utils.searchIdentifier(cw, table, variableIdentifier, false,
                      false);
                  Utils.execute("store", cw, new SimpleEntry<>("", table1.descriptor),
                      table1.index);
                  Utils.execute("load", cw, new SimpleEntry<>("", table1.descriptor), table1.index);
                } catch (Exception ignored) {
                  cw.visitLocalVariable(variableIdentifier, descriptor, null, start, end, index);
                  Utils.execute("store", cw, new SimpleEntry<>("", descriptor), index);
                  table.getSymbols().put(variableIdentifier,
                      new Table(variableIdentifier, descriptor, false, false, false, null, index));

                } finally {
                  return;

                }

              }
              cw.visitLocalVariable(variableIdentifier, descriptor, null, start, end, index);
              table.getSymbols().put(variableIdentifier,
                  new Table(variableIdentifier, descriptor, false, false, false, null, index));

              return;
            }
           /* Utils.execute("load", cw, new SimpleEntry<String, String>("", variableType),
                Integer.parseInt(rightPart.getLast()));
            Utils.execute("store", cw, new SimpleEntry<String, String>("", variableType), index);*/
          } else {
            cw.visitLdcInsn(Integer.parseInt(rightPart.getLast()));
            Utils.execute("store", cw, new SimpleEntry<String, String>("", variableType), index);
          }
          Utils.execute("load", cw, new SimpleEntry<String, String>("", variableType),
              Integer.parseInt(rightPart.getLast()));
          Utils.execute("store", cw, new SimpleEntry<String, String>("", variableType), index);
          cw.visitLocalVariable(variableIdentifier, descriptor, null, start, end, index);
          table.getSymbols().put(variableIdentifier,
              new Table(variableIdentifier, descriptor, false, false, false, null, index));

         /* int access = ACC_PUBLIC + ACC_STATIC;
          if (mainNode.getName().equals("Constant")) {
            access += ACC_FINAL;
          }
          cw.visitField(access, variableIdentifier, descriptor, null,
              rightPart);*/
        } /*else if (child.getName().equals("Initialisation") && rightPart.isEmpty()) {
          String descriptor = DescriptorUtils.getTypeFromString(variableType).getDescriptor();
          cw.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, variableIdentifier, descriptor, null,
              rightPart);
        }*/
      }

    } else {
      int index = allocator.allocate();
      cw.visitLocalVariable(variableIdentifier,
          DescriptorUtils.getTypeFromString(variableType).getDescriptor(), null, start, end, index);
      Table temp = new Table(variableIdentifier,
          DescriptorUtils.getTypeFromString(variableType).getDescriptor(), false, false, false,
          null, index);
      temp.setInitialised(false);
      table.getSymbols().put(variableIdentifier, temp);
    }
  }
}
