ALTER TABLE posts
ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_shared_image_id FOREIGN KEY (shared_image_id) REFERENCES post_images(post_image_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_shared_post_id FOREIGN KEY (shared_post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_guest_poster_id FOREIGN KEY (guest_poster_id) REFERENCES users(user_id);

ALTER TABLE post_images
ADD CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE;

ALTER TABLE post_likes
ADD CONSTRAINT fk_post_likes_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_likes_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE post_comment
ADD CONSTRAINT fk_post_comment_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_comment_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE notifications
ADD CONSTRAINT fk_notifications_receiver_id FOREIGN KEY (receiver_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_notifications_sender_id FOREIGN KEY (sender_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_notifications_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id);

ALTER TABLE post_images_likes
ADD CONSTRAINT fk_post_images_likes_post_image_id FOREIGN KEY (post_image_id) references post_images(post_image_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_images_likes_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE post_image_comments
ADD CONSTRAINT fk_post_image_comments_post_image_id FOREIGN KEY (post_image_id) references post_images(post_image_id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_image_comments_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE friendship
ADD CONSTRAINT fk_friendship_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_friendship_friend_id FOREIGN KEY (friend_id) REFERENCES users(user_id);

ALTER TABLE chat_user
ADD CONSTRAINT fk_chat_user_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_chat_user_chat_id FOREIGN KEY (chat_id) REFERENCES chat(chat_id);

ALTER TABLE message
ADD CONSTRAINT fk_message_chat_id FOREIGN KEY (chat_id) REFERENCES chat(chat_id),
ADD CONSTRAINT fk_message_user_id FOREIGN KEY (sender_id) REFERENCES users(user_id);