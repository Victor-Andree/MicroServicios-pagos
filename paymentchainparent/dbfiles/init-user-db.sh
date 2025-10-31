#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER springboot WITH PASSWORD 'qwerty';

    CREATE DATABASE customer;
    GRANT ALL PRIVILEGES ON DATABASE customer TO springboot;

    CREATE DATABASE product;
    GRANT ALL PRIVILEGES ON DATABASE product TO springboot;

    CREATE DATABASE transaction;
    GRANT ALL PRIVILEGES ON DATABASE transaction TO springboot;
EOSQL