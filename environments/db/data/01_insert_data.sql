\connect sns_db
INSERT INTO users (id, mail, name, created_at, updated_at) VALUES ('7895cf5a-9986-454a-942a-e1c0dc16f814', 'y.team.kobe@gmail.com', '原田佳明', '2020-05-12 11:11:05.926627', '2020-05-12 11:11:05.926627');

INSERT INTO messages (id, user_id, text, created_at, updated_at) VALUES ('aae10a8a-28a6-4b38-8b0c-6566bc54d76b', '7895cf5a-9986-454a-942a-e1c0dc16f814', 'こんにちは', '2020-05-12 11:11:05.930263', '2020-05-12 11:11:05.930263');

INSERT INTO comments (id, user_id, message_id, text, created_at, updated_at) VALUES ('b50741a3-e1ad-463b-9e40-583e4060156c', '7895cf5a-9986-454a-942a-e1c0dc16f814', 'aae10a8a-28a6-4b38-8b0c-6566bc54d76b', '初めまして', '2020-05-12 11:11:05.933222', '2020-05-12 11:11:05.933222');
