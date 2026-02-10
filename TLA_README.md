# OpenSKE TLA+ Specification

This directory contains the formal TLA+ specification for the Open Security Knowledge Engine (OpenSKE) framework.

## Files

- **OpenSKE.tla** - Main TLA+ specification file
- **OpenSKE.cfg** - Configuration file for TLC model checker

## Overview

The TLA+ specification models the core security analysis functionality of OpenSKE, including:

### State Space
- **Network Infrastructure**: Hosts with network connections
- **Software Components**: Applications with vulnerabilities and dependencies
- **Security Entities**: Users, accounts, groups, and assets
- **Attack Patterns**: Various attack scenarios (SQL injection, account lockout, etc.)

### Security States
- `UNKNOWN` - Initial state
- `SAFE` - No known vulnerabilities
- `RISKY` - Has vulnerabilities or connected to risky components  
- `COMPROMISED` - Successfully attacked

### Key Features Modeled

1. **Risk Propagation**
   - Software vulnerabilities make software RISKY
   - RISKY software affects assets on same host
   - Network connections spread risk to connected hosts

2. **Attack Execution**
   - **CAPEC-2 (Account Lockout)**: Locks active accounts on vulnerable software
   - **CAPEC-7 (SQL Injection)**: Gains database access through injection
   - **CAPEC-16 (Dictionary Attack)**: Creates new accounts on systems with weak credentials
   - **CAPEC-1 (ACL Bypass)**: Unauthorized access (logged only)

3. **Network Reachability**
   - Static network topology with bidirectional connections
   - Recursive reachability for attack propagation

## Verification Properties

### Invariants
- **TypeInvariant**: All variables maintain correct types
- **SecurityStateMonotonicity**: Security states only worsen
- **NoCircularDependencies**: Software dependency graphs are acyclic
- **AccountStateConsistency**: Locked accounts cannot be used by attackers

### Temporal Properties
- **AttackReachability**: Attackers can eventually exploit reachable vulnerable software
- **RiskPropagationCompleteness**: All connected assets eventually become risky
- **NoUnauthorizedCompromise**: Assets cannot be compromised without proper access

## Model Checking

To run the TLC model checker:

```bash
# Install TLA+ Toolbox or use TLC command line
tlc OpenSKE.cfg
```

The configuration limits the model to manageable sizes for verification:
- Max 3 hosts
- Max 5 software instances  
- Max 2 users
- Max 4 accounts
- Max 3 assets

## Usage

This specification can be used to:

1. **Verify Security Properties**: Ensure the system maintains security invariants
2. **Analyze Attack Scenarios**: Model different attack patterns and their effects
3. **Validate Risk Propagation**: Confirm risk spreads correctly through the infrastructure
4. **Test Edge Cases**: Find unexpected states or vulnerabilities in the logic

The specification provides a formal foundation for understanding and verifying the security analysis behavior of OpenSKE.