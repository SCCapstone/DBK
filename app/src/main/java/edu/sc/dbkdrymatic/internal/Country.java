package edu.sc.dbkdrymatic.internal;

/**
 * Enumeration of the different countries available for selection.
 *
 * <p>The different countries have different kilowatt ratings based on the voltage and frequency
 * of electricity delivery in those countries at the wall. These are national standards that have
 * been stored here as constant values and associated with the name of their country so that this
 * selection may be done transparently to the user.
 */
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
