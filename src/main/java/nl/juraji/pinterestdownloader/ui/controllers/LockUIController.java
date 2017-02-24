package nl.juraji.pinterestdownloader.ui.controllers;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class LockUIController {
  private final Set<JComponent> components;

  public LockUIController() {
    components = new HashSet<>();
  }

  public void addComponent(JComponent component) {
    if (component == null) throw new IllegalArgumentException("Component can not be null");
    components.add(component);
  }

  public void lock() {
    SwingUtilities.invokeLater(() -> components.forEach(component -> component.setEnabled(false)));
  }

  public void unlock() {
    SwingUtilities.invokeLater(() -> components.forEach(component -> component.setEnabled(true)));
  }
}
