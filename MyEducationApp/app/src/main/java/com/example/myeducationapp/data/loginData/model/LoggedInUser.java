package com.example.myeducationapp.data.loginData.model;

/**
 * 資料類別，用於擷取從 LoginRepository 檢索到的已登入使用者的使用者資訊。
 */
public class LoggedInUser {

    private String userId; // 使用者 ID
    private String displayName; // 使用者顯示名稱

    /**
     * LoggedInUser 的建構子。
     *
     * @param userId      使用者的唯一識別碼。
     * @param displayName 使用者在應用程式中顯示的名稱。
     */
    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    /**
     * 獲取使用者的 ID。
     *
     * @return 使用者的 ID 字串。
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 獲取使用者的顯示名稱。
     *
     * @return 使用者的顯示名稱字串。
     */
    public String getDisplayName() {
        return displayName;
    }
}