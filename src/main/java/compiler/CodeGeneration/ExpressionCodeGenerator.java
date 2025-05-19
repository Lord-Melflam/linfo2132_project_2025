package compiler.CodeGeneration;
/*

import static org.objectweb.asm.Opcodes.FADD;
import static org.objectweb.asm.Opcodes.FDIV;
import static org.objectweb.asm.Opcodes.FMUL;
import static org.objectweb.asm.Opcodes.FREM;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.FSUB;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.I2F;
import static org.objectweb.asm.Opcodes.ILOAD;

import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.ExtractGenericVisitor;
import compiler.Semantic.ExtractMainNodeVisitor;
import java.util.LinkedList;
import org.objectweb.asm.MethodVisitor;

public class ExpressionCodeGenerator {

  private Table table;
  LocalIndexAllocator allocator;

  public ExpressionCodeGenerator(Table table, LocalIndexAllocator allocator) {
    this.table = table;
    this.allocator = allocator;
  }

  public Table checkExpression(ASTNode expressionNode, MethodVisitor mv) throws OperatorError {
    switch (expressionNode.getName()) {
      case "BinaryExpression" -> {
        LinkedList<ASTNode> children = ((MainNode) expressionNode).getChildrenList();
        ASTNode left = children.get(0);
        GenericNode<?> operator = (GenericNode<?>) children.get(1);
        ASTNode right = children.get(2);
        generateValue((MainNode) left, mv);
        generateValue((MainNode) right, mv);
        switch (operator.getValue()) {
          case "+" -> mv.visitInsn(FADD);
          case "-" -> mv.visitInsn(FSUB);
          case "*" -> mv.visitInsn(FMUL);
          case "/" -> mv.visitInsn(FDIV);
          case "%" -> mv.visitInsn(FREM);
          default -> throw new RuntimeException("Unsupported operator: " + operator.getValue());
        }
        mv.visitVarInsn(FSTORE, allocator.allocate());

        */
/*todo visitVarInsn generail*//*


        return new Table("Tempres", "F", false, false, false, null, allocator.previous());
      }
      case "Expression" -> {
        return generate((MainNode) expressionNode, mv);
      }
    }
    return null;
  }

  public void generateValue(MainNode expressionNode, MethodVisitor mv) throws OperatorError {
    for (ASTNode astNode : expressionNode.getChildrenList()) {
      MainNode mainNode = astNode.accept(new ExtractMainNodeVisitor());
      if (mainNode != null) {
        checkExpression(mainNode, mv);
      } else {
        GenericNode<?> genericNode = astNode.accept(new ExtractGenericVisitor());
        switch (genericNode.getName()) {
          case "Identifier" -> {
            Table index = table.resolve(genericNode.getValue());
            if (index == null) {
              throw new IllegalAccessError("big problem");
            }
            if (index.isField) {
              if (index.descriptor.equals("I")) {
                mv.visitFieldInsn(GETSTATIC, "Main", genericNode.getValue(), "I");
                mv.visitInsn(I2F);
              }
              return;
            }
            mv.visitVarInsn(ILOAD, index.index);
            allocator.allocate();
            mv.visitInsn(I2F);
            */
/*todo visitVarInsn generail*//*

          }
        }
      }
    }
  }

  public Table generate(MainNode expressionNode, MethodVisitor mv) throws OperatorError {
    for (ASTNode astNode : expressionNode.getChildrenList()) {
      MainNode mainNode = astNode.accept(new ExtractMainNodeVisitor());
      if (mainNode != null) {
        checkExpression(mainNode, mv);
      } else {
        GenericNode<?> genericNode = astNode.accept(new ExtractGenericVisitor());
        switch (genericNode.getName()) {
          case "Identifier" -> {
            Table index = Utils.searchIdentifier(mv, table, genericNode.getValue(), false);
            return index;
          }
        }
      }
    }
    return null;
  }
}
*/

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FADD;
import static org.objectweb.asm.Opcodes.FCMPG;
import static org.objectweb.asm.Opcodes.FDIV;
import static org.objectweb.asm.Opcodes.FMUL;
import static org.objectweb.asm.Opcodes.FNEG;
import static org.objectweb.asm.Opcodes.FREM;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.FSUB;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.I2F;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IDIV;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFGE;
import static org.objectweb.asm.Opcodes.IFGT;
import static org.objectweb.asm.Opcodes.IFLE;
import static org.objectweb.asm.Opcodes.IFLT;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPGE;
import static org.objectweb.asm.Opcodes.IF_ICMPGT;
import static org.objectweb.asm.Opcodes.IF_ICMPLE;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;
import static org.objectweb.asm.Opcodes.IMUL;
import static org.objectweb.asm.Opcodes.INEG;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IREM;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.ISUB;
import static org.objectweb.asm.Opcodes.NEW;

import compiler.CodeGeneration.Utils.DescriptorUtils;
import compiler.CodeGeneration.Utils.LocalIndexAllocator;
import compiler.CodeGeneration.Utils.Utils;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Lexer.Symbols.Literal;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.ExtractGenericVisitor;
import compiler.Semantic.ExtractMainNodeVisitor;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ExpressionCodeGenerator {

  private final Table table;
  private final LocalIndexAllocator allocator;
  private HashSet<String> booleanValues = new HashSet<>(List.of("true", "false", "1", "0"));
  Label start, end;

  public ExpressionCodeGenerator(Table table, LocalIndexAllocator allocator, Label start,
      Label end) {
    this.table = table;
    this.allocator = allocator;
    this.start = start;
    this.end = end;
  }

  public Table checkExpression(ASTNode expressionNode, MethodVisitor mv, Boolean isConstructor)
      throws OperatorError {
    switch (expressionNode.getName()) {
      case "BinaryExpression" -> {
        LinkedList<ASTNode> children = ((MainNode) expressionNode).getChildrenList();
        ASTNode left = children.get(0);
        GenericNode<?> operator = (GenericNode<?>) children.get(1);
        ASTNode right = children.get(2);

        Table leftType = generateValue((MainNode) left, mv, isConstructor);
        Table rightType = generateValue((MainNode) right, mv, isConstructor);

        String op = operator.getValue();

        if (op.matches("[+\\-*/%]")) {
          if (leftType.descriptor.equals("I") && rightType.descriptor.equals("I")) {
            switch (op) {
              case "+" -> mv.visitInsn(IADD);
              case "-" -> mv.visitInsn(ISUB);
              case "*" -> mv.visitInsn(IMUL);
              case "/" -> mv.visitInsn(IDIV);
              case "%" -> mv.visitInsn(IREM);
            }
            mv.visitVarInsn(ISTORE, allocator.allocate());
            return new Table("temp", "I", false, false, false, null, allocator.previous());
          } else if (leftType.descriptor.equals("Ljava/lang/String;")
              && rightType.descriptor.equals("Ljava/lang/String;")) {
            // Crée un StringBuilder
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);

            // Append gauche
         /*   if (leftType.descriptor.equals("I")) {
              mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                  "(I)Ljava/lang/StringBuilder;", false);
            } else if (leftType.descriptor.equals("F")) {
              mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                  "(F)Ljava/lang/StringBuilder;", false);
            } else {*/
            mv.visitVarInsn(ALOAD, leftType.index);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
            /*  }*/

            // Append droite
           /* if (rightType.descriptor.equals("I")) {
              mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                  "(I)Ljava/lang/StringBuilder;", false);
            } else if (rightType.descriptor.equals("F")) {
              mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                  "(F)Ljava/lang/StringBuilder;", false);
            } else {*/
            mv.visitVarInsn(ALOAD, rightType.index);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
            /* }*/

            // toString et stockage
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
                "()Ljava/lang/String;", false);
            mv.visitVarInsn(ASTORE, allocator.allocate());

            return new Table("temp", "Ljava/lang/String;", false, false, false, null,
                allocator.previous());
          } else {
            promoteToFloat(leftType, mv);
            promoteToFloat(rightType, mv);
            switch (op) {
              case "+" -> mv.visitInsn(FADD);
              case "-" -> mv.visitInsn(FSUB);
              case "*" -> mv.visitInsn(FMUL);
              case "/" -> mv.visitInsn(FDIV);
              case "%" -> mv.visitInsn(FREM);
            }
            mv.visitVarInsn(FSTORE, allocator.allocate());
            return new Table("temp", "F", false, false, false, null, allocator.previous());
          }
        }

        if (op.matches("==|!=|<|>|<=|>=")) {
          Label trueLabel = new Label();
          Label endLabel = new Label();

          if (leftType.descriptor.equals("F") || rightType.descriptor.equals("F")) {
            promoteToFloat(leftType, mv);
            promoteToFloat(rightType, mv);
            mv.visitInsn(FCMPG);
            handleComparison(op, trueLabel, mv, true);
          } else {
            mv.visitJumpInsn(getJumpOpcode(op), trueLabel);
          }

          mv.visitInsn(ICONST_0);
          mv.visitJumpInsn(GOTO, endLabel);
          mv.visitLabel(trueLabel);
          mv.visitInsn(ICONST_1);
          mv.visitLabel(endLabel);
          mv.visitVarInsn(ISTORE, allocator.allocate());
          return new Table("temp", "Z", false, false, false, null, allocator.previous());
        }

        if (op.equals("&&") || op.equals("||")) {
          Label endLabel = new Label();
          Label shortCircuit = new Label();

          mv.visitJumpInsn(op.equals("&&") ? IFEQ : IFNE, shortCircuit);
          generateValue((MainNode) right, mv, isConstructor);
          mv.visitJumpInsn(GOTO, endLabel);

          mv.visitLabel(shortCircuit);
          mv.visitInsn(op.equals("&&") ? ICONST_0 : ICONST_1);
          mv.visitLabel(endLabel);

          mv.visitVarInsn(ISTORE, allocator.allocate());
          return new Table("temp", "Z", false, false, false, null, allocator.previous());
        }
        throw new OperatorError("Opérateur binaire inconnu: " + op);
      }

      case "Expression" -> {
        return generate((MainNode) expressionNode, mv, isConstructor);
      }
      case "FieldAccess" -> {
        MainNode mainNode = (MainNode) expressionNode;
        String recordName = "";
        if ((mainNode.getChildrenList().getFirst().accept(new ExtractMainNodeVisitor())) != null) {
          MainNode expression = (MainNode) mainNode.getChildrenList().getFirst();
          recordName = ((GenericNode<?>) expression.getChildren().getFirst()).getValue();
        } else {
          recordName = ((GenericNode<?>) mainNode.getChildren().getFirst()).getValue();
        }
        Table table1 = Utils.searchIdentifier(mv, table, recordName, true, false);
        System.out.println(table1.index);
        String record = table1.descriptor.substring(1, table1.descriptor.length() - 1);
        Table table2 = Utils.searchIdentifier(mv, table, record, false, false);
        System.out.println(table2.index);
        //Utils.searchIdentifier(mv, table, recordName, true, false);

        String attName = ((GenericNode<?>) ((MainNode) expressionNode).getChildrenList()
            .get(2)).getValue();
        if (table2.getRecordAtt().containsKey(attName)) {
          mv.visitFieldInsn(GETFIELD, record, attName,
              DescriptorUtils.getTypeFromString(table2.getRecordAtt().get(attName))
                  .getDescriptor());
          int index = allocator.allocate();
          Utils.execute("store", mv, new SimpleEntry<>("", table2.getRecordAtt().get(attName)),
              index);
          Utils.execute("load", mv, new SimpleEntry<>("", table2.getRecordAtt().get(attName)),
              index);
          return new Table("unk",
              DescriptorUtils.getTypeFromString(table2.getRecordAtt().get(attName)).getDescriptor(),
              false, false, false, null, index);
        }
      }

      case "UnaryExpression" -> {
        LinkedList<ASTNode> children = ((MainNode) expressionNode).getChildrenList();
        GenericNode<?> operator = (GenericNode<?>) children.get(0);
        ASTNode right = children.get(1);
        Table type = generateValue((MainNode) right, mv, isConstructor);
        switch (operator.getValue()) {
          case "!" -> {
            Label trueLabel = new Label();
            Label endLabel = new Label();
            mv.visitJumpInsn(IFEQ, trueLabel);
            mv.visitInsn(ICONST_0);
            mv.visitJumpInsn(GOTO, endLabel);
            mv.visitLabel(trueLabel);
            mv.visitInsn(ICONST_1);
            mv.visitLabel(endLabel);
          }
          case "-" -> {
            if (type.descriptor.equals("I")) {
              mv.visitInsn(INEG);
              mv.visitVarInsn(ISTORE, allocator.allocate());
              return new Table("temp", "I", false, false, false, null, allocator.previous());
            } else if (type.descriptor.equals("F")) {
              mv.visitInsn(FNEG);
              mv.visitVarInsn(FSTORE, allocator.allocate());
              return new Table("temp", "F", false, false, false, null, allocator.previous());
            } else {
              throw new OperatorError(
                  "L'unary minus n'est pas applicable au type : " + type.descriptor);
            }
          }
          default -> throw new OperatorError("Opérateur unaire inconnu: " + operator.getValue());
        }
        mv.visitVarInsn(ISTORE, allocator.allocate());
        return new Table("temp", "Z", false, false, false, null, allocator.previous());
      }
    }
    return null;
  }

  public Table generateValue(MainNode node, MethodVisitor mv, Boolean isConstructor)
      throws OperatorError {
    for (ASTNode child : node.getChildrenList()) {
      MainNode main = child.accept(new ExtractMainNodeVisitor());
      if (node.getName().equals("FieldAccess")) {
        String recordName = ((GenericNode<?>) main.getChildren().getFirst()).getValue();
        Table table1 = Utils.searchIdentifier(mv, table, recordName, true, false);
        System.out.println(table1.index);
        Table table2 = Utils.searchIdentifier(mv, table, table1.type, false, false);
        System.out.println(table2.index);
        //Utils.searchIdentifier(mv, table, recordName, true, false);

        String attName = ((GenericNode<?>) node.getChildrenList().get(2)).getValue();
        if (table2.getRecordAtt().containsKey(attName)) {
          mv.visitFieldInsn(GETFIELD, table1.type, attName,
              DescriptorUtils.getTypeFromString(table2.getRecordAtt().get(attName))
                  .getDescriptor());
          int index = allocator.allocate();
          Utils.execute("store", mv, new SimpleEntry<>("", table2.getRecordAtt().get(attName)),
              index);
          Utils.execute("load", mv, new SimpleEntry<>("", table2.getRecordAtt().get(attName)),
              index);
          return new Table("unk",
              DescriptorUtils.getTypeFromString(table2.getRecordAtt().get(attName)).getDescriptor(),
              false, false, false, null, index);
        }
      }
      if (main != null) {
        Table result = checkExpression(node, mv, isConstructor);
        Utils.execute("load", mv, new SimpleEntry<>("", result.descriptor), result.index);
        //return result.descriptor;
        return result;
      } else {
        GenericNode<?> generic = child.accept(new ExtractGenericVisitor());
        String name = generic.getName();
        String value = String.valueOf(generic.getValue());
        if (name.equals("Literal")) {
          name = new Literal().typeOfLiteral(generic.getValue());
        }
        return switch (name) {
          case "BuiltInFunction" -> {
            BuiltInFunctionCall builtInFunctionCall = new BuiltInFunctionCall(table, null,
                allocator, start, end);
            Table table1 = builtInFunctionCall.CheckBuiltInFunctionCall(mv, node, false);
            Utils.execute("load", mv, new SimpleEntry<>("", table1.descriptor), table1.index);
            //yield "I";
            yield table1;
          }
          case "string" -> {
            mv.visitLdcInsn(value.replace("\"", ""));
            //yield "I";
            Utils.execute("store", mv, new SimpleEntry<>("", "Ljava/lang/String;"),
                allocator.allocate());
            Utils.execute("load", mv, new SimpleEntry<>("", "Ljava/lang/String;"),
                allocator.previous());

            yield new Table("unk", "Ljava/lang/String;", false, false, false, null,
                allocator.previous());
          }
          case "int" -> {
            int v = Integer.parseInt(value);
            mv.visitLdcInsn(v);
            //yield "I";
            Utils.execute("store", mv, new SimpleEntry<>("", "int"), allocator.allocate());
            Utils.execute("load", mv, new SimpleEntry<>("", "int"), allocator.previous());

            yield new Table("unk", "I", false, false, false, null, allocator.previous());
          }
          case "float" -> {
            float f = Float.parseFloat(value);
            mv.visitLdcInsn(f);
            Utils.execute("store", mv, new SimpleEntry<>("", "float"), allocator.allocate());
            Utils.execute("load", mv, new SimpleEntry<>("", "float"), allocator.previous());

            //yield "F";
            yield new Table("unk", "F", false, false, false, null, allocator.previous());

          }
          case "boolean" -> {
            mv.visitInsn(value.equals("true") ? ICONST_1 : ICONST_0);
            Utils.execute("store", mv, new SimpleEntry<>("", "bool"), allocator.allocate());
            Utils.execute("load", mv, new SimpleEntry<>("", "bool"), allocator.previous());

            //yield "Z";
            yield new Table("unk", "Z", false, false, false, null, allocator.previous());

          }
          case "Identifier" -> {
            Table t = Utils.searchIdentifier(mv, table, value, true, false);
            if (t == null) {
              throw new IllegalStateException("Variable non trouvée : " + value);
            }
           /* if (t.descriptor.equals("I")) {
              mv.visitVarInsn(ILOAD, t.index);
            } else if (t.descriptor.equals("F")) {
              mv.visitVarInsn(FLOAD, t.index);
            } else {
              mv.visitVarInsn(ILOAD, t.index); // Z
            }*/
            /*yield t.descriptor;*/
            yield t;
          }
          default -> throw new OperatorError("Littéral ou identifiant inconnu : " + name);
        };
      }
    }
    return null;
  }

  public Table generate(MainNode node, MethodVisitor mv, Boolean isConstructor)
      throws OperatorError {
    for (ASTNode child : node.getChildrenList()) {
      MainNode main = child.accept(new ExtractMainNodeVisitor());
      if (main != null) {
        return checkExpression(main, mv, isConstructor);
      } else {
        GenericNode<?> genericNode = (GenericNode<?>) child;
        if (child.getName().equals("Identifier")) {
          return Utils.searchIdentifier(mv, table, genericNode.getValue(), true, isConstructor);
        }
        if (child.getName().equals("Literal")) {
          if (booleanValues.contains(genericNode.getValue())) {
            int bIndex = allocator.allocate();
            if (genericNode.getValue().equals("true") || genericNode.getValue().equals("1")) {
              mv.visitInsn(Opcodes.ICONST_1);
            } else {
              mv.visitInsn(Opcodes.ICONST_0);
            }
            mv.visitVarInsn(Opcodes.ISTORE, bIndex);
            return new Table("true", DescriptorUtils.getTypeFromString("bool").getDescriptor(),
                false, false, false, null, bIndex);
          }
          Utils.loadLitField(mv,
              new SimpleEntry<>("", new Literal().typeOfLiteral(genericNode.getValue())),
              genericNode.getValue());
          Utils.execute("store", mv,
              new SimpleEntry<>("", new Literal().typeOfLiteral(genericNode.getValue())),
              allocator.allocate());
          return new Table("lit", DescriptorUtils.getTypeFromString(
              new Literal().typeOfLiteral(((GenericNode<?>) child).getValue())).getDescriptor(),
              false, false, false, null, allocator.previous());
        }
      }

    }
    return null;
  }

  private void promoteToFloat(Table type, MethodVisitor mv) {
    Utils.execute("load", mv, new SimpleEntry<>("", type.descriptor), type.index);
    if (type.descriptor.equals("I")) {
      mv.visitInsn(I2F);
      Utils.execute("store", mv, new SimpleEntry<>("", "float"), allocator.allocate());
      Utils.execute("load", mv, new SimpleEntry<>("", "float"), allocator.previous());

    }
  }

  private void handleComparison(String op, Label trueLabel, MethodVisitor mv, boolean isFloat) {
    switch (op) {
      case "==" -> mv.visitJumpInsn(IFEQ, trueLabel);
      case "!=" -> mv.visitJumpInsn(IFNE, trueLabel);
      case "<" -> mv.visitJumpInsn(IFLT, trueLabel);
      case "<=" -> mv.visitJumpInsn(IFLE, trueLabel);
      case ">" -> mv.visitJumpInsn(IFGT, trueLabel);
      case ">=" -> mv.visitJumpInsn(IFGE, trueLabel);
    }
  }

  private int getJumpOpcode(String op) {
    return switch (op) {
      case "==" -> IF_ICMPEQ;
      case "!=" -> IF_ICMPNE;
      case "<" -> IF_ICMPLT;
      case "<=" -> IF_ICMPLE;
      case ">" -> IF_ICMPGT;
      case ">=" -> IF_ICMPGE;
      default -> throw new IllegalArgumentException("Invalid comparator: " + op);
    };
  }
}
