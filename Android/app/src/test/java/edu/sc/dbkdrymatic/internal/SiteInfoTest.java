package edu.sc.dbkdrymatic.internal;

import org.jscience.physics.amount.Amount;
import org.junit.Test;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SiteInfoTest {
  @Test
  public void usaCalculationMatchesSpreadsheet() {
    // Set up a job site with the parameters provided in the spreadsheet:
    SiteInfo si = new SiteInfo(
        Amount.valueOf(12000, SiteInfo.CUBIC_FOOT),
        Amount.valueOf(80, NonSI.FAHRENHEIT),
        Amount.valueOf(105, NonSI.FAHRENHEIT),
        Amount.valueOf(60, NonSI.FAHRENHEIT),
        80,
        Damage.CLASS2,
        Country.USA,
        "Default Site"
    );

    assertEquals(5.1, si.getBoostBoxRequirement(), 0.05);
    assertEquals(2.0, si.getD2Requirement(), 0.05);
  }

  @Test
  public void cubicFootCorrectlySized() {
    assertEquals(
        28.31685,
        Amount.valueOf(1, SiteInfo.CUBIC_FOOT).doubleValue(NonSI.LITER),
        0.01);
  }

  @Test
  public void btuCorrectlySized() {
    assertEquals(
        1055.056,
        Amount.valueOf(1, SiteInfo.BTU).doubleValue(SI.JOULE),
        0.01);
  }
}