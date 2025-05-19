package compiler.CodeGeneration;

import java.util.HashMap;

public class Table {

  private String className;
  public String name;
  public String descriptor;
  public boolean isField;
  public boolean isStatic;
  public boolean isFunction;
  public String descriptorFunction;
  public String type;
  public int index;
  private HashMap<String, String> recordAtt = new HashMap<>();
  private final HashMap<String, Table> symbols = new HashMap<>();
  private Table parent;
  private boolean initialised;

  public Table(String name, String descriptor, boolean isField, boolean isStatic,
      boolean isFunction, String descriptorFunction,
      int index, HashMap<String, String> recordAtt) {
    this.recordAtt = recordAtt;
    this.name = name;
    this.descriptor = descriptor;
    this.isField = isField;
    this.isStatic = isStatic;
    this.index = index;
    this.isFunction = isFunction;
    this.descriptorFunction = descriptorFunction;
    this.initialised = true;
  }

  public Table(String name, String descriptor, boolean isField, boolean isStatic,
      boolean isFunction, String descriptorFunction,
      int index) {
    this.name = name;
    this.descriptor = descriptor;
    this.isField = isField;
    this.isStatic = isStatic;
    this.index = index;
    this.isFunction = isFunction;
    this.descriptorFunction = descriptorFunction;
    this.initialised = true;
  }

  public boolean isInitialised() {
    return initialised;
  }

  public void setInitialised(boolean initialised) {
    this.initialised = initialised;
  }

  public HashMap<String, String> getRecordAtt() {
    return recordAtt;
  }

  public String getClassName() {
    return className;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Table(Table parent, String className) {
    this.parent = parent;
    this.className = className;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public Table resolve(String name) {
    Table s = symbols.get(name);
    if (s != null) {
      return s;
    }
    if (parent != null) {
      return parent.resolve(name);
    }
    return null;
  }

  public HashMap<String, Table> getSymbols() {
    return symbols;
  }

  public Table getParent() {
    return parent;
  }
}

