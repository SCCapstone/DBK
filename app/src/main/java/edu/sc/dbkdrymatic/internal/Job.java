package edu.sc.dbkdrymatic.internal;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class Job {
  @Relation(parentColumn = "id", entityColumn = "jobId", entity = BoostBox.class)
  private List<BoostBox> boxes;

  @Embedded
  private SiteInfo siteInfo;

  public Job() {}
  public Job(List<BoostBox> boxes, SiteInfo siteInfo) {
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

  public List<BoostBox> getBoxes() {
    return this.boxes;
  }

  public void setBoxes(List<BoostBox> boxes) {
    this.boxes = boxes;
  }

  public SiteInfo getSiteInfo() {
    return this.siteInfo;
  }

  public void setSiteInfo(SiteInfo siteInfo) {
    this.siteInfo = siteInfo;
  }
}
