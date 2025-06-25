package com.miyu.cloud.mcs.dto.schedule.utils;

import ilog.concert.IloIntervalVar;

import java.util.ArrayList;

public class IntervalVarList extends ArrayList<IloIntervalVar> {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IloIntervalVar[] toArray() {
        return (IloIntervalVar[]) this.toArray(new IloIntervalVar[this.size()]);
    }
}
