# Storing Hierarchical Data in Redis

Lets use a common structure across all HUC application related redis keys. A suggested structure is as follows:

    <Application Name>:<Reference>:<Key>

### Hierarchy References
Taking above as a guide we can store hierarchy references as follows:

    UAGE:HierarchyReferences:{underage_hier_id}
    UAGE:HierarchyReferences:101

    Fields: ev_class_id, ev_type_id, status 

Example Record:

    UAGE:HierarchyReferences:101 
    Hash Values: ev_class_id: 10, ev_type_id: 20, status: 'A'

Setting Hierarchy References:

    HSET UAGE:HierarchyReferences:101 ev_class_id 10
    HSET UAGE:HierarchyReferences:101 ev_type_id 20
    [Optional] HSET UAGE:HierarchyReferences:101 status 'A' 

Getting Hierarchy References:

    HGET UAGE:HierarchyReferences:101 ev_class_id
    HGET UAGE:HierarchyReferences:101 ev_type_id
    HGET UAGE:HierarchyReferences:101 status

Deleting Hierarchy References:

    DEL UAGE:HierarchyReferences:101

Scanning all available hierarchy References:

    SCAN 0 MATCH UAGE:HierarchyReferences:*
    SCAN 0 MATCH UAGE:HierarchyReferences:101
    SCAN 0 MATCH UAGE:HierarchyReferences:101*

### Suspension Rules 

    UAGE:SuspensionRules:{underage_hier_id}
    UAGE:SuspensionRules:201

    Fields: underage_hier_id, rule
    Field Values: 
        - underage_hier_id: 101, rule: 'SUSPEV'
        - underage_hier_id: 102, rule: 'SUSPMKT'

Example Record:

    UAGE:SuspensionRules:201
    Hash Values: underage_hier_id: 101, rule: 'SUSPEV'

### Player Names Agaist Hierarchy

    UAGE:PlayerNames:{underage_hier_id}
    UAGE:PlayerNames:301

    Fields: underage_hier_id, player_name
    Field Values: 
        - underage_hier_id: 101, player_name: 'Player 1'
        - underage_hier_id: 102, player_name: 'Player 2'
        
