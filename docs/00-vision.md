# WorldMesh Vision

**Author / Source of Truth:** Nicholas Brunet  
**Co-Author:** ChatGPT  

## Purpose

WorldMesh is a proof-of-concept distributed Minecraft-compatible world runtime.

The long-term goal is to make many backend processes collectively simulate one apparent world.

From the player's perspective:

```text
I joined one Minecraft server.
I walked around one world.
The world felt continuous.
```

From the backend's perspective:

```text
Client/session layer
Proxy/routing layer
Region simulation workers
Shared handoff/state systems
Orchestration layer
Monitoring/debug tooling
```

WorldMesh is not simply a Minecraft server optimization project.

It is an experiment in building a distributed Minecraft world runtime.

## Long-Term Direction

The dream target is:

```text
10,000+ players
one apparent open world
many region workers
containerized deployment
shared handoff/state systems
eventual Java Edition support
possible Bedrock support later
developer-friendly tooling
```

This project should grow through small proof-of-concepts, documented decisions, and controlled pivots.

## Initial Direction

The first official version should start with a practical architecture:

```text
Minecraft Client
  -> Velocity Proxy
  -> Region Workers
  -> Redis Handoff Store
```

This is not the final dream architecture.

It is the first useful stepping stone.

## Known Technical Wall

Normal Velocity backend switching causes a client-visible loading transition.

That means Velocity-based transfer can prove routing, handoff state, and region boundaries, but it probably cannot prove fully seamless movement by itself.

The future seamless path likely requires a stable client-facing session layer:

```text
Minecraft Client
  -> SessionNode
  -> RegionRouter
  -> RegionWorkers
```

## Project Philosophy

WorldMesh should be:

```text
documented
versioned
pivot-friendly
experiment-driven
modular
easy to hand off to another developer or ChatGPT conversation
```

The project should preserve experiments, not hide them.

Failed or limited approaches should be documented because they explain why later architecture decisions exist.
