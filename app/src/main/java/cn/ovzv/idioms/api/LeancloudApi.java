package cn.ovzv.idioms.api;

import java.util.HashMap;
import java.util.Map;

import cn.leancloud.LCCloud;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LeancloudApi {

    public static final String APP_ID = "Ao3V1A0vUSegwa8uPi8zyVWo-9Nh9j0Va";
    public static final String APP_KEY = "Eq7sgbHSUr8b3jKS7K48Hgp6";
    public static final String APP_MasterKey = "rcI6nc7mwf7QixyNvhIPdJ3o";

    public static final String REST_API = "https://idiom-api.wxiou.cn";
    public static final String FileUpload_API = "https://upload.qiniup.com";
    public static final String File_Url = "https://idiom-file.wxiou.cn";

    /** 提交反馈意见 **/
    public static void Feedback_save(String str){
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("feedback", str );
        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("Feedback_save", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
