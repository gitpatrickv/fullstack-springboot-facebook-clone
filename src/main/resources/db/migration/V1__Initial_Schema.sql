CREATE TABLE IF NOT EXISTS users (
    `user_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(100) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR(50) NOT NULL,
    `gender` ENUM('MALE', 'FEMALE') NOT NULL,
    `role` ENUM('USER', 'ADMIN') NOT NULL,
    `date_of_birth` DATE,
    `profile_picture` VARCHAR(255) DEFAULT NULL,
    `cover_photo` VARCHAR(255) DEFAULT NULL,
    `created_at` DATE
);

CREATE TABLE IF NOT EXISTS posts (
    `post_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `shared_post_id` BIGINT DEFAULT NULL,
    `shared_image_id` BIGINT DEFAULT NULL,
    `guest_poster_id` BIGINT DEFAULT NULL,
    `content` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS post_images (
    `post_image_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id` BIGINT NOT NULL,
    `post_image_url` VARCHAR(255) NOT NULL,
    `timestamp` TIMESTAMP
);

ALTER TABLE posts
ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_shared_image_id FOREIGN KEY (shared_image_id) REFERENCES post_images(post_image_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_shared_post_id FOREIGN KEY (shared_post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_guest_poster_id FOREIGN KEY (guest_poster_id) REFERENCES users(user_id);

ALTER TABLE post_images
ADD CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS post_likes (
    `post_like_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `timestamp` TIMESTAMP
);

ALTER TABLE post_likes
ADD CONSTRAINT fk_post_likes_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_likes_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);


CREATE TABLE IF NOT EXISTS post_comment (
    `post_comment_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `comment` VARCHAR(255) DEFAULT NULL,
    `comment_image` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

ALTER TABLE post_comment
ADD CONSTRAINT fk_post_comment_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_comment_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

CREATE TABLE IF NOT EXISTS notifications (
    `notification_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `receiver_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `post_id` BIGINT DEFAULT NULL,
    `notification_type` ENUM('FRIEND_REQUEST', 'FRIEND_ACCEPTED', 'POST_LIKED', 'POST_COMMENTED'),
    `is_read` BOOLEAN DEFAULT FALSE,
    `message` VARCHAR(255) NOT NULL,
    `timestamp` TIMESTAMP
);

ALTER TABLE notifications
ADD CONSTRAINT fk_notifications_receiver_id FOREIGN KEY (receiver_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_notifications_sender_id FOREIGN KEY (sender_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_notifications_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id);

CREATE TABLE IF NOT EXISTS post_images_likes (
    `post_image_like_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_image_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `timestamp` TIMESTAMP
);

ALTER TABLE post_images_likes
ADD CONSTRAINT fk_post_images_likes_post_image_id FOREIGN KEY (post_image_id) references post_images(post_image_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_images_likes_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

CREATE TABLE IF NOT EXISTS post_image_comments (
    `post_image_comment_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_image_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `comment` VARCHAR(255) DEFAULT NULL,
    `comment_image` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

ALTER TABLE post_image_comments
ADD CONSTRAINT fk_post_image_comments_post_image_id FOREIGN KEY (post_image_id) references post_images(post_image_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_image_comments_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

CREATE TABLE IF NOT EXISTS friendship (
    `friendship_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `friend_id` BIGINT NOT NULL,
    `status` ENUM('PENDING', 'FRIENDS'),
    `timestamp` TIMESTAMP
);

ALTER TABLE friendship
ADD CONSTRAINT fk_friendship_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_friendship_friend_id FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS chat (
    `chat_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `chat_type` ENUM('PRIVATE_CHAT', 'GROUP_CHAT'),
    `group_chat_name` VARCHAR(50) DEFAULT NULL,
    `group_chat_image` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS chat_user (
    `user_id` BIGINT NOT NULL,
    `chat_id` BIGINT NOT NULL,
    PRIMARY KEY (user_id, chat_id)
);

ALTER TABLE chat_user
ADD CONSTRAINT fk_chat_user_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_chat_user_chat_id FOREIGN KEY (chat_id) REFERENCES chat(chat_id);

CREATE TABLE IF NOT EXISTS message (
    `message_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `chat_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `message` VARCHAR(255) DEFAULT NULL,
    `message_update` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

ALTER TABLE message
ADD CONSTRAINT fk_message_chat_id FOREIGN KEY (chat_id) REFERENCES chat(chat_id),
ADD CONSTRAINT fk_message_user_id FOREIGN KEY (sender_id) REFERENCES users(user_id);