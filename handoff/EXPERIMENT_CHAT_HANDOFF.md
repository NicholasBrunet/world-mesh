# WorldMesh Experiment Chat Handoff

**Author / Source of Truth:** Ok Bro  
**Co-Author:** ChatGPT  

## Purpose

This handoff is meant to start a new ChatGPT conversation focused primarily on the first WorldMesh experiment.

The new chat should treat the exported context file as canonical project state.

## Project Summary

WorldMesh is a proof-of-concept distributed Minecraft-compatible world runtime.

The long-term goal is to make many backend region workers collectively simulate one apparent world.

The project started from a prior beta called RegionHop.

RegionHop proved:

```text
Minestom region workers can run
Docker can manage multiple workers
Velocity can route players between workers
Redis can store handoff state
border detection works
position/yaw/pitch restoration works
normal Velocity backend transfer causes a visible loading transition
```

The current official direction is not to build around Velocity.

Velocity is prior research, not the target architecture.

The active long-term direction is a custom session/routing layer:

```text
Minecraft Client
  -> SessionNode
  -> RegionRouter
  -> RegionWorkers
```

## Current Official Code Direction

The official code is transport-neutral.

Current foundation includes:

```text
apps/region-worker/
packages/region-model/
packages/transfer-model/
tools/export-context/
docs/
```

The RegionWorker currently supports:

```text
Minestom startup
env-based RegionConfig
flat world generation
/region debug command
RegionBounds
RegionPosition
RegionDirection
RegionId
RegionNeighbor
border detection
HandoffIntent creation
HandoffDispatcher abstraction
HandoffReceiver abstraction
structured logging
dev run script
```

## Important Rule

Do not assume Velocity is the target architecture.

Do not design new core code around Velocity.

Focus on custom session/routing experiments.

## First Experiment Target

Create the first experiment here:

```text
experiments/EXP-001-session-node-smoke/
```

Primary goal:

```text
Can we start a simple custom SessionNode process that represents the future client-facing/session-facing layer?
```

Initial non-goals:

```text
No real Minecraft protocol implementation yet
No packet forwarding yet
No chunk streaming yet
No entity replication yet
No seamless transfer claim yet
No production networking yet
```

The first experiment should be intentionally small.

## Suggested First Experiment Shape

Start with a simple Java application or module that can:

```text
boot as a standalone process
load basic env config
log startup information
define what a SessionNode is responsible for
prepare a clean boundary for future client/session communication
```

Possible future concepts:

```text
SessionNode
SessionConfig
SessionId
ConnectedSession
RegionRouterClient
```

But only implement what is useful for the first smoke test.

## Request For New Chat

Please help continue the WorldMesh project from the exported context.

Primary focus:

```text
Start EXP-001-session-node-smoke.
Keep it isolated under experiments/.
Do not overbuild.
Do not implement full Minecraft protocol yet.
Use small complete file batches.
Give exact file paths.
For markdown files, provide downloadable files.
For code files, provide copy-paste code.
```

## Current Workflow Preference

The user prefers:

```text
short, focused responses
one main point at a time
2-5 complete files per batch
specific file paths
one-line CLI commands to create files before code
downloadable markdown files
clean official code
messier experiments isolated under experiments/
```
