package cn.ovzv.idioms;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 数据持久化类
 */
public class PushPreferences {

    private static final String NAME = "app_settings";
    private static final String KEY_PRIVACY_AGREEMENT = "privacy_agreement";

    private static volatile PushPreferences instance;

    private final SharedPreferences preferences;

    private PushPreferences(Context context) {
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static PushPreferences getInstance(Context context) {
        if (instance == null) {
            synchronized (PushPreferences.class) {
                if (instance == null) {
                    instance = new PushPreferences(context);
                }
            }
        }
        return instance;
    }

    /**
     * 设置隐私协议是否同意
     *
     * @param value 是否同意
     */
    public void setAgreePrivacyAgreement(boolean value) {
        preferences.edit().putBoolean(KEY_PRIVACY_AGREEMENT, value).apply();
    }

    /**
     * 是否同意了隐私协议
     *
     * @return true 已经同意；false 还没有同意
     */
    public boolean hasAgreePrivacyAgreement() {
        return preferences.getBoolean(KEY_PRIVACY_AGREEMENT, false);
    }

}
