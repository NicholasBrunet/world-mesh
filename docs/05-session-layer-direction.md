# Session Layer Direction

**Author / Source of Truth:** Nicholas Brunet 
**Co-Author:** ChatGPT  

## Purpose

This document defines the current direction for WorldMesh's future custom session layer.

The goal is to prevent the project from accidentally rebuilding a normal proxy-transfer system when the long-term target is a stable client-facing runtime layer.

## Current Direction

WorldMesh should move toward a custom session architecture where the client remains connected to one stable session-facing process.

The long-term shape is:

```text
Minecraft Client
  -> SessionNode
  -> RegionRouter
  -> RegionWorkers
```

In this model, the client should not be transferred between normal backend servers every time it crosses a region boundary.

Instead, the session layer should eventually coordinate region authority behind the scenes.

## Why This Exists

The RegionHop beta proved that Velocity-style backend transfer can move players between region workers.

However, normal backend transfer creates a visible client-side loading transition.

That makes Velocity useful as evidence and as a stepping-stone, but not suitable as the long-term seamless architecture.

## What A SessionNode Should Eventually Do

A future SessionNode may be responsible for:

```text
maintaining the stable client connection
understanding Minecraft protocol state
routing relevant packets to region workers
receiving world/chunk/entity updates from workers
deciding which region currently owns player authority
coordinating region handoff without a normal server switch
hiding or reducing client-visible transition behavior
```

## What A SessionNode Should Not Be Yet

The first experiments should not attempt to solve everything.

Early SessionNode experiments should not immediately include:

```text
full Minecraft protocol support
full chunk streaming
entity replication
inventory/state persistence
anti-cheat
survival simulation
Bedrock support
production networking
```

The first goal is only to prove process boundaries and communication patterns.

## First Experiment Target

The first session-layer experiment should live at:

```text
experiments/EXP-001-session-node-smoke/
```

Its goal should be:

```text
Can we start a simple custom session-node process?
Can it expose a clear boundary for future client/session work?
Can it communicate with or prepare to communicate with region workers?
```

This experiment does not need to understand real Minecraft traffic yet.

## Design Rule

Official region-worker code should stay transport-neutral.

Region workers should emit logical handoff events such as:

```text
HandoffIntent
```

They should not care whether that handoff is later handled by:

```text
logging
Redis
custom TCP
custom HTTP
a RegionRouter
a SessionNode
a future packet/session runtime
```

This keeps WorldMesh flexible while the custom session architecture is researched.

## Pivot Rule

If a session-layer experiment proves useful, the clean parts may be promoted into:

```text
apps/session-node/
packages/protocol-model/
packages/transfer-model/
packages/region-model/
```

If an experiment fails, it should remain documented under:

```text
experiments/
```

Failed experiments are still valuable because they explain why later architectural choices exist.

## Current Conclusion

WorldMesh should continue building clean, transport-neutral official foundations while using isolated experiments to explore the custom session layer.

Velocity should remain documented as prior research, not as the active target architecture.
