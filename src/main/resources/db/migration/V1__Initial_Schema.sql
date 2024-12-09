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

CREATE TABLE IF NOT EXISTS post_likes (
    `post_like_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `timestamp` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS post_comment (
    `post_comment_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `comment` VARCHAR(255) DEFAULT NULL,
    `comment_image` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

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

CREATE TABLE IF NOT EXISTS post_images_likes (
    `post_image_like_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_image_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `timestamp` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS post_image_comments (
    `post_image_comment_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_image_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `comment` VARCHAR(255) DEFAULT NULL,
    `comment_image` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS friendship (
    `friendship_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `friend_id` BIGINT NOT NULL,
    `status` ENUM('PENDING', 'FRIENDS'),
    `timestamp` TIMESTAMP
);

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

CREATE TABLE IF NOT EXISTS message (
    `message_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `chat_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `message` VARCHAR(255) DEFAULT NULL,
    `message_update` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS story (
    `story_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `text` VARCHAR(255) DEFAULT NULL,
    `story_image` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    `product_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `category` ENUM('VEHICLES', 'ELECTRONICS', 'APPAREL', 'TOYS_AND_GAMES', 'HOME_SALES', 'ENTERTAINMENT', 'SPORTS', 'OFFICE_SUPPLIES', 'MUSICAL_INSTRUMENTS') NOT NULL,
    `product_condition` ENUM('NEW', 'GOOD', 'FAIR') NOT NULL,
    `availability` ENUM('SINGLE', 'IN_STOCK') NOT NULL,
    `product_name` VARCHAR(100) NOT NULL,
    `price` DOUBLE NOT NULL,
    `brand` VARCHAR(50) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `timestamp` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_images (
    `product_image_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `product_image` VARCHAR(255) DEFAULT NULL
);
