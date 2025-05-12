package com.example.myeducationapp.data.loginData;

import com.example.myeducationapp.data.loginData.model.LoggedInUser;

/**
 * 類別，用於從遠端資料來源請求驗證和使用者資訊，
 * 並在記憶體中維護登入狀態和使用者憑證資訊的快取。
 */
public class LoginRepository {

    // LoginRepository 的靜態實例 (Singleton 模式)
    private static volatile LoginRepository instance;

    // 資料來源，用於處理實際的登入邏輯
    private LoginDataSource dataSource;

    // 如果使用者憑證將被快取在本地儲存空間，建議對其進行加密
    // @see https://developer.android.com/training/articles/keystore
    // 目前登入的使用者物件，如果未登入則為 null
    private LoggedInUser user = null;

    // 私有建構子：用於 Singleton 模式的存取
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 獲取 LoginRepository 的唯一實例 (Singleton 模式)。
     *
     * @param dataSource 資料來源。
     * @return LoginRepository 的實例。
     */
    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    /**
     * 檢查使用者是否已登入。
     *
     * @return 如果使用者已登入則回傳 true，否則回傳 false。
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    /**
     * 登出目前使用者。
     * 清除快取的使用者資訊並呼叫資料來源的登出方法。
     */
    public void logout() {
        user = null; // 清除記憶體中的使用者資訊
        dataSource.logout(); // 通知資料來源執行登出操作
    }

    /**
     * 設定目前登入的使用者。
     *
     * @param user 已登入的使用者物件。
     */
    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // 如果使用者憑證將被快取在本地儲存空間，建議對其進行加密
        // @see https://developer.android.com/training/articles/keystore
    }

    /**
     * 執行登入操作。
     * 此方法會呼叫資料來源的登入方法，並在成功時快取使用者資訊。
     *
     * @param username 使用者名稱。
     * @param password 使用者密碼。
     * @return 包含登入結果的 {@link Result} 物件。如果成功，則為 {@link Result.Success} 並包含 {@link LoggedInUser}；
     *         如果失敗，則為 {@link Result.Error}。
     */
    public Result<LoggedInUser> login(String username, String password) {
        // 處理登入邏輯
        Result<LoggedInUser> result = dataSource.login(username, password);
        // 如果登入成功
        if (result instanceof Result.Success) {
            // 設定並快取已登入的使用者資訊
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result; // 回傳登入結果
    }
}