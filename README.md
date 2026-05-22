# WorldMesh

**Author / Source of Truth:** Nicholas Brunet  
**Co-Author:** ChatGPT  

WorldMesh is a proof-of-concept distributed Minecraft-compatible world runtime.

The long-term goal is to make many backend region workers collectively simulate one apparent world.

The current project starts practical:

```text
Minecraft Client
  -> Velocity Proxy
  -> Region Workers
  -> Redis Handoff Store
```

The known technical wall is seamless movement between backend workers. Normal Velocity backend transfers cause a visible client-side loading transition.

WorldMesh is designed so the project can begin with Velocity-based region transfer, while leaving room to pivot later toward a custom session layer.

## Current Focus

The first official milestone is:

```text
v0.1 - Official Region Worker PoC
```

Initial goals:

```text
- Clean repository structure
- Context export tooling
- Minimal Minestom region worker
- Docker Compose development environment
- Velocity proxy routing
- Redis transfer intent storage
```

Initial non-goals:

```text
- No full survival gameplay
- No entity simulation
- No inventory persistence
- No true seamless transition yet
- No Bedrock support yet
- No dynamic scaling yet
```

## Repository Layout

```text
apps/
  region-worker/      Main Minestom region worker app
  proxy-plugin/       Future Velocity/plugin code
  session-node/       Future seamless session-layer experiments

packages/
  common/             Shared utilities
  region-model/       Region IDs, bounds, neighbors, routing models
  transfer-model/     TransferIntent and future HandoffIntent models
  config/             Shared configuration loading
  redis-store/        Redis-backed storage logic
  protocol-model/     Future protocol/session abstractions

experiments/          Isolated proof-of-concepts

infra/
  compose/            Docker Compose files
  docker/             Dockerfiles and container assets

tools/
  export-context/     Project context export script

docs/
  adr/                Architecture Decision Records
```

## Development Rule

Experiments may be messy.

Production-facing code should stay modular, documented, and easy to replace.

## Source of Truth Rule

The human author is the final source of truth for product direction, validation, and acceptance.

ChatGPT may assist as a co-author by drafting documentation, proposing structure, and helping with implementation, but project decisions should be validated by the author before being treated as official.

## Context Export

To export the current project into one markdown handoff file:

```bash
python tools/export-context/export_context.py
```

Generated exports are written to:

```text
context_exports/
```

These exports are meant to be uploaded into a future ChatGPT conversation or handed to another developer.
