package compiler.Parser.ASTNode;

import com.fasterxml.jackson.annotation.JsonTypeName;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;

@JsonTypeName("GenericNode")
public class GenericNode<T> extends ASTNode {

  private String name;
  private String value;

  public GenericNode(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public GenericNode() {
    this.name = "";
    this.value = "";
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "<" + name + "," + value + ">";
  }

  @Override
  public void accept(ASTVisitor visitor) {

  }
}
