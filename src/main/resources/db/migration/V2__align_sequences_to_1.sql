ALTER SEQUENCE IF EXISTS user_seq     INCREMENT BY 1;
ALTER SEQUENCE IF EXISTS category_seq INCREMENT BY 1;

SELECT setval('user_seq',     COALESCE((SELECT MAX(id)+1 FROM users), 1), false);
SELECT setval('category_seq', COALESCE((SELECT MAX(id)+1 FROM categories), 1), false);

ALTER SEQUENCE IF EXISTS user_seq     OWNED BY users.id;
ALTER SEQUENCE IF EXISTS category_seq OWNED BY categories.id;