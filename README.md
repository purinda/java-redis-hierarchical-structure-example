# Java+Redis Example

## Introduction

This project demonstrates how to work with structured data in Redis using Java.


## Demo

![](./docs/demo.gif)


## Running the Project

The project includes a Dockerfile and a docker-compose.yml file for running the application and its dependencies (Redis, Java, Maven etc) in Docker containers.

To build and run the application in Docker, use the following commands:

    git clone https://github.com/purinda/java-redis-hierarchical-structure-example.git
    cd java-redis-hierarchical-structure-example

    docker-compose build
    docker-compose up -d

When the application finished running, you will see the output from the Java application in the logs using the command below

    docker-compose logs app

Sample output is attached below

    [16:30:22] purinda@achilles.local[~/src/java-redis-hierarchical-structure-example][master] docker-compose logs app
    app-1  | JRHS App Started
    app-1  |
    app-1  | Set Hierarchy References
    app-1  | Getting a single Hierarchy Reference: {ev_type_id=20, ev_class_id=10, status=A}
    app-1  | Getting all Hierarchy References: [HierarchyReference: [ID: 100, EvClassId: 10, EvTypeId: 10, Status: A], HierarchyReference: [ID: 101, EvClassId: 10, EvTypeId: 20, Status: A], HierarchyReference: [ID: 102, EvClassId: 202, EvTypeId: 302, Status: A]]
    app-1  |
    app-1  | Set Player Names
    app-1  | Getting a single Player Name: [PlayerName: [ID: 100, PlayerName: Will Smith], PlayerName: [ID: 100, PlayerName: Bob], PlayerName: [ID: 100, PlayerName: Alice], PlayerName: [ID: 100, PlayerName: Jane Doe]]
    app-1  | Getting all Player Names: [PlayerName: [ID: 100, PlayerName: Will Smith], PlayerName: [ID: 100, PlayerName: Bob], PlayerName: [ID: 100, PlayerName: Alice], PlayerName: [ID: 100, PlayerName: Jane Doe], PlayerName: [ID: 102, PlayerName: Charlie], PlayerName: [ID: 101, PlayerName: Sam Smith]]
    app-1  |
    app-1  | Set Suspension Rules
    app-1  | Getting a single Suspension Rule: SUSPEV
    app-1  | Getting all suspension rules: [SuspensionRule: [ID: 102, Rule: SUSPMKT], SuspensionRule: [ID: 100, Rule: SUSPEV], SuspensionRule: [ID: 101, Rule: SUSPMKT]]
    app-1  |
    app-1  | Process JSON blob below, store in Redis then reads it back
    app-1  | {
    app-1  |   "hierarchyReferences" : [ {
    app-1  |     "hierarchyRefId" : 100,
    app-1  |     "eventClassId" : 200,
    app-1  |     "eventTypeId" : 300
    app-1  |   }, {
    app-1  |     "hierarchyRefId" : 101,
    app-1  |     "eventClassId" : 200,
    app-1  |     "eventTypeId" : 301
    app-1  |   }, {
    app-1  |     "hierarchyRefId" : 102,
    app-1  |     "eventClassId" : 202,
    app-1  |     "eventTypeId" : 302
    app-1  |   } ],
    app-1  |   "suspensionRules" : [ {
    app-1  |     "hierarchyRefId" : 100,
    app-1  |     "ruleType" : "SUSPEV"
    app-1  |   }, {
    app-1  |     "hierarchyRefId" : 101,
    app-1  |     "ruleType" : "SUSPMKT"
    app-1  |   }, {
    app-1  |     "hierarchyRefId" : 102,
    app-1  |     "ruleType" : "SUSPMKT"
    app-1  |   } ],
    app-1  |   "playerNames" : [ {
    app-1  |     "hierarchyRefId" : 100,
    app-1  |     "name" : "Alice"
    app-1  |   }, {
    app-1  |     "hierarchyRefId" : 100,
    app-1  |     "name" : "Bob"
    app-1  |   }, {
    app-1  |     "hierarchyRefId" : 102,
    app-1  |     "name" : "Charlie"
    app-1  |   } ]
    app-1  | }
    app-1  | Getting all Hierarchy References: [HierarchyReference: [ID: 100, EvClassId: 200, EvTypeId: 300, Status: A], HierarchyReference: [ID: 101, EvClassId: 200, EvTypeId: 301, Status: A], HierarchyReference: [ID: 102, EvClassId: 202, EvTypeId: 302, Status: A]]
    app-1  | Getting all suspension rules: [SuspensionRule: [ID: 102, Rule: SUSPMKT], SuspensionRule: [ID: 100, Rule: SUSPEV], SuspensionRule: [ID: 101, Rule: SUSPMKT]]
    app-1  | Getting all Player Names: [PlayerName: [ID: 100, PlayerName: Will Smith], PlayerName: [ID: 100, PlayerName: Bob], PlayerName: [ID: 100, PlayerName: Alice], PlayerName: [ID: 100, PlayerName: Jane Doe], PlayerName: [ID: 102, PlayerName: Charlie], PlayerName: [ID: 101, PlayerName: Sam Smith]]

