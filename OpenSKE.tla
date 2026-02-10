------------------------------ MODULE OpenSKE ------------------------------
(* TLA+ Specification for Open Security Knowledge Engine (OpenSKE)
 * 
 * This specification models the security analysis framework including:
 * - Network topology and infrastructure components
 * - Software vulnerabilities and weaknesses
 * - User accounts and access permissions
 * - Attack patterns and security state transitions
 * - Risk propagation through dependencies and connections
 *)

EXTENDS Integers, Sequences, FiniteSets, TLC

(* ==================== CONSTANTS ==================== *)

CONSTANTS
    (* Security states *)
    UNKNOWN, SAFE, RISKY, COMPROMISED,
    
    (* Account states *)
    ACTIVE, EXPIRED, LOCKED,
    
    (* Asset types *)
    FILE, DATABASE, SERVICE,
    
    (* Access types *)
    NONE, READ, READ_WRITE, READ_EXECUTE, READ_WRITE_EXECUTE,
    
    (* Weakness types (sample CWE IDs) *)
    CWE_521,  (* Weak credentials *)
    CWE_89,   (* SQL injection *)
    CWE_79,   (* Cross-site scripting *)
    CWE_400,  (* Resource exhaustion *)
    CWE_200,  (* Information exposure *)
    
    (* Attack patterns (sample CAPEC IDs) *)
    CAPEC_1,  (* Exploiting Trust in Client Code *)
    CAPEC_2,  (* Inducing Account Lockout *)
    CAPEC_7,  (* Blind SQL Injection *)
    CAPEC_16, (* Dictionary-based Password Attack *)
    
    (* Maximum network size for verification *)
    MaxHosts, MaxSoftware, MaxUsers, MaxAccounts, MaxAssets

(* ==================== TYPE DEFINITIONS ==================== *)

Host == [address : STRING, connections : SUBSET Host, software : SUBSET Software, assets : SUBSET Asset]

Software == [host : Host, cpeId : STRING, hasVulnerability : BOOLEAN, weaknesses : SUBSET WEAKNESS,
              accounts : SUBSET UserAccount, assetAccesses : SUBSET AssetAccess, dependencies : SUBSET Software,
              securityState : {UNKNOWN, SAFE, RISKY, COMPROMISED}]

UserAccount == [username : STRING, software : Software, groups : SUBSET UserGroup, 
                state : {ACTIVE, EXPIRED, LOCKED}, assets : SUBSET Asset]

UserGroup == [name : STRING, software : Software, accounts : SUBSET UserAccount]

User == [fullName : STRING, email : STRING, host : Host, isAttacker : BOOLEAN,
         accounts : SUBSET UserAccount, assetAccesses : SUBSET AssetAccess]

Asset == [name : STRING, host : Host, type : {FILE, DATABASE, SERVICE},
          securityState : {UNKNOWN, SAFE, RISKY, COMPROMISED}]

AssetAccess == [asset : Asset, accessor : ASSET_ACCESSOR, accessType : {NONE, READ, READ_WRITE, READ_EXECUTE, READ_WRITE_EXECUTE}]

AttackPattern == [capecId : STRING, weakness : WEAKNESS, state : {READY, ACTIVE, SUCCESSFUL, FAILED}]

WEAKNESS == [identifier : STRING, software : Software]
ASSET_ACCESSOR == User \cup UserAccount \cup UserGroup \cup Software

(* ==================== VARIABLES ==================== *)

VARIABLES
    hosts,          (* Set of all hosts in the infrastructure *)
    softwares,      (* Set of all software instances *)
    users,          (* Set of all users *)
    userAccounts,   (* Set of all user accounts *)
    userGroups,     (* Set of all user groups *)
    assets,         (* Set of all assets *)
    assetAccesses,  (* Set of all asset access permissions *)
    attackPatterns, (* Set of known attack patterns *)
    step            (* Counter for debugging/traceability *)

(* ==================== HELPER OPERATORS ==================== *)

(* Network reachability - can source host reach target host *)
CanReach(source, target) == 
    \/ source = target
    \/ \E h \in source.connections : CanReach(h, target)

(* HasSoftwareWithWeakness - check if host runs software with specific weakness *)
HasSoftwareWithWeakness(host, weakness) ==
    \E s \in softwares : /\ s.host = host
                         /\ weakness \in s.weaknesses

(* GetActiveAccountsOnSoftware - get active accounts on specific software *)
GetActiveAccountsOnSoftware(software) ==
    {ua \in userAccounts : /\ ua.software = software /\ ua.state = ACTIVE}

(* CanUserAccessSoftware - check if user can access software through accounts *)
CanUserAccessSoftware(user, software) ==
    \E ua \in userAccounts : /\ ua \in user.accounts
                           /\ ua.software = software
                           /\ ua.state = ACTIVE

(* AssetsAccessibleByUser - get assets user can access *)
AssetsAccessibleByUser(user) ==
    {a \in assets : \E aa \in assetAccesses : /\ aa.asset = a /\ aa.accessor \in {user} \cup user.accounts}

(* AssetsAccessibleByAccount - get assets account can access *)
AssetsAccessibleByAccount(account) ==
    {a \in assets : \E aa \in assetAccesses : /\ aa.asset = a /\ aa.accessor = account}

(* GetAttackers - set of all attacker users *)
GetAttackers() == {u \in users : u.isAttacker = TRUE}

(* ==================== INITIALIZATION ==================== *)

Init ==
    /\ hosts = {}
    /\ softwares = {}
    /\ users = {}
    /\ userAccounts = {}
    /\ userGroups = {}
    /\ assets = {}
    /\ assetAccesses = {}
    /\ attackPatterns = {}
    /\ step = 0

(* ==================== STATE TRANSITIONS ==================== *)

(* AddHost - add a new host to the infrastructure *)
AddHost(host) ==
    /\ host \notin hosts
    /\ hosts' = hosts \cup {host}
    /\ UNCHANGED <<softwares, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns, step>>

(* AddConnection - establish network connection between hosts *)
AddConnection(sourceHost, targetHost) ==
    /\ sourceHost \in hosts /\ targetHost \in hosts
    /\ targetHost \notin sourceHost.connections
    /\ LET newSource == [sourceHost EXCEPT !.connections = @ \cup {targetHost}]
           newHosts == (hosts \ {sourceHost}) \cup {newSource}
       IN hosts' = newHosts
    /\ UNCHANGED <<softwares, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns, step>>

(* InstallSoftware - install software on a host *)
InstallSoftware(software) ==
    /\ software \notin softwares
    /\ software.host \in hosts
    /\ softwares' = softwares \cup {software}
    /\ UNCHANGED <<hosts, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns, step>>

(* AddVulnerability - mark software as vulnerable *)
AddVulnerability(software) ==
    /\ software \in softwares
    /\ LET vulnSoftware == [software EXCEPT !.hasVulnerability = TRUE, !.securityState = RISKY]
           newSoftwares == (softwares \ {software}) \cup {vulnSoftware}
       IN softwares' = newSoftwares
    /\ UNCHANGED <<hosts, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns, step>>

(* AddWeakness - add weakness to software *)
AddWeakness(software, weakness) ==
    /\ software \in softwares
    /\ weakness \notin software.weaknesses
    /\ LET weakSoftware == [software EXCEPT !.weaknesses = @ \cup {weakness}]
           newSoftwares == (softwares \ {software}) \cup {weakSoftware}
       IN softwares' = newSoftwares
    /\ UNCHANGED <<hosts, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns, step>>

(* CreateUserAccount - create new user account *)
CreateUserAccount(account) ==
    /\ account \notin userAccounts
    /\ account.software \in softwares
    /\ userAccounts' = userAccounts \cup {account}
    /\ UNCHANGED <<hosts, softwares, users, userGroups, assets, assetAccesses, attackPatterns, step>>

(* AddAssetAccess - grant access permissions *)
AddAssetAccess(access) ==
    /\ access \notin assetAccesses
    /\ access.asset \in assets
    /\ UNCHANGED <<hosts, softwares, users, userAccounts, userGroups, assets, attackPatterns, step>>

(* RiskPropagation - propagate risk through dependencies and connections *)
RiskPropagation ==
    \/ (* Risk from software vulnerabilities *)
       \E s \in softwares : 
           /\ \/ s.hasVulnerability = TRUE
              \/ \E dep \in s.dependencies : dep.securityState = RISKY
           /\ s.securityState /= RISKY
           /\ LET riskySoftware == [s EXCEPT !.securityState = RISKY]
                  newSoftwares == (softwares \ {s}) \cup {riskySoftware}
              IN softwares' = newSoftwares
           /\ UNCHANGED <<hosts, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns, step>>

    \/ (* Risk to assets on same host *)
       \E a \in assets, s \in softwares :
           /\ a.host = s.host
           /\ s.securityState = RISKY
           /\ a.securityState /= RISKY
           /\ LET riskyAsset == [a EXCEPT !.securityState = RISKY]
                  newAssets == (assets \ {a}) \cup {riskyAsset}
              IN assets' = newAssets
           /\ UNCHANGED <<hosts, softwares, users, userAccounts, userGroups, assetAccesses, attackPatterns, step>>

    \/ (* Risk to assets on connected hosts *)
       \E a \in assets, s \in softwares, h \in hosts :
           /\ a.host = h
           /\ s.securityState = RISKY
           /\ CanReach(s.host, h)
           /\ a.securityState /= RISKY
           /\ LET riskyAsset == [a EXCEPT !.securityState = RISKY]
                  newAssets == (assets \ {a}) \cup {riskyAsset}
              IN assets' = newAssets
           /\ UNCHANGED <<hosts, softwares, users, userAccounts, userGroups, assetAccesses, attackPatterns, step>>

(* AttackExecution - simulate various attack patterns *)
AttackExecution ==
    \/ (* Account Lockout Attack - CAPEC-2 *)
       \E attacker \in GetAttackers(), software \in softwares, weakness \in {CWE_400}, account \in userAccounts :
           /\ CanReach(attacker.host, software.host)
           /\ HasSoftwareWithWeakness(software.host, weakness)
           /\ account.software = software
           /\ account.state = ACTIVE
           /\ LET lockedAccount == [account EXCEPT !.state = LOCKED]
                  newAccounts == (userAccounts \ {account}) \cup {lockedAccount}
              IN userAccounts' = newAccounts
           /\ UNCHANGED <<hosts, softwares, users, userGroups, assets, assetAccesses, attackPatterns, step>>

    \/ (* SQL Injection Attack - CAPEC-7 *)
       \E attacker \in GetAttackers(), software \in softwares, weakness \in {CWE_89}, asset \in assets :
           /\ CanReach(attacker.host, software.host)
           /\ HasSoftwareWithWeakness(software.host, weakness)
           /\ asset.type = DATABASE
           /\ asset \notin AssetsAccessibleByUser(attacker)
           /\ LET newAccess == [asset |-> asset, accessor |-> attacker, accessType |-> READ]
                  newAssetAccesses == assetAccesses \cup {newAccess}
              IN assetAccesses' = newAssetAccesses
           /\ UNCHANGED <<hosts, softwares, users, userAccounts, userGroups, assets, attackPatterns, step>>

    \/ (* Dictionary Attack - CAPEC-16 *)
       \E attacker \in GetAttackers(), software \in softwares, weakness \in {CWE_521} :
           /\ CanReach(attacker.host, software.host)
           /\ HasSoftwareWithWeakness(software.host, weakness)
           /\ \A ua \in GetActiveAccountsOnSoftware(software) : ua \notin attacker.accounts
           /\ LET newAccount == [username |-> "hacked_" \魫 ToString(step), software |-> software, 
                                 groups |-> {}, state |-> ACTIVE, assets |-> {}]
                  newAccounts == userAccounts \cup {newAccount}
                  newAttacker == [attacker EXCEPT !.accounts = @ \cup {newAccount}]
                  newUsers == (users \ {attacker}) \cup {newAttacker}
              IN /\ userAccounts' = newAccounts
                 /\ users' = newUsers
           /\ UNCHANGED <<hosts, softwares, userGroups, assets, assetAccesses, attackPatterns, step>>

    \/ (* ACL Bypass Attack - CAPEC-1 *)
       \E attacker \in GetAttackers(), software \in softwares, weakness \in {CWE_200} :
           /\ CanReach(attacker.host, software.host)
           /\ HasSoftwareWithWeakness(software.host, weakness)
           /\ CanUserAccessSoftware(attacker, software)
           /\ (* Unauthorized access is logged but doesn't change state in this simplified model *)
           /\ UNCHANGED <<hosts, softwares, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns, step>>

(* ProgressStep - increment step counter for debugging *)
ProgressStep ==
    /\ step' = step + 1
    /\ UNCHANGED <<hosts, softwares, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns>>

(* ==================== NEXT STATE RELATION ==================== *)

Next ==
    \/ AddHost(h) for some h not in hosts
    \/ AddConnection(h1, h2) for some h1, h2 in hosts
    \/ InstallSoftware(s) for some s not in softwares
    \/ AddVulnerability(s) for some s in softwares
    \/ AddWeakness(s, w) for some s in softwares, w not in s.weaknesses
    \/ CreateUserAccount(acc) for some acc not in userAccounts
    \/ AddAssetAccess(acc) for some acc not in assetAccesses
    \/ RiskPropagation
    \/ AttackExecution
    \/ ProgressStep

(* ==================== TYPE CORRECTNESS ==================== *)

TypeInvariant ==
    /\ hosts \subseteq SUBSET Host
    /\ softwares \subseteq Software
    /\ users \subseteq User
    /\ userAccounts \subseteq UserAccount
    /\ userGroups \subseteq UserGroup
    /\ assets \subseteq Asset
    /\ assetAccesses \subseteq AssetAccess
    /\ attackPatterns \subseteq AttackPattern
    /\ step \in Nat

(* ==================== SECURITY PROPERTIES ==================== *)

(* NoUnknownStates - all components should have a defined security state *)
NoUnknownStates ==
    /\ \A s \in softwares : s.securityState /= UNKNOWN
    /\ \A a \in assets : a.securityState /= UNKNOWN

(* SecurityStateMonotonicity - security states only worsen, never improve *)
SecurityStateMonotonicity ==
    /\ \A s \in softwares : (s.securityState = SAFE) => ~(s'.securityState = UNKNOWN)
    /\ \A a \in assets : (a.securityState = SAFE) => ~(a'.securityState = UNKNOWN)

(* NoCircularDependencies - software dependencies should not have cycles *)
NoCircularDependencies ==
    \A s \in softwares : ~(\E path \in Seq(softwares) : 
        /\ path[1] = s
        /\ \A i \in 1..Len(path)-1 : path[i+1] \in path[i].dependencies
        /\ s \in path[Len(path)].dependencies)

(* AccountStateConsistency - locked accounts cannot be used *)
AccountStateConsistency ==
    \E attacker \in GetAttackers() : 
        \A ua \in attacker.accounts : ua.state = ACTIVE

(* ==================== TEMPORAL PROPERTIES ==================== *)

(* AttackReachability - if an attacker can reach vulnerable software, eventually they can exploit it *)
AttackReachability ==
    \A attacker \in GetAttackers(), software \in softwares :
        (CanReach(attacker.host, software.host) /\ software.hasVulnerability = TRUE) => 
        <> ( \E ua \in attacker.accounts : ua.software = software )

(* RiskPropagationCompleteness - if software is risky, all connected assets eventually become risky *)
RiskPropagationCompleteness ==
    \A s \in softwares, h \in hosts :
        (s.securityState = RISKY /\ CanReach(s.host, h)) => 
        <> (\A a \in assets : a.host = h => a.securityState = RISKY)

(* NoUnauthorizedCompromise - assets cannot be compromised without proper access *)
NoUnauthorizedCompromise ==
    \A a \in assets, attacker \in GetAttackers() :
        (a.securityState = COMPROMISED) => 
        \E aa \in assetAccesses : aa.asset = a /\ aa.accessor \in {attacker} \cup attacker.accounts

(* ==================== SPECIFICATION ==================== *)

Spec == Init /\ [][Next]_<<hosts, softwares, users, userAccounts, userGroups, assets, assetAccesses, attackPatterns, step>>

(* ==================== THEOREMS ==================== *)

THEOREM Spec => []TypeInvariant
THEOREM Spec => []SecurityStateMonotonicity  
THEOREM Spec => []NoCircularDependencies
THEOREM Spec => []AccountStateConsistency

THEOREM Spec => []AttackReachability
THEOREM Spec => []RiskPropagationCompleteness
THEOREM Spec => []NoUnauthorizedCompromise

====================================================================