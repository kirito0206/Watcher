package com.example.watcher.Retrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.watcher.data.AreaTree;
import com.example.watcher.data.Children;
import com.example.watcher.data.ChinaDay;
import com.example.watcher.data.Comment;
import com.example.watcher.data.CommentData;
import com.example.watcher.data.Subcomment;
import com.example.watcher.data.User;
import com.example.watcher.ui.LoginActivity;
import com.example.watcher.ui.MyApplication;
import com.example.watcher.data.Region;
import com.example.watcher.ui.SubCommentActivity;
import com.example.watcher.ui.discuss.DiscussFragment;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private static RetrofitUtil instance;
    private static Region dataMain;
    private static CommentData commentData;
    public boolean receiveFlag = false;
    public static String BASE_URL = "http://101.200.157.252/api/";

    public RetrofitUtil() {
        initData();
        updateComments(0);
    }

    public void initData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://c.m.163.com/ug/api/wuhan/app/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        // 创建 网络请求接口 的实例
        WatcherService request = retrofit.create(WatcherService.class);
        //对 发送请求 进行封装
        Call<Region> call = request.getCall();
        call.enqueue(new Callback<Region>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                //请求处理,输出结果
                dataMain = response.body();
                StringBuilder result = new StringBuilder();
                //采取Gson通过user.getContent()的各种方法调用
                ///可以定义内部类的方式获得子元素的数组
                result.append("返回码："+dataMain.getCode());
                if (dataMain.getCode() == 10000){
                    receiveFlag = true;
                    Collections.sort(dataMain.getData().getAreaTree(),new Comparator<AreaTree>() {
                        @Override
                        public int compare(AreaTree o1, AreaTree o2) {
                            int x = o2.getTotal().getConfirm() - o2.getTotal().getHeal() - o2.getTotal().getDead();
                            int y = o1.getTotal().getConfirm() - o1.getTotal().getHeal() - o1.getTotal().getDead();
                            return x-y;
                        }
                    });

                }
                else
                    Toast.makeText(MyApplication.getContext(),"请求异常，请重试！！",Toast.LENGTH_SHORT).show();
                Log.d("MainActivity",result.toString());
            }
            //请求失败时候的回调
            @Override
            public void onFailure(Call<Region> call, Throwable throwable) {
                Log.d("MainActivity","连接失败");
            }
        });
    }

    public static RetrofitUtil getInstance() {
        if (instance == null)
            instance = new RetrofitUtil();
        return instance;
    }

    public List<Integer> getChinaTotalData(){
        List<Integer> totalList = new ArrayList<>();
        //"累计确诊", "现有确诊", "境外输入确诊", "疑似", "死亡", "治愈"
        int confirm = dataMain.getData().getChinaTotal().getTotal().getConfirm();
        int input = dataMain.getData().getChinaTotal().getTotal().getInput();
        int suspect = dataMain.getData().getChinaTotal().getTotal().getSuspect();
        int dead = dataMain.getData().getChinaTotal().getTotal().getDead();
        int heal = dataMain.getData().getChinaTotal().getTotal().getHeal();
        int confirmLeft = confirm-dead-heal;
        totalList.add(confirm);
        totalList.add(confirmLeft);
        totalList.add(input);
        totalList.add(suspect);
        totalList.add(dead);
        totalList.add(heal);
        return totalList;
    }

    public List<Integer> getChinaTodayData(){
        List<Integer> todayList = new ArrayList<>();
        //"累计确诊", "现有确诊", "境外输入确诊", "疑似", "死亡", "治愈"
        int confirm = dataMain.getData().getChinaTotal().getToday().getConfirm();
        int input = dataMain.getData().getChinaTotal().getToday().getInput();
        int suspect = dataMain.getData().getChinaTotal().getToday().getSuspect();
        int dead = dataMain.getData().getChinaTotal().getToday().getDead();
        int heal = dataMain.getData().getChinaTotal().getToday().getHeal();
        int confirmLeft = confirm-dead-heal;
        todayList.add(confirm);
        todayList.add(confirmLeft);
        todayList.add(input);
        todayList.add(suspect);
        todayList.add(dead);
        todayList.add(heal);
        return todayList;
    }

    public List<Children> getProvinceList(){
        for (AreaTree areaTree : dataMain.getData().getAreaTree()){
            if (areaTree.getName().equals("中国")){
                return areaTree.getChildren();
            }
        }
        return new ArrayList<>();
    }

    public List<String> getChinaDaysDate(){
        List<String> list = new ArrayList<>();
        for (ChinaDay chinaDay : dataMain.getData().getChinaDayList()){
            list.add(chinaDay.getDate());
        }
        return list;
    }

    public List<Entry> getChinaDaysConfirm(){
        List<Entry> list = new ArrayList<>();
        float flag = 1;
        for (ChinaDay chinaDay : dataMain.getData().getChinaDayList()){
            float d = new Integer(chinaDay.getTotal().getConfirm()).floatValue();
            list.add(new Entry(flag,d));
            flag += 1;
        }
        return list;
    }

    public List<Entry> getChinaDaysHeal(){
        List<Entry> list = new ArrayList<>();
        float flag = 1;
        for (ChinaDay chinaDay : dataMain.getData().getChinaDayList()){
            float d = new Integer(chinaDay.getTotal().getHeal()).floatValue();
            list.add(new Entry(flag,d));
            flag += 1;
        }
        return list;
    }

    public List<Entry> getChinaDaysDead(){
        List<Entry> list = new ArrayList<>();
        float flag = 1;
        for (ChinaDay chinaDay : dataMain.getData().getChinaDayList()){
            float d = new Integer(chinaDay.getTotal().getDead()).floatValue();
            list.add(new Entry(flag,d));
            flag += 1;
        }
        return list;
    }

    public List<Entry> getChinaDaysConfirmLeft(){
        List<Entry> list = new ArrayList<>();
        float flag = 1;
        for (ChinaDay chinaDay : dataMain.getData().getChinaDayList()){
            float d = new Integer(chinaDay.getTotal().getConfirm() - chinaDay.getTotal().getDead() - chinaDay.getTotal().getHeal()).floatValue();
            list.add(new Entry(flag,d));
            flag += 1;
        }
        return list;
    }

    public List<Integer> getWorldTotalData(){
        List<Integer> list = new ArrayList<>();
        //累计确诊，现有确诊，治愈，死亡
        list.add(0);list.add(0);list.add(0);list.add(0);
        for (AreaTree areaTree : dataMain.getData().getAreaTree()){
            list.set(0,list.get(0)+areaTree.getTotal().getConfirm());
            list.set(2,list.get(2)+areaTree.getTotal().getHeal());
            list.set(3,list.get(3)+areaTree.getTotal().getDead());
        }
        list.set(1,list.get(0)-list.get(2)-list.get(3));
        return list;
    }

    public List<Integer> getWorldTodayData(){
        List<Integer> list = new ArrayList<>();
        //累计确诊，现有确诊，治愈，死亡
        list.add(0);list.add(0);list.add(0);list.add(0);
        for (AreaTree areaTree : dataMain.getData().getAreaTree()){
            list.set(0,list.get(0)+areaTree.getToday().getConfirm());
            list.set(2,list.get(2)+areaTree.getToday().getHeal());
            list.set(3,list.get(3)+areaTree.getToday().getDead());
        }
        list.set(1,list.get(0)-list.get(2)-list.get(3));
        return list;
    }

    public List<AreaTree> getCountriesData(int n){
        List<AreaTree> areaTreeList = new ArrayList<>();
        for (int i = 0;i < 20;i++){
            if (n*20+i >= dataMain.getData().getAreaTree().size())
                break;
            areaTreeList.add(dataMain.getData().getAreaTree().get(n*20+i));
        }
        return areaTreeList;
    }

    public Children getProvinceDetail(String name){
        for (AreaTree areaTree : dataMain.getData().getAreaTree()){
            if (areaTree.getName().equals("中国")){
                for (Children children : areaTree.getChildren()){
                    if (name.equals(children.getName())){
                        return children;
                    }
                }
                break;
            }
        }
        return null;
    }

    public AreaTree getCountryDetail(String name){
        for (AreaTree areaTree : dataMain.getData().getAreaTree()){
            if (areaTree.getName().equals(name)){
                return areaTree;
            }
        }
        return null;
    }

    public List<String> getCountriesName(){
        List<String> list = new ArrayList<>();
        for (int i = 0;i < 8;i++){
            AreaTree areaTree = dataMain.getData().getAreaTree().get(i);
            list.add(areaTree.getName());
        }
        return list;
    }
    public List<BarEntry> getCountriesConfirm(){
        List<BarEntry> list = new ArrayList<>();
        for (int i = 0;i < 8;i++){
            AreaTree areaTree = dataMain.getData().getAreaTree().get(i);
            list.add(new BarEntry(i,areaTree.getTotal().getConfirm()));
        }
        return list;
    }
    public List<BarEntry> getCountriesHeal(){
        List<BarEntry> list = new ArrayList<>();
        for (int i = 0;i < 8;i++){
            AreaTree areaTree = dataMain.getData().getAreaTree().get(i);
            list.add(new BarEntry(i,areaTree.getTotal().getHeal()));
        }
        return list;
    }
    public List<BarEntry> getCountriesDead(){
        List<BarEntry> list = new ArrayList<>();
        for (int i = 0;i < 8;i++){
            AreaTree areaTree = dataMain.getData().getAreaTree().get(i);
            list.add(new BarEntry(i,areaTree.getTotal().getDead()));
        }
        return list;
    }

    public void registerUser(String name,String passwd){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            obj.put("username", name);//添加相应键值对
            obj.put("password", passwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.callRegister(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    Message msg = new Message();
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        msg.what = 1;
                        User.getInstance().setUserName(name);
                        User.getInstance().setPasswd(passwd);
                        User.getInstance().setToken(object.getString("token"));
                    }else if (status == 1002){
                        msg.what = 2;
                    }else{
                        msg.what = 0;
                    }
                    LoginActivity.mHandler.sendMessage(msg);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public void loginUser(String name,String passwd){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            obj.put("username", name);//添加相应键值对
            obj.put("password", passwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.callLogin(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    Message msg = new Message();
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        msg.what = 1;
                        User.getInstance().setUserName(name);
                        User.getInstance().setPasswd(passwd);
                        User.getInstance().setToken(object.getString("token"));
                    }else if (status == 1004){
                        msg.what = 3;
                    }else{
                        msg.what = 0;
                    }
                    LoginActivity.mHandler.sendMessage(msg);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public void sendMainComment(String text){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            String token = User.getInstance().getToken();
            obj.put("token", token);//添加相应键值对
            obj.put("text",text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.sendMainComment(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        String token = object.getString("token");
                        User.getInstance().setToken(token);
                        updateComments(1);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public void sendSubComment(String text, Integer parentId){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            String token = User.getInstance().getToken();
            obj.put("token", token);//添加相应键值对
            obj.put("text",text);
            obj.put("parentid",parentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.sendSubComment(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        String token = object.getString("token");
                        User.getInstance().setToken(token);
                        updateComments(2);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public void deleteMainComment(Integer commentId){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            String token = User.getInstance().getToken();
            obj.put("token", token);//添加相应键值对
            obj.put("commentid",commentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.deleteComment(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        String token = object.getString("token");
                        User.getInstance().setToken(token);
                        updateComments(3);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public void deleteSubComment(Integer subCommentId){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            String token = User.getInstance().getToken();
            obj.put("token", token);//添加相应键值对
            obj.put("commentid",subCommentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.deleteSubComment(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        String token = object.getString("token");
                        User.getInstance().setToken(token);
                        updateComments(4);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public void updateComments(int flag){
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();
        final WatcherService request = retrofit.create(WatcherService.class);
        Call<CommentData> data = request.getComments();
        data.enqueue(new Callback<CommentData>() {
            @Override
            public void onResponse(Call<CommentData> call, Response<CommentData> response) {
                CommentData com = response.body();

                if (com.getStatus() == 0){
                    commentData = com;
                    Message msg = new Message();
                    if (flag == 1){
                        msg.what = 1;
                        DiscussFragment.mHandler.sendMessage(msg);
                    }else if (flag == 2){
                        msg.what = 1;
                        SubCommentActivity.mHandler.sendMessage(msg);
                    }else if (flag == 3){
                        msg.what = 2;
                        DiscussFragment.mHandler.sendMessage(msg);
                    }else if (flag == 4){
                        msg.what = 2;
                        SubCommentActivity.mHandler.sendMessage(msg);
                    }
                    //下拉刷新
                    else if (flag == 5){
                        msg.what = 3;
                        DiscussFragment.mHandler.sendMessage(msg);
                    }
                }
            }
            @Override
            public void onFailure(Call<CommentData> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public List<Comment> getComments(){
        updateComments(0);
        List<Comment> list = commentData.getData().getComments();
        return list;
    }

    public List<Subcomment> getSubComments(Integer parentId){
        for (Comment comment : commentData.getData().getComments()){
            if (comment.getCommentid() == parentId){
                return comment.getSubcomments();
            }
        }
        return new ArrayList<>();
    }

    public void starMainComment(Integer commentId){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            String token = User.getInstance().getToken();
            obj.put("token", token);//添加相应键值对
            obj.put("commentid",commentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.starComment(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    //点赞成功
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        String token = object.getString("token");
                        User.getInstance().setToken(token);
                        updateComments(0);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public void starSubComment(Integer subCommentId){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            String token = User.getInstance().getToken();
            obj.put("token", token);//添加相应键值对
            obj.put("commentid",subCommentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.starSubComment(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    //点赞成功
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        String token = object.getString("token");
                        User.getInstance().setToken(token);
                        updateComments(0);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }

    public void reLogin(){
        JSONObject obj = new JSONObject();//服务器需要传参的json对象
        try{
            obj.put("username", User.getInstance().getUserName());//添加相应键值对
            obj.put("password", User.getInstance().getPasswd());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL).build();
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf(obj));
        final WatcherService login = retrofit.create(WatcherService.class);
        Call<ResponseBody> data = login.callLogin(body);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject rootObject = new JSONObject(response.body().string());
                    int status = rootObject.getInt("status");
                    if (status == 0){
                        JSONObject object = rootObject.getJSONObject("data");
                        User.getInstance().setToken(object.getString("token"));
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.d("132",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("132", "onResponse: --err--"+t.toString());
            }
        });
    }
}
