-- Creates Priority table for task priority levels
CREATE TABLE Priority (
    priority_id INTEGER PRIMARY KEY AUTOINCREMENT,
    priority_name TEXT NOT NULL
);

-- Creates Categories table for task categories
CREATE TABLE Categories (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name TEXT NOT NULL
);

-- Creates Tasks table with foreign keys to Categories and Priority
CREATE TABLE Tasks (
    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
    description TEXT NOT NULL,
    is_completed BOOLEAN DEFAULT 0,
    category_id INTEGER,
    priority_id INTEGER,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    FOREIGN KEY (priority_id) REFERENCES Priority(priority_id)
);