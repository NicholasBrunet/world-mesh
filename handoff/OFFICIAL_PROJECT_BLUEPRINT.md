# Official Project Blueprint: Distributed Minecraft World Runtime

## Purpose of this document

This document is meant to be passed into a fresh ChatGPT conversation or handed to another developer as the starting context for the next, more official version of the project.

The previous prototype, `RegionHop`, was a beta/test run. It proved that the basic infrastructure is possible, but also exposed the biggest technical wall: removing the client-side loading transition when moving between backend region servers.

The next project should be designed as a proper long-term proof-of-concept platform, not just a cleaned-up copy of the beta.

---

# 1. High-level vision

The long-term goal is to build a distributed Minecraft-compatible world platform where many backend processes can collectively simulate one apparent world.

The dream target:

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

From the player’s perspective:

```text
I joined one Minecraft server.
I walked around one world.
The world felt continuous.
```

From the backend’s perspective:

```text
client/session layer
proxy/routing layer
region simulation workers
shared state/handoff storage
orchestration layer
monitoring/debug tooling
```

The project is not simply “optimize a Minecraft server.”

It is closer to:

```text
Build a distributed Minecraft world runtime.
```

---

# 2. RegionHop beta summary

## What RegionHop was

`RegionHop` was a small Minestom-based prototype.

Its goal was to prove that two Dockerized Minecraft region workers could represent separate parts of a single logical world and move the player between them.

## Proven components

RegionHop proved:

```text
Minestom can run lightweight region workers.
Docker Compose can run multiple region containers.
Velocity can route a Minecraft client into region workers.
Redis can be used as a shared transfer-intent store.
A region can detect border crossing by coordinates.
A region can visually mark warning/transfer zones.
A player can be moved through Velocity from one backend to another.
A target region can restore player position, yaw, and pitch using shared handoff state.
```

## Current beta architecture

```text
Minecraft Client
    ↓
Velocity proxy
    ↓
region-west Minestom container
region-east Minestom container
    ↓
Redis transfer intent store
```

## Current beta features

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

## Important beta files/classes

```text
BorderTransferHandler.java
Main.java
RegionBounds.java
RegionCommand.java
RegionConfig.java
RegionDirection.java
RegionNeighbor.java
RegionNeighborConfig.java
RegionServer.java
RegionWorldGenerator.java
RedisConfig.java
RedisTransferIntentStore.java
TransferIntent.java
TransferIntentStore.java
TransferTarget.java
ProxyTransferService.java
docker-compose.yml
velocity/velocity.toml
```

The actual uploaded context export before Redis/transfer-intent additions showed the project had already split the core code into multiple classes instead of one giant `Main.java`.

---

# 3. Main lesson from RegionHop

The main difficulty is not starting multiple servers.

The main difficulty is not Docker.

The main difficulty is not basic coordinate routing.

The main difficulty is:

```text
The Minecraft client sees Velocity backend switching as a server transition.
That creates a loading screen.
```

This is the core friction point.

## Key conclusion

There are two separate architecture paths:

### Path A: Practical proxy-transfer platform

```text
Velocity
Minestom region workers
Redis transfer intents
visible loading transition
position/yaw/pitch restoration
state handoff improvements
```

This path is buildable sooner.

It can support:

```text
region gates
large zones
portal-like transitions
intentional transfer corridors
MMO-style area loading
```

But it probably cannot fully remove the loading screen because the client is still switching backend servers.

### Path B: True seamless session-layer architecture

```text
Client connects to one stable session node.
Session node stays connected to the client.
Region workers sit behind the session node.
Region authority changes behind the scenes.
The client never performs a normal server switch.
```

This is the real path toward seamless transitions.

It is much harder because it requires packet/session routing, ghost chunks, entity visibility, and authoritative region handoff.

---

# 4. Core project ideology

The official project should be built around these principles:

```text
1. Build proof-of-concepts first.
2. Keep clean module boundaries.
3. Treat Velocity transfer as a stepping stone, not the final answer.
4. Separate client/session architecture from region simulation architecture.
5. Version ideas aggressively.
6. Document pivots instead of hiding them.
7. Design so the project can pivot when technical reality demands it.
8. Prefer small testable milestones over giant rewrites.
9. Keep beta experiments isolated from official architecture.
10. Make context export and handoff documentation first-class.
```

The project should not assume Velocity transfer is the final form.

It should assume:

```text
Velocity transfer is Phase 1.
A custom client/session layer may become Phase 2 or Phase 3.
```

---

# 5. Proposed official project name

Working name:

```text
WorldMesh
```

Other possible names:

```text
ShardMC
AtlasMC
RegionNet
Mosaic
ChunkGrid
MeshCraft
```

Recommended term set:

```text
WorldMesh      = whole platform
RegionWorker   = backend process simulating a region
SessionNode    = future client-facing stable connection layer
RegionRouter   = decides region ownership/routing
TransferIntent = short-lived handoff state for proxy transfers
HandoffIntent  = future richer state handoff protocol
RegionRegistry = maps region IDs/directions to workers
Orchestrator   = starts/stops/scales/manages workers
```

---

# 6. Proposed repository structure

```text
worldmesh/
  README.md

  docs/
    00-vision.md
    01-regionhop-beta-learnings.md
    02-architecture-principles.md
    03-roadmap.md
    04-pivot-strategy.md
    05-seamless-transition-research.md
    06-region-runtime.md
    07-session-layer.md
    08-container-orchestration.md

    adr/
      ADR-001-use-minestom-for-initial-region-workers.md
      ADR-002-use-velocity-for-early-proxy-routing.md
      ADR-003-use-redis-for-transfer-intents.md
      ADR-004-loading-transition-is-unsolved.md
      ADR-005-keep-session-layer-as-future-research.md

  experiments/
    regionhop-beta/
    velocity-transfer-poc/
    redis-handoff-poc/
    ghost-border-visual-poc/
    packet-router-poc/

  apps/
    region-worker/
    proxy-plugin/
    session-node/

  packages/
    common/
    region-model/
    transfer-model/
    config/
    redis-store/
    protocol-model/

  infra/
    docker/
    compose/
    kubernetes-later/

  tools/
    export-context/
    dev-cli/

  scripts/
    dev-up.ps1
    dev-down.ps1
    export-context.py
```

## Repository philosophy

`experiments/` is allowed to be messy.

`apps/` and `packages/` should be cleaner.

`docs/adr/` records decisions and pivots.

`tools/export-context/` exists so the project can always be handed to another ChatGPT conversation or developer without losing context.

---

# 7. Initial official PoC target

The first official PoC should not try to be a full seamless system.

It should formalize the RegionHop learnings into a cleaner architecture.

## PoC v0.1 target

```text
Two region workers
Velocity proxy
Redis handoff store
position/yaw/pitch restoration
debug command
clean module boundaries
documented architecture decisions
```

## PoC v0.1 non-goals

```text
No full vanilla survival.
No entities.
No redstone.
No inventory persistence.
No chunk persistence.
No true seamless client transition.
No Bedrock support yet.
No version agnosticism yet.
No dynamic scaling yet.
```

## Why this scope is correct

It creates a stable foundation and leaves room to pivot toward a custom session layer later.

---

# 8. Versioning plan

Use semantic-ish versions for project milestones, but treat versions as architecture milestones, not just code releases.

```text
v0.1 - Official region-worker PoC
v0.2 - Velocity-routed regions
v0.3 - Redis transfer intents
v0.4 - position/yaw/pitch restoration
v0.5 - richer player state handoff bundle
v0.6 - region registry
v0.7 - basic orchestration/dev CLI
v0.8 - ghost-border visual simulation
v0.9 - packet/session-layer research PoC
v1.0 - first complete documented distributed-world PoC
```

## Experiment versioning

Experiments should be named clearly:

```text
EXP-001-region-worker-basics
EXP-002-velocity-transfer
EXP-003-redis-transfer-intents
EXP-004-state-continuous-handoff
EXP-005-ghost-border-visuals
EXP-006-session-node-research
```

---

# 9. Architecture Decision Record template

Each decision should use this format:

```markdown
# ADR-XXX: Title

## Status

Accepted / Experimental / Deprecated / Replaced

## Context

What problem are we solving?

## Decision

What are we choosing?

## Reasoning

Why this choice?

## Consequences

What does this make easier?
What does this make harder?

## Exit criteria / Pivot trigger

When should this decision be revisited?
```

## Example ADRs

### ADR-001: Use Minestom for initial region workers

Reasoning:

```text
Minestom is lightweight.
It avoids Paper/Bukkit legacy assumptions.
It lets us build custom behavior from minimal primitives.
It is better for PoC region-worker experiments.
```

Pivot trigger:

```text
If Minestom blocks critical protocol/session experimentation,
evaluate custom protocol/runtime code or another library.
```

### ADR-002: Use Velocity for early proxy routing

Reasoning:

```text
Velocity proves multi-backend routing quickly.
It avoids writing a client-facing proxy too early.
It gives a practical baseline.
```

Pivot trigger:

```text
If seamless client transition becomes the main blocker,
move research toward a SessionNode instead of normal Velocity transfer.
```

### ADR-003: Use Redis for transfer intents

Reasoning:

```text
Transfer intents are short-lived.
Redis is simple and container-friendly.
TTL-based handoff data is a natural fit.
```

Pivot trigger:

```text
If handoff state becomes richer or transactional,
evaluate a more formal event bus or database-backed handoff protocol.
```

### ADR-004: Loading transition is unsolved

Reasoning:

```text
Velocity backend transfer causes a visible client transition.
Position/yaw/pitch restoration improves continuity but does not remove loading.
```

Pivot trigger:

```text
If the goal becomes true seamless movement,
prototype SessionNode architecture.
```

---

# 10. Seamless transition research notes

## Why the loading transition happens

Velocity backend transfer still causes the client to switch backend servers.

From the client’s perspective, this behaves like:

```text
leave one backend
join another backend
receive world/session state
load terrain
spawn player
```

So even if the proxy handles routing, the client experiences a transition.

## How to reduce it

Short-term improvements:

```text
restore position/yaw/pitch
restore velocity
restore health/food/effects
restore inventory
restore selected hotbar slot
restore gamemode
restore border-facing direction
add titles/sounds to make transfer feel intentional
use transfer corridors/gates
```

These do not remove the loading screen. They only make it feel less broken.

## How to actually remove it

The likely long-term answer is:

```text
Do not switch the client between backend servers.
Keep the client connected to a stable SessionNode.
Let the SessionNode route packets to region workers.
Switch region authority behind the scenes.
```

Future architecture:

```text
Client
  ↓
SessionNode
  ↓
RegionRouter
  ↓
RegionWorkers
```

This requires research into:

```text
Minecraft protocol handling
chunk packet routing
entity packet routing
player movement authority
ghost chunks
read-only border replicas
region ownership handoff
packet-level synchronization
```

---

# 11. Proper system boundaries

The official project should separate these ideas:

## Region model

Owns:

```text
region ID
bounds
directions
neighbors
ownership
coordinate mapping
```

## Transfer model

Owns:

```text
TransferIntent
HandoffIntent
source region
target region
player UUID
position/yaw/pitch
future player state bundle
```

## Region worker

Owns:

```text
Minestom server instance
world generation
player spawn/configuration
border detection
debug commands
handoff consumption
```

## Proxy routing

Owns:

```text
Velocity config
early proxy transfer
possible proxy plugin later
```

## Session layer

Owns future research:

```text
stable client connection
packet routing
chunk visibility
entity visibility
region authority switching
```

## Orchestration

Owns:

```text
Docker Compose initially
future Kubernetes/Docker API experiments
worker health checks
worker lifecycle
logs
metrics
```

---

# 12. Suggested first official coding milestone

## Milestone: `worldmesh-region-worker`

Create a clean Gradle Java project:

```text
apps/region-worker/
```

Features:

```text
Minestom server starts
env-based RegionConfig
flat generated world
debug /region command
RedisConfig
TransferIntentStore
Velocity transfer support
```

The first official commit should be simple and documented.

## First commit message

```text
chore: initialize WorldMesh repository structure
```

## Second commit message

```text
feat(region-worker): add minimal Minestom region worker
```

## Third commit message

```text
feat(infra): add Docker Compose with Velocity, Redis, and two region workers
```

---

# 13. Context export / handoff strategy

This project must always be easy to pass to a new chat.

Maintain a script:

```text
tools/export-context/export_context.py
```

It should export:

```text
README.md
docs/**/*.md
apps/**/*.java
packages/**/*.java
*.gradle.kts
settings.gradle.kts
Dockerfile
docker-compose.yml
*.toml
*.yaml
*.yml
*.json
```

It should ignore:

```text
.git/
.gradle/
build/
out/
target/
node_modules/
context_exports/
__pycache__/
directories starting with "-"
```

The export should include:

```text
manifest
file paths
file contents
timestamp
root path
project summary
```

Recommended generated file:

```text
context_exports/worldmesh_context_YYYY-MM-DD_HH-MM-SS.md
```

## Handoff instructions for another ChatGPT conversation

Paste or upload the latest context export and say:

```text
This is the current canonical context for my WorldMesh project.
Treat this as the current project state.
Do not assume files outside this export exist.
For new implementations, give full files.
For small/medium edits, give targeted replacements.
Do not remove existing working code unless explicitly asked.
This project is a distributed Minecraft world-runtime PoC.
The main known research wall is removing the client-side loading transition.
```

---

# 14. Recommended next ChatGPT prompt

Use this in a fresh chat:

```text
I am starting an official project called WorldMesh based on a previous beta called RegionHop.

WorldMesh is a long-term proof-of-concept for a distributed Minecraft-compatible world runtime. The beta proved Minestom region workers, Docker Compose, Velocity routing, Redis transfer intents, border detection, warning zones, /region debugging, and position/yaw/pitch restoration across backend transfers.

The biggest known friction point is the client-side loading transition caused by normal Velocity backend switching. The official project should be designed so we can start with Velocity transfer but pivot later toward a custom SessionNode / packet-routing architecture if needed.

I want to build this properly with docs, ADRs, versioning, experiments, clean module boundaries, and context export so another chat can pick it up.

Please help me initialize the repository structure and create the first docs:
- README.md
- docs/00-vision.md
- docs/01-regionhop-beta-learnings.md
- docs/02-architecture-principles.md
- docs/03-roadmap.md
- docs/adr/ADR-001-use-minestom-for-region-workers.md
- docs/adr/ADR-002-use-velocity-for-early-routing.md
- docs/adr/ADR-003-use-redis-for-transfer-intents.md
- docs/adr/ADR-004-loading-transition-is-unsolved.md

Do not write code yet unless I ask. Start with project structure and documentation.
```

---

# 15. Immediate next action

Next session should start by creating the official repository documentation, not code.

Recommended first generated files:

```text
README.md
docs/00-vision.md
docs/01-regionhop-beta-learnings.md
docs/02-architecture-principles.md
docs/03-roadmap.md
docs/04-pivot-strategy.md
docs/adr/ADR-001-use-minestom-for-region-workers.md
docs/adr/ADR-002-use-velocity-for-early-routing.md
docs/adr/ADR-003-use-redis-for-transfer-intents.md
docs/adr/ADR-004-loading-transition-is-unsolved.md
tools/export-context/export_context.py
```

This gives the project an official foundation before copying any RegionHop code forward.

---

# 16. Final beta conclusion

RegionHop should be treated as a successful beta.

It answered the first big questions:

```text
Can we run multiple region workers? Yes.
Can Docker manage them? Yes.
Can Velocity route into them? Yes.
Can Redis share handoff state? Yes.
Can position/yaw/pitch restoration work? Yes.
Can normal Velocity transfer remove loading? No, not by itself.
```

The official project should now be designed around that knowledge.

The next project should not be merely “RegionHop v2.”

It should be:

```text
WorldMesh:
a documented, versioned, pivot-friendly distributed Minecraft world-runtime proof-of-concept.
```
