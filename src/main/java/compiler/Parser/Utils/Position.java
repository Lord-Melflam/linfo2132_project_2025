package compiler.Parser.Utils;

import compiler.Parser.Utils.Interface.Observer;
import compiler.Parser.Utils.Interface.Subject;
import java.util.ArrayList;
import java.util.List;

public class Position implements Subject {

  private final List<Observer> observers = new ArrayList<>();

  private int savedPosition;

  public Position() {
    this.savedPosition = 0;
  }

  public void setSavedPosition(int savedPosition) {
    this.savedPosition = savedPosition;
    notifyObservers();
  }

  public int getSavedPosition() {
    return savedPosition;
  }

  public int add() {
    savedPosition++;
    notifyObservers();
    return getSavedPosition();
  }

  public int add(List<?> list) {
    savedPosition += list.size();
    notifyObservers();
    return getSavedPosition();
  }

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }
}
