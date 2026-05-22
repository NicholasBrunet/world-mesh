# Pivot Strategy

**Author / Source of Truth:** Nicholas Brunet  
**Co-Author:** ChatGPT  

## Purpose

This document defines how WorldMesh should decide when to continue building official code, when to create experiments, and when to pivot.

WorldMesh is research-heavy. Pivoting is expected.

The goal is to pivot deliberately instead of accidentally creating a messy codebase.

## Two Development Lanes

WorldMesh has two main lanes:

```text
Official foundation lane
Experiment lane
```

## Official Foundation Lane

Official code lives in:

```text
apps/
packages/
infra/
tools/
docs/
```

This code should be:

```text
modular
documented
transport-neutral where possible
easy to test
easy to replace
stable enough to survive future milestones
```

Current official foundation examples:

```text
apps/region-worker/
packages/region-model/
packages/transfer-model/
tools/export-context/
```

## Experiment Lane

Experiments live in:

```text
experiments/
```

Experiment code may be messy.

Experiment code may break rules temporarily.

Experiment code may test risky ideas before they are promoted into the official foundation.

Examples:

```text
experiments/EXP-001-session-node-smoke/
experiments/EXP-002-region-router-smoke/
experiments/EXP-003-worker-router-communication/
experiments/EXP-004-protocol-handshake-research/
```

## When To Create An Experiment

Create an experiment when the idea is:

```text
uncertain
risky
research-heavy
likely to be rewritten
not ready for the official architecture
testing a new technical direction
```

Examples:

```text
custom SessionNode
Minecraft protocol handling
packet routing
ghost chunks
chunk mirroring
entity visibility handoff
region authority switching
custom networking layer
```

## When To Keep Building Official Code

Keep building official code when the idea is:

```text
already validated
core to the long-term design
transport-neutral
needed by multiple future paths
safe to depend on
```

Examples:

```text
RegionBounds
RegionDirection
RegionNeighbor
HandoffIntent
HandoffDispatcher
HandoffReceiver
context export tooling
documentation
ADRs
```

## Promotion Rule

An experiment can be promoted only when it has proven value.

Before promotion, ask:

```text
What did this experiment prove?
Which parts are reusable?
Which parts are throwaway?
Does this belong in apps/, packages/, infra/, or tools/?
Does promotion require an ADR?
```

Promotion should usually be a clean rewrite, not a direct copy of messy experiment code.

## Failure Rule

Failed experiments should not be deleted immediately.

They should remain documented because they explain why the project changed direction.

A failed experiment should record:

```text
what was tested
what worked
what failed
why it failed
what the next direction should be
```

## Velocity Pivot

Velocity-style backend transfer is no longer the active long-term direction.

It remains valuable as prior evidence because it proved:

```text
multiple region workers can run
proxy routing can connect a client to workers
handoff state can restore position/yaw/pitch
normal backend transfer still creates a loading transition
```

Current strategy:

```text
Do not build WorldMesh around Velocity.
Do not bake Velocity assumptions into core models.
Keep RegionWorker handoff logic transport-neutral.
Move custom session/routing ideas into experiments.
```

## Session Layer Pivot

The active long-term research direction is:

```text
Minecraft Client
  -> SessionNode
  -> RegionRouter
  -> RegionWorkers
```

This is harder than proxy transfer, so it should begin in experiments before becoming official app code.

## Current Conclusion

WorldMesh should continue building the official RegionWorker foundation while starting isolated experiments for the custom session/routing layer.

Official code should stay clean.

Experiments should answer risky questions quickly.
