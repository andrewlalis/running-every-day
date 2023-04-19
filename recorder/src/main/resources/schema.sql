CREATE TABLE run (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date TEXT NOT NULL,
    start_time TEXT,
    distance INTEGER,
    duration INTEGER,
    weight INTEGER,
    comment TEXT
)