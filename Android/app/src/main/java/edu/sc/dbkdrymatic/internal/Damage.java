package edu.sc.dbkdrymatic.internal;

public enum Damage {
  CLASS1 ("Class 1", 0.05),
  CLASS2 ("Class 2", 0.12),
  CLASS3 ("Class 3", 0.16),
  CLASS4 ("Class 4", 0.09);

  private String label;
  private double value;

  Damage(String label, double value) {
    this.label = label;
    this.value = value;
  }

  public String getLabel() {
    return this.label;
  }

  public double getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.getLabel();
  }
}
