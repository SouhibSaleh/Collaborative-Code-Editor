CREATE TABLE IF NOT EXISTS public.users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(80),
    role INTEGER,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.project (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES public.users(id) ON DELETE SET NULL,
    snapshot_id INTEGER,
    name VARCHAR(255),
    language VARCHAR(50) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.snapshot (
    id SERIAL PRIMARY KEY,
    code TEXT,
    project_id INTEGER REFERENCES public.project(id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES public.users(id) ON DELETE SET NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comments TEXT DEFAULT '[]'
);

CREATE TABLE IF NOT EXISTS public.languages (
    id SERIAL PRIMARY KEY,
    language VARCHAR(30)
);
