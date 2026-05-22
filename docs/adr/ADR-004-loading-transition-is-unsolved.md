# ADR-004: Loading Transition Is Unsolved

**Author / Source of Truth:** Nicholas Brunet  
**Co-Author:** ChatGPT  

## Status

Accepted

## Context

RegionHop proved that a player can be transferred between backend region workers using Velocity.

It also proved that Redis can store short-lived handoff state and that the target region can restore position, yaw, and pitch.

However, the Minecraft client still experiences a visible loading transition during normal Velocity backend switching.

From the player's perspective, this does not feel truly seamless.

## Decision

WorldMesh will treat the client-side loading transition as an unsolved architecture problem.

Velocity-based backend transfer will remain useful for early proof-of-concepts, but it will not be considered the final solution for seamless movement.

## Reasoning

Normal Velocity backend switching still causes the client to behave like it is moving from one server backend to another.

Even if the proxy handles routing, the client receives a transition sequence that can include terrain loading, world/session state updates, and player respawn-style behavior.

Position restoration improves continuity, but it does not remove the transition.

Therefore, the project should separate two paths:

```text
Path A: Practical proxy-transfer platform
Path B: True seamless session-layer architecture
```

## Path A: Practical Proxy-Transfer Platform

This path uses:

```text
Velocity
Minestom region workers
Redis transfer intents
position/yaw/pitch restoration
state handoff improvements
```

This path is useful for:

```text
region gates
large zones
portal-like transitions
intentional transfer corridors
MMO-style area loading
early routing and handoff experiments
```

This path is not expected to fully remove the loading transition.

## Path B: True Seamless Session-Layer Architecture

This path likely requires:

```text
stable client-facing SessionNode
packet routing
chunk packet management
entity visibility management
ghost chunks
read-only border replicas
region authority switching
handoff synchronization
```

In this model, the client stays connected to one stable session node while backend region authority changes behind the scenes.

## Consequences

This decision makes the project more honest and easier to pivot.

It prevents early Velocity success from being mistaken for true seamless world simulation.

It also gives the project a practical short-term path without ignoring the long-term research wall.

## Exit Criteria / Pivot Trigger

This ADR should be revisited if one of the following happens:

```text
Velocity or another proxy supports truly seamless backend authority switching.
A prototype hides backend transfer without noticeable client loading.
SessionNode research becomes the main project milestone.
A custom protocol/session runtime replaces normal proxy backend switching.
```

Until then, the loading transition remains an accepted limitation of the practical proxy-transfer path.
