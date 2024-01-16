package com.purinda.models;

import com.purinda.definitions.Rule.RuleType;

public class SuspensionRule {
    private int underageHierId;
    private RuleType rule;

    public SuspensionRule(int underageHierId, RuleType rule) {
        this.underageHierId = underageHierId;
        this.rule = rule;
    }

    public String toString() {
        return "SuspensionRule: [ID: " + underageHierId + ", Rule: " + rule + "]";
    }
}
