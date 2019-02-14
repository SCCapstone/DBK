package edu.sc.dbkdrymatic.internal;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Relation;

import java.util.Set;

public class Job {
  @Relation(parentColumn = "id", entityColumn = "jobId", entity = BoostBox.class)
  private Set<BoostBox> boxes;

  @Embedded
  private SiteInfo siteInfo;

  public Job() {}
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

  public Set<BoostBox> getBoxes() {
    return this.boxes;
  }

  public void setBoxes(Set<BoostBox> boxes) {
    this.boxes = boxes;
  }

  public SiteInfo getSiteInfo() {
    return this.siteInfo;
  }

  public void setSiteInfo(SiteInfo siteInfo) {
    this.siteInfo = siteInfo;
  }
}
