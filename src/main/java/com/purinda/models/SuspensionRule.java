package com.purinda.models;

public class SuspensionRule {
    private int underageHierId;
    private String rule;

    public SuspensionRule(int underageHierId, String rule) {
        this.underageHierId = underageHierId;
        this.rule = rule;
    }

    public String toString() {
        return "SuspensionRule: [ID: " + underageHierId + ", Rule: " + rule + "]";
    }
}
