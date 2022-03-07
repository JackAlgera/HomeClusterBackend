CREATE TABLE IF NOT EXISTS tasks
(
    taskId uuid NOT NULL PRIMARY KEY,
    taskName VARCHAR (255) NOT NULL,
    description VARCHAR (2000),
    doTaskAtDate TIMESTAMP NOT NULL,
    repeatDelay BIGINT NOT NULL,
    createdOn TIMESTAMP NOT NULL,
    lastModified TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS tags
(
    tagId uuid NOT NULL PRIMARY KEY,
    taskId uuid NOT NULL,
    tagName VARCHAR (255) NOT NULL,
    FOREIGN KEY (taskId)
        REFERENCES tasks (taskId)
        ON DELETE CASCADE
);
