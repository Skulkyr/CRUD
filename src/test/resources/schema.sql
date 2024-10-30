CREATE TYPE status_enum AS ENUM ('WAITING', 'IN_PROGRESS', 'COMPLETED');

CREATE TABLE task (
                      id SERIAL PRIMARY KEY,
                      title TEXT NOT NULL,
                      description TEXT,
                      status status_enum NOT NULL,
                      created TIMESTAMP NOT NULL,
                      updated TIMESTAMP NOT NULL
);