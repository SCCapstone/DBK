package edu.sc.dbkdrymatic.internal;

public enum Country {
  USA ("United States", 1.4),
  UK  ("United Kingdom", 2.05),
  AUS ("Australia", 1.8);

  private String name;
  private double kilowattRating;

  Country(String name, double kilowattRating) {
    this.name = name;
    this.kilowattRating = kilowattRating;
  }

  public double getKilowattRating() {
    return this.kilowattRating;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
