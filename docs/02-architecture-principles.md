# Architecture Principles

**Author / Source of Truth:** Nicholas Brunet 
**Co-Author:** ChatGPT  

## Purpose

This document defines the principles WorldMesh should follow as the project grows.

The goal is to avoid building a messy prototype that becomes impossible to pivot, explain, or hand off.

## Principle 1: Build Proof-of-Concepts First

WorldMesh should grow through small testable milestones.

Each milestone should answer a specific question.

Examples:

```text
Can one region worker boot?
Can two workers boot with different configs?
Can Velocity route between them?
Can Redis store transfer intent state?
Can position/yaw/pitch be restored?
Can transfer feel less broken?
Can the client transition ever be hidden?
```

## Principle 2: Keep Experiments Isolated

Messy experiments are allowed, but they should live in:

```text
experiments/
```

Official code should live in:

```text
apps/
packages/
infra/
tools/
```

This allows the project to test risky ideas without damaging the main codebase.

## Principle 3: Separate System Boundaries

WorldMesh should separate these concerns:

```text
Region model
Transfer model
Region worker runtime
Proxy routing
Session layer research
Orchestration
Context export tooling
```

The project should avoid mixing all behavior into one large application class.

## Principle 4: Treat Velocity As A Stepping Stone

Velocity is useful for early routing experiments.

It should not be treated as the guaranteed final architecture.

The practical path is:

```text
Client
  -> Velocity
  -> RegionWorker
```

The future seamless path may require:

```text
Client
  -> SessionNode
  -> RegionRouter
  -> RegionWorkers
```

## Principle 5: Document Pivots Instead Of Hiding Them

When an approach works, document it.

When an approach fails, document it.

When an approach is useful but limited, document it.

This project is research-heavy, so the reasoning behind decisions is just as important as the code.

## Principle 6: Prefer Small Milestones Over Giant Rewrites

A good milestone should be small enough to commit cleanly.

Example milestone order:

```text
Minimal region worker
Two configured workers
Velocity routing
Redis handoff store
Border detection
Position restoration
Richer handoff bundle
Region registry
Basic orchestration tooling
Session-layer research
```

## Principle 7: Make Context Export First-Class

WorldMesh should always be easy to hand off.

The context export tool should preserve:

```text
file paths
manifest
source code
docs
configuration
architecture decisions
project summary
```

It should exclude:

```text
dependencies
build outputs
binaries
large generated files
local IDE files
secrets
```

## Principle 8: The Human Author Is The Source Of Truth

ChatGPT may assist with drafting, implementation, and structure.

However, project direction, acceptance, and validation come from the human author.

A document or implementation should not be considered official until validated by the author.
