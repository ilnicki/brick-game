package me.ilnicki.bg.core.system;

import me.ilnicki.bg.core.data.DataBean;

public class SystemConfig implements DataBean {
  private String workingPath = ".";

  public String getWorkingPath() {
    return workingPath;
  }

  public void setWorkingPath(String workingPath) {
    this.workingPath = workingPath;
  }
}
