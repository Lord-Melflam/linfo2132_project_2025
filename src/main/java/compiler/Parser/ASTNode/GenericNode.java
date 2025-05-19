package compiler.Parser.ASTNode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import compiler.Exceptions.Semantic.OperatorError;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.ASTVisitor;

@JsonTypeName("GenericNode")
public class GenericNode<T> extends ASTNode {

  private String name;
  private String value;
  @JsonIgnore
  private int line;

  public GenericNode(String name, String value, int line) {
    this.name = name;
    this.value = value;
    this.line = line;
  }

  public GenericNode() {
    this.name = "";
    this.value = "";
    this.line = -1;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public int getLine() {
    return line;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "<" + name + "," + value + ">";
  }

  @Override
  public void acceptAST(ASTVisitor visitor) {

  }

  // GenericNode.java
  @Override
  public <T> T accept(ASTVisitor<T> visitor) throws OperatorError {
    return visitor.visitGenericNode(this);
  }

  @Override
  public ASTNode deepCopy() {
    return new GenericNode<>(this.name = name, this.value, this.line);
  }
}


