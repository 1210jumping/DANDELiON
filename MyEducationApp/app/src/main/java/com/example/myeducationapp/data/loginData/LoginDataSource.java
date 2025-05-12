package com.example.myeducationapp.data.loginData;

import com.example.myeducationapp.data.loginData.model.LoggedInUser;

import java.io.IOException;

/**
 * 處理登入憑證驗證並擷取使用者資訊的類別。
 */
public class LoginDataSource {

    /**
     * 使用使用者名稱和密碼進行登入。
     *
     * @param username 使用者名稱。
     * @param password 使用者密碼。
     * @return 如果登入成功，則回傳包含 {@link LoggedInUser} 的 {@link Result.Success}；
     *         如果登入失敗，則回傳包含 {@link IOException} 的 {@link Result.Error}。
     */
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: 處理已登入使用者的驗證邏輯
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(), // 隨機產生一個 UUID 作為使用者 ID
                            "Jane Doe"); // 設定預設的顯示名稱
            return new Result.Success<>(fakeUser); // 回傳成功的結果，並附帶假使用者物件
        } catch (Exception e) {
            // 如果在登入過程中發生任何例外，則回傳錯誤結果
            return new Result.Error(new IOException("登入時發生錯誤", e));
        }
    }

    /**
     * 登出目前使用者。
     */
    public void logout() {
        // TODO: 撤銷驗證的邏輯
    }
}