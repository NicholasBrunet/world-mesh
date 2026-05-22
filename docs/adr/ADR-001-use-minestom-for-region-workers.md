# ADR-001: Use Minestom For Region Workers

**Author / Source of Truth:** Nicholas Brunet
**Co-Author:** ChatGPT  

## Status

Accepted

## Context

WorldMesh needs an initial runtime for region workers.

A region worker is a backend process responsible for simulating one region of the larger logical world.

The first official proof-of-concept needs a lightweight Minecraft-compatible server runtime that can:

```text
start quickly
run in containers
generate simple test worlds
handle player connections
support custom logic
avoid unnecessary vanilla server complexity
```

The previous RegionHop beta proved that Minestom can run lightweight region workers for this style of experiment.

## Decision

WorldMesh will use Minestom for the initial official region-worker proof-of-concept.

The first region-worker app will live at:

```text
apps/region-worker/
```

Minestom will be used to build the early server-side runtime for:

```text
flat world generation
player spawning
region configuration
border detection
debug commands
handoff consumption
Velocity transfer support
```

## Reasoning

Minestom is a good fit for early WorldMesh work because it is lightweight and does not assume a full traditional survival server architecture.

It gives the project enough control to test custom world-runtime behavior without immediately building a Minecraft protocol implementation from scratch.

It also keeps the first proof-of-concept focused on region-worker architecture instead of low-level packet/session research.

## Consequences

This makes it easier to:

```text
start quickly
prototype region workers
run multiple workers in Docker
test border and handoff logic
avoid Paper/Bukkit plugin assumptions
```

This makes it harder to:

```text
reuse existing Bukkit/Paper plugins
depend on full vanilla server behavior
treat the runtime as production-ready by default
solve true session-layer seamlessness
```

## Exit Criteria / Pivot Trigger

This decision should be revisited if:

```text
Minestom blocks required protocol/session experiments
Minestom cannot support required player/world behavior
a custom SessionNode becomes the main project path
another runtime becomes clearly better for distributed region simulation
```

Until then, Minestom is the official runtime for early region-worker proof-of-concepts.
