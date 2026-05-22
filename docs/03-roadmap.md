# Roadmap

**Author / Source of Truth:** Nicholas Brunet
**Co-Author:** ChatGPT  

## Purpose

This document defines the current milestone roadmap for WorldMesh.

WorldMesh is a long-term proof-of-concept for a distributed Minecraft-compatible world runtime. The roadmap should stay flexible because the project is research-heavy and may need to pivot as technical limits become clearer.

## Current Direction

WorldMesh is moving away from normal proxy-transfer architecture as the final goal.

Velocity-style transfer was useful as prior research, but the active long-term direction is a custom session/routing layer.

The current target architecture is:

```text
Minecraft Client
  -> SessionNode
  -> RegionRouter
  -> RegionWorkers
```

## Milestone v0.1: Region Worker Foundation

Goal:

```text
Create a clean, modular RegionWorker foundation.
```

Status:

```text
In progress
```

Current features:

```text
Gradle multi-module project
Minestom region-worker app
region-model package
transfer-model package
env-based region config
region bounds
region directions
region neighbors
border detection
HandoffIntent creation
handoff dispatch abstraction
handoff receive abstraction
structured logging
dev run script
```

Non-goals:

```text
No real transfer yet
No Redis dependency yet
No Velocity dependency yet
No session-node yet
No Minecraft protocol routing yet
```

## Milestone v0.2: Region Worker Handoff Readiness

Goal:

```text
Prepare RegionWorker to emit and consume handoff state through replaceable interfaces.
```

Target features:

```text
HandoffDispatcher interface
HandoffReceiver interface
clean handoff logging
future-safe state bundle model
basic validation around target/source regions
simple local test hooks
```

Success condition:

```text
A RegionWorker can detect a border crossing, create a HandoffIntent, and pass it to an abstract handoff system without knowing the transport.
```

## Milestone v0.3: Session Node Smoke Experiment

Goal:

```text
Create the first isolated custom session-node experiment.
```

Location:

```text
experiments/EXP-001-session-node-smoke/
```

Initial scope:

```text
start a standalone session-node process
define basic process boundaries
define future communication shape with RegionWorkers
avoid Minecraft protocol complexity at first
```

Non-goals:

```text
No full Minecraft protocol implementation
No packet routing yet
No chunk streaming yet
No actual seamless transfer yet
```

## Milestone v0.4: Region Router Experiment

Goal:

```text
Prototype how a routing layer may map players, sessions, and regions.
```

Possible location:

```text
experiments/EXP-002-region-router-smoke/
```

Target questions:

```text
How does a player session map to a current region?
How does the system decide when authority changes?
How does a SessionNode discover RegionWorkers?
What data does a RegionWorker need to expose?
```

## Milestone v0.5: Worker-to-Router Communication

Goal:

```text
Test communication between RegionWorker and future routing/session components.
```

Possible transports to evaluate:

```text
local in-memory testing
TCP
HTTP
WebSocket
gRPC
message bus
Redis streams
```

The project should not choose a final transport too early.

## Milestone v0.6: Protocol Research

Goal:

```text
Begin Minecraft protocol/session research.
```

Target questions:

```text
What must a client-facing SessionNode understand?
Can we maintain one stable client connection?
Which packets must be owned by the SessionNode?
Which packets can be forwarded from RegionWorkers?
How are chunks, entities, and movement authority handled?
```

## Milestone v1.0: Documented Distributed World PoC

Goal:

```text
A complete documented proof-of-concept showing multiple RegionWorkers coordinated through a custom routing/session architecture.
```

Success does not require full production readiness.

Success means the architecture is proven enough to justify deeper development.
