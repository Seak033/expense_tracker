-- Sequences (start at 1)
CREATE SEQUENCE user_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE category_seq    START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE transaction_seq START WITH 1 INCREMENT BY 1;

-- users (note: plural, not "user")
CREATE TABLE users (
  id         INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  first_name VARCHAR(64)  NOT NULL,
  last_name  VARCHAR(45)  NOT NULL,
  email      VARCHAR(128) NOT NULL UNIQUE,
  password   VARCHAR(255)
);

-- categories
CREATE TABLE categories (
  id       BIGINT  PRIMARY KEY DEFAULT nextval('category_seq'),
  user_id  INTEGER NOT NULL,
  name     VARCHAR(64) NOT NULL,
  active   BOOLEAN NOT NULL DEFAULT TRUE,

  CONSTRAINT uk_category_user_name UNIQUE (user_id, name),
  CONSTRAINT fk_category_user FOREIGN KEY (user_id)
      REFERENCES users(id) ON DELETE CASCADE
);

-- transactions
CREATE TABLE transactions (
  id          BIGINT  PRIMARY KEY DEFAULT nextval('transaction_seq'),
  user_id     INTEGER NOT NULL,
  category_id BIGINT,
  amount      NUMERIC(12,2) NOT NULL,
  date        DATE NOT NULL,
  description VARCHAR(20),
  type        VARCHAR(20) NOT NULL CHECK (type in ('EXPENSE','INCOME')),

  CONSTRAINT fk_tx_user     FOREIGN KEY (user_id)     REFERENCES users(id),
  CONSTRAINT fk_tx_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);
