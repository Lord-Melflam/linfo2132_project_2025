package compiler.Parser.ASTNode;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import compiler.Parser.Utils.Interfaces.ASTVisitor;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ASTNodeProcessor implements ASTVisitor {

  @JsonProperty("root")
  private RootNode root;

  public ASTNodeProcessor() {
    this.root = new RootNode();
  }

  public RootNode getRoot() {
    return root;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ASTNodeProcessor that = (ASTNodeProcessor) o;
    return Objects.equals(getRoot(), that.getRoot());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getRoot());
  }

  @Override
  public void visit(MainNode constantNode) {
    root.addNode(constantNode);
  }

  @Override
  public Object visitMainNode(MainNode node) {
    return null;
  }

  @Override
  public Object visitGenericNode(GenericNode node) {
    return null;
  }
}
