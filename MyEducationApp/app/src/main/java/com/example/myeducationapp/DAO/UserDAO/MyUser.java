package com.example.myeducationapp.DAO.UserDAO;

/**
 * MyUser 類別，使用 Singleton 模式管理單一使用者。
 */
public class MyUser extends User {

    private static MyUser instance = null;

    /**
     * 建構子，接收使用者物件並呼叫父類別建構子。
     * @param user 代表要使用的使用者物件
     */
    public MyUser(User user) {
        super(user.getId(), user.getName(), user.getEmail(), user.getSex(), user.getPwd(), user.getImgURL());
    }

    /**
     * 取得 MyUser 單例。若尚未建立，則實例化。
     * @param user 代表要使用的使用者物件
     * @return MyUser 單例
     */
    public static MyUser getInstance(User user) {
        if (instance == null) {
            instance = new MyUser(user);
        }
        return instance;
    }

    /**
     * 登出使用者，將實例重設為 null。
     */
    public void logoutUser() {
        instance = null;
    }
}