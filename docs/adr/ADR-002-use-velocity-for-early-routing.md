# ADR-002: Use Velocity For Early Routing

**Author / Source of Truth:** Nicholas Brunet  
**Co-Author:** ChatGPT  

## Status

Accepted

## Context

WorldMesh needs an early way to route Minecraft clients into backend region workers.

The RegionHop beta proved that Velocity can route a player between multiple Minestom backend workers.

This gives the project a practical way to test:

```text
multiple backend workers
region-to-region transfer
proxy-based routing
Docker Compose networking
handoff restoration
debuggable client behavior
```

However, Velocity backend switching causes a visible client-side loading transition.

That limitation is documented separately in:

```text
docs/adr/ADR-004-loading-transition-is-unsolved.md
```

## Decision

WorldMesh will use Velocity as the early proxy/routing layer.

Velocity will be treated as a practical proof-of-concept tool, not the guaranteed final architecture.

Early infrastructure will support this shape:

```text
Minecraft Client
  -> Velocity Proxy
  -> Region Workers
  -> Redis Handoff Store
```

## Reasoning

Velocity gives WorldMesh a fast and proven way to run early multi-region experiments.

It avoids requiring a custom client-facing session layer before the project has proven the rest of the region-worker architecture.

It also gives a clear baseline for understanding what normal proxy transfer can and cannot solve.

## Consequences

This makes it easier to:

```text
test multiple region workers
move players between backend servers
use existing Minecraft proxy infrastructure
build a Docker Compose development environment
separate proxy routing from region simulation
```

This makes it harder to:

```text
hide backend transitions
remove the client-side loading screen
implement true behind-the-scenes region authority switching
control every packet sent to the client
```

## Exit Criteria / Pivot Trigger

This decision should be revisited if:

```text
the project moves from practical transfer to true seamless movement
Velocity transfer limitations block the next milestone
SessionNode research becomes the primary architecture path
a custom packet/session router becomes necessary
```

Until then, Velocity is the official early routing layer for WorldMesh.
