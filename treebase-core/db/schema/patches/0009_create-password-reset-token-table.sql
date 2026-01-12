--
-- Schema patch 0009: Create password_reset_token table
--
-- This table stores secure tokens for password reset functionality.
-- Tokens expire after a configurable time period and can only be used once.
--

-- Create sequence for token_id
CREATE SEQUENCE password_reset_token_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

-- Create password_reset_token table
CREATE TABLE password_reset_token (
    token_id bigint DEFAULT nextval('password_reset_token_id_sequence'::regclass) NOT NULL,
    token character varying(100) NOT NULL,
    user_id bigint NOT NULL,
    expiry_date timestamp without time zone NOT NULL,
    used boolean DEFAULT false NOT NULL
);

-- Add primary key constraint
ALTER TABLE ONLY password_reset_token
    ADD CONSTRAINT password_reset_token_pkey PRIMARY KEY (token_id);

-- Add unique constraint on token
ALTER TABLE ONLY password_reset_token
    ADD CONSTRAINT password_reset_token_token_key UNIQUE (token);

-- Add foreign key constraint to user table
ALTER TABLE ONLY password_reset_token
    ADD CONSTRAINT password_reset_token_user_fk FOREIGN KEY (user_id) REFERENCES "user"(user_id) ON DELETE CASCADE;

-- Create index on user_id for faster lookups
CREATE INDEX password_reset_token_user_idx ON password_reset_token USING btree (user_id);

-- Create index on expiry_date for faster cleanup queries
CREATE INDEX password_reset_token_expiry_idx ON password_reset_token USING btree (expiry_date);
