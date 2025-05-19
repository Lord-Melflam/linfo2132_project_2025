package compiler.Semantic.Utils;

import compiler.Exceptions.Semantic.ScopeError;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Semantic.Types.Type;
import java.util.LinkedList;
import java.util.List;

public class Utils {

  public Type lookInSymbolsTable(Type symbolTable, String nodeValue, int line) throws ScopeError {
    Type temp = symbolTable;
    while (temp.getPrevious() != null) {
      if (temp.getCurrent().containsKey(nodeValue)) {
        return temp.getCurrent().get(nodeValue);
      }
      temp = temp.getPrevious();
    }
    if (temp.getCurrent().containsKey(nodeValue)) {
      return temp.getCurrent().get(nodeValue);
    }
    throw new ScopeError(nodeValue, line);
  }

  public void getSymbols(MainNode mainNode, LinkedList<Type> typeList, Type table) {
    for (ASTNode child : mainNode.getChildren()) {
      LinkedList<String> list = new LinkedList<>();
      getSymbolsImpl(child, true, list);
      if (list.size() == 2) {
        typeList.add(
            new Type("Fields", list.pop(), null, list.pop(), null, table.getCurrent()));
      }
    }
  }

  private void getSymbolsImpl(ASTNode node, boolean isLast, LinkedList<String> list) {
    if (node == null) {
      return;
    }

    if (node instanceof GenericNode<?>) {
      if (node.getName().equals("Identifier") || node.getName().equals("TypeSpecifier")
          || node.getName().equals("Record")) {
        list.add(((GenericNode<?>) node).getValue());
      }
    } else if (node instanceof MainNode mainNode) {
      List<? extends ASTNode> children = mainNode.getChildren();
      for (int i = 0; i < children.size(); i++) {
        getSymbolsImpl(children.get(i), i == children.size() - 1, list);
      }
    }
  }

}
