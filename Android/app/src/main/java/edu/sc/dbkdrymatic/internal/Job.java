package edu.sc.dbkdrymatic.internal;

import java.util.Set;

public class Job {
  private Set<BoostBox> boxes;
  private SiteInfo siteInfo;

  public Job(Set<BoostBox> boxes, SiteInfo siteInfo) {
    this.boxes = boxes;
    this.siteInfo = siteInfo;
  }

  public void AddBox(BoostBox box) {
    boxes.add(box);
  }

  public void RemoveBox(BoostBox box) {
    boxes.remove(box);
  }

  public boolean HasBox(BoostBox box) {
    return boxes.contains(box);
  }

  public SiteInfo getSiteInfo() {
    return this.siteInfo;
  }
}
