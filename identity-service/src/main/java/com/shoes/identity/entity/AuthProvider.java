package com.shoes.identity.entity;

/**
 * Phuong thuc dang ky / dang nhap cua user.
 * LOCAL    — dang ky bang email/password (BCrypt hash trong cot password)
 * GOOGLE   — dang nhap bang Google OAuth2
 * FACEBOOK — dang nhap bang Facebook OAuth2
 */
public enum AuthProvider {
    LOCAL,
    GOOGLE,
    FACEBOOK
}
