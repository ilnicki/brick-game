package me.ilnicki.bg.engine.system;

import me.ilnicki.bg.engine.data.DataBean;

public class SystemConfig implements DataBean {
    private String workingPath = ".";

    public String getWorkingPath() {
        return workingPath;
    }

    public void setWorkingPath(String workingPath) {
        this.workingPath = workingPath;
    }
}
