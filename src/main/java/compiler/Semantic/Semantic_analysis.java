package compiler.Semantic;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.GenericError;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Parser;
import compiler.Semantic.Types.Type;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Semantic_analysis {

  private Parser parser;
  private MainNode astRoot;
  private HashMap<String, Type> table = new HashMap<>();
  public Type symbolTable;

  public Semantic_analysis(Parser parser)
      throws ParserException, NotASCIIException, IOException, UnrecognisedTokenException, TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    this.parser = parser;
    astRoot = parser.getMainNode();
    symbolTable = new Type("Program", "Root", null, "", null, table);
    printTree();
  }

  public Semantic_analysis(MainNode node)
      throws ParserException, NotASCIIException, IOException, UnrecognisedTokenException, TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    astRoot = node;
    symbolTable = new Type("Program", "Root", null, "", null, table);
    printTree();
  }

  public Semantic_analysis(List<MainNode> node)
      throws ParserException, NotASCIIException, IOException, UnrecognisedTokenException, TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    symbolTable = new Type("Program", "Root", null, "", null, table);
    CheckGlobalVisitor visitor = new CheckGlobalVisitor(symbolTable);
    for (MainNode child : node) {
      child.accept(visitor);
    }
  }

  public void printTree() throws TypeError, ArgumentError, GenericError, ScopeError, OperatorError {
    CheckGlobalVisitor visitor = new CheckGlobalVisitor(symbolTable);
    for (MainNode child : astRoot.getNodes()) {
      child.accept(visitor);
    }

    /*for (MainNode child : astRoot.getNodes()) {
      switch (child.getName()) {
        case "Constant" -> {
          CheckConstantType checkConstantType = new CheckConstantType();
          checkConstantType.checkConstant(child.getChildrenList(), symbolTable);
        }
      *//*  case "Record" -> {

        }*//*
     *//* case "Declaration" -> {

        }
        case "Function" -> {

        }
        case "Statements" -> {

        }*//*
        default -> {

        }
      }

    }
    System.out.println();*/
  }
/*
  public void printTree() throws TypeException, ArgumentError, GenericError, ScopeError {
    for (MainNode child : astRoot.getNodes()) {
      System.out.println(child.getName());
      */
/*LinkedList<GenericNode<?>> stack = new LinkedList<>();
      LinkedList<ASTNode> children = (LinkedList<ASTNode>) child.getChildren();
      CheckTypes utilsSemanticAnalysis = new CheckTypes(child.getName(), children);
      utilsSemanticAnalysis.checkType(symbolTable);*//*

    }
    System.out.println();
  }
*/

 /* private void printNode(ASTNode node, boolean isLast, String type,
      LinkedList<GenericNode<?>> stack) throws TypeException {
    if (node == null) {
      return;
    }
    *//*if (node instanceof GenericNode<?>) {
    } else*//*
    if (node instanceof MainNode mainNode) {
      LinkedList<ASTNode> children = (LinkedList<ASTNode>) mainNode.getChildren();
      CheckTypes utilsSemanticAnalysis = new CheckTypes(node.getName(), children);
*//*
      utilsSemanticAnalysis.checkType(table);
*//*
    }
  }*/

}
