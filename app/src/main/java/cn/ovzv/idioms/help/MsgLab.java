package cn.ovzv.idioms.help;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;
public class MsgLab {
    public static List<Msg> generateMockList(JSONArray DataJSONArray) {
        List<Msg> msgList = new ArrayList<>();
        //利用泛型，以及上一个文件来读取出我们所需要的内容
        for(int i = 0; i< DataJSONArray.size();i++) {
            Msg msg = new Msg(i,
                    GetHttpBitmap.getHttpBitmap(DataJSONArray.getJSONObject(i).getString("thumb")),
                    DataJSONArray.getJSONObject(i).getString("name"),
                    DataJSONArray.getJSONObject(i).getInteger("count"));
            msgList.add(msg);
        }
        return msgList;
    }

}
