package com.qa.iFramework.Entity;

/**
 * Created by haijia on 12/1/16.
 */
public class RunStatus {
    /// <summary> Cases to run </summary>
    public int getTotal() {
        return Passed + Failed + Blocked + Skipped;
    }
    /// <summary> Cases executed </summary>
    public int getRun() {
        return Passed + Failed;
    }

    /// <summary> Cases passed </summary>
    public int Passed;
    /// <summary> Cases failed </summary>
    public int Failed;
    /// <summary> Cases blocked </summary>
    public int Blocked;
    /// <summary> Cases skipped </summary>
    public int Skipped;

    /// <summary> Initialize the test result summary count</summary>
    public RunStatus(int passed, int failed, int blocked, int skipped)
    {
        Passed = passed;
        Failed = failed;
        Blocked = blocked;
        Skipped = skipped;
    }

}
