-- OWNER: Team B. Matches brief Section 4.

CREATE TABLE IF NOT EXISTS locations (
    locationId   INTEGER PRIMARY KEY,
    name         TEXT NOT NULL,
    area         TEXT,
    type         TEXT,
    latitude     REAL,
    longitude    REAL
);

CREATE TABLE IF NOT EXISTS roads (
    fromLocationId      INTEGER NOT NULL,
    toLocationId        INTEGER NOT NULL,
    distance            REAL,
    travelTime          REAL,
    roadConditionWeight REAL,
    FOREIGN KEY (fromLocationId) REFERENCES locations(locationId),
    FOREIGN KEY (toLocationId)   REFERENCES locations(locationId)
);

CREATE TABLE IF NOT EXISTS service_requests (
    requestId     INTEGER PRIMARY KEY,
    source        INTEGER,
    destination   INTEGER,
    category      TEXT,
    urgency       TEXT,
    timeSubmitted TEXT,
    deadline      TEXT,
    status        TEXT
);

CREATE TABLE IF NOT EXISTS resources (
    resourceId         INTEGER PRIMARY KEY,
    type               TEXT,
    homeLocation       INTEGER,
    capacity           INTEGER,
    availabilityStatus TEXT
);

CREATE TABLE IF NOT EXISTS algorithm_runs (
    runId         INTEGER PRIMARY KEY,
    algorithmName TEXT,
    inputSize     INTEGER,
    timeNs        INTEGER,
    memoryKb      INTEGER,
    dateRun       TEXT
);

CREATE TABLE IF NOT EXISTS audit_events (
    eventId     INTEGER PRIMARY KEY,
    eventType   TEXT,
    description TEXT,
    timestamp   TEXT
);
