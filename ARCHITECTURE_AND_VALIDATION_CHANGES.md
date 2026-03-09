# Architecture And Validation Changes

## Purpose
This document explains the refactor and safeguards added to the car wash simulator, with focus on:
- encapsulation (what is public vs internal),
- validation (what invalid input is blocked),
- event/update flow (how components interact safely).

## Access Control Model

### 1) `CarWashState` is now controlled
File: `src/carwash/CarWashState.java`

- `public`:
  - `CarWashState(...)` constructor
- package-private (no `public` keyword):
  - all domain getters/mutators such as `advanceTime`, machine counters, queue access, random draws, stats reads

Why:
- only classes in package `carwash` can mutate/read internal operational state directly.
- external packages (for example `labb6`) cannot change state "willy nilly".

### 2) Start/stop domain events moved into `carwash`
Files:
- `src/carwash/CarWashStartEvent.java`
- `src/carwash/CarWashStopEvent.java`

Why:
- these events need internal access to `CarWashState` package-private methods.
- keeping them in `carwash` preserves encapsulation.

### 3) Runner (`labb6`) only orchestrates
File: `src/labb6/StartCarWashSim.java`

Now the runner:
- constructs state and simulator,
- registers observer,
- schedules `CarWashStartEvent` and `CarWashStopEvent`,
- does not directly mutate internal car wash state.

## Validation Rules Added

### `CarWashState` constructor validation
File: `src/carwash/CarWashState.java`

Rejects:
- negative `fastMachines`, `slowMachines`, `maxQueueSize`,
- null `arrivalStream`, `fastServiceStream`, `slowServiceStream`.

### Time progression validation
File: `src/carwash/CarWashState.java`

`advanceTime(newTime)` now rejects:
- non-finite time (`NaN`, `Infinity`),
- negative time,
- backward time (`newTime < currentTime`).

Result:
- `lastTimeSession` cannot be negative,
- state time can never move backwards.

### Event time validation
File: `src/simulator/Event.java`

Event constructor rejects:
- non-finite `time`,
- negative `time`.

### Event payload validation
Files:
- `src/carwash/CarWashEvent.java`
- `src/carwash/EventSnapshot.java`

Rejects:
- null `Car` for `CarWashEvent`,
- null `eventName`/`machine` in `EventSnapshot`,
- invalid `carId < -1` in `EventSnapshot`.

### Car validation
File: `src/carwash/Car.java`

Rejects:
- negative car id,
- invalid queue timestamp (`NaN`, `Infinity`, negative) in `setQueuedAt`.

### Random stream validation
Files:
- `src/random/ExponentialRandomStream.java`
- `src/random/UniformRandomStream.java`

Rejects:
- invalid exponential `lambda` (must be finite and `> 0`),
- invalid uniform bounds (must be finite, non-negative, `upper >= lower`).

## Machine/State Safety Checks
File: `src/carwash/CarWashState.java`

Guardrails:
- `decreaseFast`/`decreaseSlow` prevent going below zero free machines,
- `increaseFast`/`increaseSlow` prevent exceeding total machine count.

## Observer Update Flow

### Current mechanism
Files:
- `src/simulator/State.java`
- `src/carwash/StatusView.java`
- `src/carwash/EventSnapshot.java`

Flow:
1. Domain event computes action.
2. It publishes `new EventSnapshot(eventName, carId, machine)` via `publishUpdate(arg)`.
3. `StatusView.update(Observable o, Object arg)` receives snapshot and prints row.

Notes:
- event metadata is no longer stored in `CarWashState` fields.
- this avoids stale/shared mutable event-name/id/machine state.

## Removed/Restricted APIs

### Removed from `CarWashState`
- `setCurrentTime(...)`
- `addQueueTime(...)`
- `addIdleTime(...)`

Reason:
- these allowed external arbitrary mutation of core counters/time.

### Old observer interface removed earlier
- custom `simulator.Observer` interface removed
- replaced by `java.util.Observable` / `java.util.Observer` integration.

## How It Works End-to-End

1. `StartCarWashSim.run()` creates streams + state + simulator + view.
2. It schedules:
   - `CarWashStartEvent(0.0)`
   - `CarWashStopEvent(stopTime)`
3. `CarWashStartEvent` advances state and schedules first arrival.
4. `ArriveEvent` and `LeaveEvent` drive queue/service transitions.
5. Every event publishes an immutable `EventSnapshot` to observers.
6. `StatusView` prints current line from state stats + snapshot metadata.
7. `CarWashStopEvent` advances time, publishes stop snapshot, stops simulation.

## Design Outcome
- External code can no longer directly manipulate internal car wash state internals.
- Invalid constructor inputs are blocked early.
- Backward/invalid time progression is blocked.
- Event updates are explicit and immutable through observer payloads.
