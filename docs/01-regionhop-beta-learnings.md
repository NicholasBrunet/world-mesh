# RegionHop Beta Learnings

**Author / Source of Truth:** Nicholas Brunet 
**Co-Author:** ChatGPT  

## Purpose

This document records what the RegionHop beta proved, what it did not prove, and what WorldMesh should carry forward.

RegionHop should be treated as a successful beta, not as the official codebase.

WorldMesh should use RegionHop as evidence, then rebuild the official project with cleaner structure and stronger long-term architecture boundaries.

## What RegionHop Was

RegionHop was a small Minestom-based prototype.

Its goal was to test whether two Dockerized Minecraft region workers could represent separate parts of one logical world and move a player between them.

## Proven Components

RegionHop proved:

```text
Minestom can run lightweight region workers.
Docker Compose can run multiple region containers.
Velocity can route a Minecraft client into region workers.
Redis can be used as a shared transfer-intent store.
A region can detect border crossing by coordinates.
A region can visually mark warning and transfer zones.
A player can be moved through Velocity from one backend to another.
A target region can restore player position, yaw, and pitch from shared handoff state.
```

## Beta Architecture

```text
Minecraft Client
  -> Velocity Proxy
  -> region-west Minestom container
  -> region-east Minestom container
  -> Redis transfer intent store
```

## Useful Features From The Beta

The beta had several useful concepts worth carrying forward:

```text
Dockerized region containers
Minestom flat worlds
Velocity proxy routing
Redis handoff storage
coordinate-based region boundaries
warning zones
visual border strips
/region debug command
directional neighbor model
TransferIntent persistence
position/yaw/pitch restoration
```

## Important Beta Concepts

The official project should preserve these ideas, but not necessarily copy the old files directly:

```text
RegionBounds
RegionConfig
RegionDirection
RegionNeighbor
RegionNeighborConfig
TransferIntent
TransferIntentStore
TransferTarget
RedisTransferIntentStore
ProxyTransferService
BorderTransferHandler
RegionWorldGenerator
RegionCommand
```

## Main Lesson

The main difficulty is not starting multiple servers.

The main difficulty is not Docker.

The main difficulty is not Redis.

The main difficulty is not basic coordinate routing.

The main difficulty is:

```text
The Minecraft client sees Velocity backend switching as a server transition.
That creates a loading screen.
```

## What This Means For WorldMesh

WorldMesh should start with the practical RegionHop-style path:

```text
Velocity
Minestom region workers
Redis transfer intents
position/yaw/pitch restoration
```

But it should not assume that this path can solve true seamless movement.

The official architecture should leave room for a future session-layer design where the client stays connected to one stable node while region authority changes behind the scenes.

## Final Beta Conclusion

RegionHop answered the first big questions:

```text
Can we run multiple region workers? Yes.
Can Docker manage them? Yes.
Can Velocity route into them? Yes.
Can Redis share handoff state? Yes.
Can position/yaw/pitch restoration work? Yes.
Can normal Velocity transfer remove loading? No, not by itself.
```

Therefore, WorldMesh should not be RegionHop v2.

WorldMesh should be a documented, versioned, pivot-friendly distributed Minecraft world-runtime proof-of-concept.
