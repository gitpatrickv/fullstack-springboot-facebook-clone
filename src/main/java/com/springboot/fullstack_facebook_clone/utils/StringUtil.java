package com.springboot.fullstack_facebook_clone.utils;

public class StringUtil {
    public static final String PHOTO_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/facebook-clone/";
// public static final String PHOTO_DIRECTORY = "/app/images/facebook-clone/";
// make a directory images/facebook-clone to solve the image problem when running docker
    public static final String USER_NOT_FOUND = "User not found";
    public static final String INVALID_CREDENTIALS = "Invalid username or password!";
    public static final String ERROR_MESSAGE = "We encountered an unexpected problem. Please try again later.";
    public static final String USER_EXISTS = "User already exists: ";
    public static final String TIMESTAMP = "timestamp";
    public static final String SIZE_EXCEEDED = "File size exceeds the maximum allowed limit";
    public static final String X_REQUESTED_WITH = "X-Requested-With";
    public static final String FRIEND_REQUEST_NOT_ALLOWED = "You can't send a friend request to yourself.";
    public static final String FRIEND_REQUEST_ALREADY_EXISTS = "User already sent a friend request";
    public static final String LIKES_YOUR_POST = "likes your post";
    public static final String FRIEND_REQUEST_NOT_FOUND = "Friend request not found";
    public static final String NOT_GROUP_CHAT = "Chat is not of type GROUP_CHAT";

}
