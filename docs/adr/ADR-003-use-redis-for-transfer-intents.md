# ADR-003: Use Redis For Transfer Intents

**Author / Source of Truth:** Ok Bro  
**Co-Author:** ChatGPT  

## Status

Accepted

## Context

WorldMesh needs a simple shared store for short-lived handoff state between region workers.

When a player crosses from one region worker to another, the source worker needs to record transfer state that the target worker can consume.

The RegionHop beta proved that Redis can be used for this purpose.

The early handoff state is expected to include:

```text
player UUID
source region
target region
position
yaw
pitch
timestamp
```

Future handoff state may include:

```text
velocity
health
food
effects
gamemode
selected hotbar slot
inventory snapshot
temporary metadata
```

## Decision

WorldMesh will use Redis for early transfer-intent storage.

The early transfer model should treat transfer intents as short-lived records with a TTL.

Redis-backed storage should eventually live under:

```text
packages/redis-store/
```

The shared transfer models should eventually live under:

```text
packages/transfer-model/
```

## Reasoning

Redis is a good fit for early transfer intents because transfer state is short-lived, simple, and container-friendly.

A transfer intent does not need to live forever.

It should exist long enough for the target region worker to consume it, then expire automatically if something goes wrong.

Redis also works well in the early Docker Compose development environment.

## Consequences

This makes it easier to:

```text
share handoff state between workers
expire stale transfer intents
run locally with Docker Compose
debug transfer state
avoid committing too early to a heavier database
```

This makes it harder to:

```text
model complex transactional handoff
persist long-term player state
replay historical transfer events
guarantee multi-step distributed consistency
```

## Exit Criteria / Pivot Trigger

This decision should be revisited if:

```text
handoff state becomes large or complex
transfer requires stronger transactional guarantees
player state persistence becomes a major concern
the system needs event replay or auditing
a formal event bus becomes more appropriate
```

Until then, Redis is the official early store for WorldMesh transfer intents.
