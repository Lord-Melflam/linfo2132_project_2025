package compiler.Semantic.Types.Statement;

import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.ExtractMainNodeVisitor;
import compiler.Semantic.Types.Assignment.CheckAssignment;
import compiler.Semantic.Types.Constant.CheckConstantType;
import compiler.Semantic.Types.ControlStructure.CheckFor;
import compiler.Semantic.Types.ControlStructure.CheckIf;
import compiler.Semantic.Types.ControlStructure.CheckWhile;
import compiler.Semantic.Types.Function.CheckBuiltInFunction;
import compiler.Semantic.Types.Function.Expression;
import compiler.Semantic.Types.Function.Function;
import compiler.Semantic.Types.Type;
import java.util.LinkedList;
import java.util.List;

public class CheckStatement {

  public void checkStatement(List<ASTNode> functionBlock, Type functionTable)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    if (!functionBlock.isEmpty()) {
      printTree((MainNode) functionBlock.getFirst(), functionTable);
    }
  }

  public void printTree(MainNode mainNode, Type functionTable)
      throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    ExtractMainNodeVisitor extractMainNodeVisitor = new ExtractMainNodeVisitor();

    for (ASTNode child : mainNode.getChildren()) {
      LinkedList<String> list = new LinkedList<>();
      MainNode block = child.accept(extractMainNodeVisitor);
      if (block == null) {
        continue;
      }
      checkBlock(block, functionTable);
      /*switch (child.getName()) {
        case "Return" -> {
          CheckReturn checkReturn = new CheckReturn();
          checkReturn.checkReturn(block, functionTable);
        }
        case "Declaration", "Initialisation"*//*, "Assignment", "Expression"*//* -> {
          CheckConstantType checkConstantType = new CheckConstantType();
          checkConstantType.check(block.getChildrenList(), functionTable,
              child.getName());
        }
        case "FunctionCall", "Expression" -> {
          assert mainNode != null;
          if (child.getName().equals("Expression")) {
            Expression expression = new Expression();
            expression.checkFunction(block, functionTable);
          } else if (child.getName().equals("FunctionCall")) {
            Function function = new Function();
            function.checkFunction(block, functionTable);
          }
        }
        case "BuiltInFunctionCall" -> {
          CheckBuiltInFunction checkBuiltInFunction = new CheckBuiltInFunction();
          checkBuiltInFunction.checkBuiltInFunction((LinkedList<ASTNode>) block.getChildren(),
              functionTable);
        }
        case "IfControlStructure" -> {
          CheckIf checkIf = new CheckIf();
          checkIf.check(block, functionTable);
        }
      }
      //printNode(child, true, functionTable);
    }*/
    }
  }

  public void checkBlock(MainNode mainNode, Type functionTable) throws OperatorError {
    switch (mainNode.getName()) {
      case "Assignment" -> {
        CheckAssignment checkAssignment = new CheckAssignment();
        checkAssignment.checkArrayAssignment(mainNode, functionTable);
      }
      case "RecordAssignment" -> {
        CheckAssignment checkAssignment = new CheckAssignment();
        checkAssignment.checkAssignment(mainNode, functionTable);
      }
      case "Return" -> {
        CheckReturn checkReturn = new CheckReturn();
        checkReturn.checkReturn(mainNode, functionTable);
      }
      case "Declaration", "Initialisation"/*, "Assignment", "Expression"*/ -> {
        CheckConstantType checkConstantType = new CheckConstantType();
        checkConstantType.check(mainNode.getChildrenList(), functionTable,
            mainNode.getName());
      }
      case "FunctionCall", "Expression" -> {
        if (mainNode.getName().equals("Expression")) {
          Expression expression = new Expression();
          expression.checkFunction(mainNode, functionTable);
        } else if (mainNode.getName().equals("FunctionCall")) {
          Function function = new Function();
          function.checkFunction(mainNode, functionTable);
        }
      }
      case "BuiltInFunctionCall" -> {
        CheckBuiltInFunction checkBuiltInFunction = new CheckBuiltInFunction();
        checkBuiltInFunction.checkBuiltInFunction((LinkedList<ASTNode>) mainNode.getChildren(),
            functionTable);
      }
      case "IfControlStructure" -> {
        CheckIf checkIf = new CheckIf();
        checkIf.check(mainNode, functionTable);
      }
      case "ForControlStructure" -> {
        CheckFor checkFor = new CheckFor();
        checkFor.checkFor(mainNode, functionTable);
      }
      case "WhileControlStructure" -> {
        CheckWhile checkWhile = new CheckWhile();
        checkWhile.checkWhile(mainNode, functionTable);
      }
    }
  }
/*
  private void printNode(ASTNode node, boolean isLast, Type functionTable)
      throws TypeError, ArgumentError, GenericError, ScopeError {
    if (node == null) {
      return;
    }

    if (node instanceof GenericNode<?>) {

    } else if (node instanceof MainNode mainNode) {
      LinkedList<ASTNode> children = (LinkedList<ASTNode>) mainNode.getChildren();

      for (int i = 0; i < children.size(); i++) {
        switch (mainNode.getName()) {
          case "Return" -> {
            CheckReturn checkReturn = new CheckReturn();
            checkReturn.checkReturn((MainNode) children.get(i), functionTable);
          }
          case "Declaration" -> {
            CheckConstantType checkConstantType = new CheckConstantType();
            //checkConstantType.checkConstant(children, functionTable);
          }
          case "FunctionCall" -> {
            *//*todo*//*
            System.out.println("eeehedxreydxfdxfgdxfgdxdgfxtdxfxerttdt");
          }
          case "BuiltInFunctionCall" -> {
            *//*todo*//*
            System.out.println("eeehedxreydxfdxfgdxfgdxdgfxtdxfxerttdt");
          }
          default -> {
            throw new IllegalStateException("jfyfftytyytcycgycyttycytctyctyctycyt");
          }
        }
      }
    }
  }*/
}
