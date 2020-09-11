package com.example.watcher.data;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.example.watcher.R;
import com.example.watcher.ui.MyApplication;
import com.example.watcher.Retrofit.RetrofitUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapView extends View {

    private final Map<String, String> provinceId = new HashMap<String,String>(){
        {
             put("11", "北京"); put("12", "天津"); put("13", "河北");
             put("14", "山西"); put("15", "内蒙古"); put("21", "辽宁");
             put("22", "吉林"); put("23", "黑龙江"); put("31", "上海");
             put("32", "江苏"); put("33", "浙江"); put("34", "安徽");
             put("35", "福建"); put("36", "江西"); put("37", "山东");
             put("41", "河南"); put("42", "湖北"); put("43", "湖南");
             put("44", "广东"); put("45", "广西"); put("46", "海南");
             put("50", "重庆"); put("51", "四川"); put("52", "贵州");
             put("53", "云南"); put("54", "西藏"); put("61", "陕西");
             put("62", "甘肃"); put("63", "青海"); put("64", "宁夏");
             put("65", "新疆"); put("91", "澳门"); put("92","香港");
             put("71","台湾");
        }
    };
    private final Map<String,String> countryId = new HashMap<String, String>(){
            {
                put("AD","安道尔");
                put("AE","阿联酋");
                put("AF","阿富汗");
                put("AL","阿尔巴尼亚");
                put("AM","亚美尼亚");
                put("AN","荷兰大小安第列斯群岛");
                put("AO","安哥拉");
                put("AQ","南极洲");
                put("AR","阿根廷");
                put("AS","美洲萨摩亚");
                put("AT","奥地利");
                put("AU","澳大利亚");
                put("AW","阿卢巴岛");
                put("AZ","阿塞拜疆");
                put("BA","波斯尼亚");
                put("BB","巴贝多");
                put("BD","孟加拉国");
                put("BE","比利时");
                put("BF","布基那法索");
                put("BG","保加利亚");
                put("BH","巴林");
                put("BI","蒲隆地");
                put("BJ","百慕达");
                put("BM","百慕达");
                put("BN","汶莱达累斯萨拉姆");
                put("BO","玻利维亚");
                put("BR","巴西");
                put("BS","巴哈马");
                put("BT","Buthan");
                put("BV","Island");
                put("BW","博茨瓦纳");
                put("BY","白俄罗斯");
                put("BZ","贝里斯");
                put("CA","加拿大");
                put("CC","椰子岛");
                put("CD","刚果（金）");
                put("CF","中非共和国");
                put("CG","刚果");
                put("CH","瑞士");
                put("CI","科特迪瓦");
                put("CK","Islands");
                put("CL","智利");
                put("CM","喀麦隆");
                put("CN","中国");
                put("CO","安哥伦比亚");
                put("CR","哥斯达黎加");
                put("CS","捷克斯拉夫");
                put("CU","古巴");
                put("CV","维德角");
                put("CX","圣诞岛");
                put("CY","塞普勒斯");
                put("CZ","捷克");
                put("DE","德国");
                put("DJ","吉布地");
                put("DK","丹麦");
                put("DM","多米尼克");
                put("DO","多明尼加共和国");
                put("DZ","阿尔及利亚");
                put("EC","厄瓜多尔");
                put("EE","爱沙尼亚");
                put("EG","埃及");
                put("EH","西撒哈拉");
                put("ER","厄立特里亚");
                put("ES","西班牙");
                put("ET","埃塞俄比亚");
                put("FI","芬兰");
                put("FJ","斐济");
                put("FK","福克兰群岛");
                put("FM","密克罗西尼亚");
                put("FO","法罗群岛");
                put("FR","法国");
                put("FX","法国（欧洲）");
                put("GA","加彭");
                put("GB","英国");
                put("GD","格瑞纳达");
                put("GE","美国乔治亚洲");
                put("GH","加纳");
                put("GI","直布罗陀");
                put("GL","格陵兰");
                put("GP","哥德洛普");
                put("GQ","赤道几内亚");
                put("GF","盖亚那");
                put("GM","甘比亚");
                put("GN","几内亚");
                put("GR","希腊");
                put("GS","美国乔治亚及三");
                put("GT","瓜地马拉");
                put("GU","关岛");
                put("GW","几内亚比索");
                put("GY","盖亚那");
                put("HK","香港");
                put("HM","Isl.");
                put("HN","宏都拉斯");
                put("HR","克罗埃西亚共和国");
                put("HT","海地");
                put("HU","匈牙利");
                put("ID","印度尼西亚");
                put("IE","爱尔兰");
                put("IL","以色列");
                put("IN","印度");
                put("IO","Terr.");
                put("IQ","伊拉克");
                put("IR","伊朗");
                put("IS","冰岛");
                put("IT","意大利");
                put("JM","牙买加");
                put("JO","约旦");
                put("JP","日本");
                put("KE","肯尼亚");
                put("KG","吉尔吉斯斯坦");
                put("KH","高棉");
                put("KI","吉里巴斯共和国");
                put("KM","科摩洛");
                put("KN","Anguilla");
                put("KP","朝鲜");
                put("KR","韩国");
                put("KW","科威特");
                put("KY","鳄鱼岛");
                put("KZ","哈萨克斯坦");
                put("LA","寮国");
                put("LB","黎巴嫩");
                put("LC","Lucia");
                put("LI","列支敦斯登");
                put("LK","斯里兰卡");
                put("LR","赖比瑞亚");
                put("LS","赖索托");
                put("LT","立陶宛");
                put("LU","卢森堡");
                put("LV","拉脱维亚");
                put("LY","利比亚");
                put("MA","摩洛哥");
                put("MC","摩纳哥");
                put("MD","摩尔达维亚");
                put("MG","马达加斯加");
                put("MH","马绍尔群岛");
                put("MK","马其顿");
                put("ML","马里");
                put("MM","缅甸");
                put("MN","蒙古");
                put("MO","澳门");
                put("MP","Isl.");
                put("MQ","(Fr.)");
                put("MR","毛里塔尼亚");
                put("MS","蒙皮立");
                put("MT","马尔他");
                put("MU","模尼西斯");
                put("MV","马尔地夫");
                put("MW","马拉维");
                put("MX","墨西哥");
                put("MY","马来西亚");
                put("MZ","莫桑比克");
                put("NA","纳米比亚");
                put("NC","新加勒多尼亚");
                put("NE","尼日利亚");
                put("NF","诺福克岛");
                put("NG","尼日利亚");
                put("NI","尼加拉瓜");
                put("NL","荷兰");
                put("NO","挪威");
                put("NP","尼泊尔");
                put("NR","诺鲁");
                put("NT","Zone");
                put("NU","Niue");
                put("NZ","新西兰");
                put("OM","阿曼");
                put("PA","巴拿马");
                put("PE","秘鲁");
                put("PF","玻里尼西亚");
                put("PG","巴布新几内亚");
                put("PH","菲律宾");
                put("PK","巴基斯坦");
                put("PL","波兰");
                put("PM","圣皮耳米开朗基罗");
                put("PN","Pitcairn");
                put("PT","葡萄牙");
                put("PR","波多黎各");
                put("PW","Palau");
                put("PY","巴拉圭");
                put("QA","卡达");
                put("RE","留尼旺岛");
                put("RO","罗马尼亚");
                put("RU","俄罗斯");
                put("RW","卢安达");
                put("SA","沙特阿拉伯");
                put("SB","所罗门群岛");
                put("SC","塞席尔群岛");
                put("SD","苏丹");
                put("SE","瑞典");
                put("SG","新加坡");
                put("SH","圣海伦娜");
                put("SI","斯洛维尼亚共和国");
                put("SJ","Is");
                put("SK","斯洛伐克");
                put("SL","狮子山");
                put("SM","圣马利诺");
                put("SN","塞内加尔");
                put("SO","索马里");
                put("SR","苏利南莫河");
                put("ST","Principe");
                put("SU","前苏联首都");
                put("SV","萨尔瓦多");
                put("SY","叙利亚");
                put("SZ","史瓦济兰");
                put("TC","Islands");
                put("TD","乍得");
                put("TF","Terr.");
                put("TG","多哥");
                put("TH","泰国");
                put("TJ","塔吉克斯坦");
                put("TK","Tokelau");
                put("TM","土库曼");
                put("TN","突尼西亚");
                put("TO","东加");
                put("TP","东帝汶");
                put("TR","土耳其");
                put("TT","千里达托贝哥");
                put("TV","吐瓦鲁");
                put("TW","台湾");
                put("TZ","坦桑尼亚");
                put("UA","乌克兰");
                put("UG","乌干达");
                put("UK","大英联合王国");
                put("UM","Isl.");
                put("US","美国");
                put("UY","乌拉圭");
                put("UZ","乌兹别克斯坦");
                put("VA","梵蒂冈城");
                put("VC","Grenadines");
                put("VE","委内瑞拉");
                put("VG","维尔京群岛（英国）");
                put("VI","维尔京群岛（美国）");
                put("VN","越南");
                put("VU","瓦那图");
                put("WF","Islands");
                put("WS","萨摩亚");
                put("YE","叶门");
                put("YU","南斯拉夫");
                put("ZA","南非");
                put("ZM","赞比亚");
                put("ZR","萨伊");
                put("ZW","津巴布韦");
            }
    };
    private ExecutorService mThreadPool;
    private Paint mPaint;
    private int mMapResId = -1;
    private List<PathItem> mItemList;
    private RectF mMaxRect;
    //现有确诊范围 1-9,10-99,100-999,1000-9999,10000
    private int[] mColorArray = new int[] { Color.parseColor("#FFFFFF"),Color.parseColor("#cc6633"),Color.parseColor("#cc3300"), Color.parseColor("#cc0000"),Color.parseColor("#990000"),Color.parseColor("#660000") };
    private PathItem mSelectItem;
    private float mScale = 1f;
    private Paint rectPaint;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 初始化线程池
        initThreadPool();
        // 解析自定义属性
        getMapResource(context, attrs, defStyleAttr);
        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.FILL);   // 设置样式
    }

    private void initThreadPool() {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(Thread.MAX_PRIORITY);
                return thread;
            }
        };
        mThreadPool = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(10), threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    private void getMapResource(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MapView, defStyleAttr, 0);
        int resId = a.getResourceId(R.styleable.MapView_map, -1);
        a.recycle();
        setMapResId(resId);
    }

    /**
     * 设置地图资源Id
     */
    public void setMapResId(int resId) {
        mMapResId = resId;
        executeLoad();
    }

    private void executeLoad() {
        if (mMapResId <= 0) {
            return;
        }

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                // 获取xml文件输入流
                InputStream inputStream = getResources().openRawResource(mMapResId);
                // 创建解析实例
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                try {
                    builder = factory.newDocumentBuilder();
                    // 解析输入流，得到Document实例
                    Document doc = builder.parse(inputStream);
                    // 获取根节点，即vector节点
                    Element rootElement = doc.getDocumentElement();
                    // 获取所有的path节点
                    NodeList items = rootElement.getElementsByTagName("path");

                    // 以下四个变量用来保存地图四个边界，用于确定缩放比例(适配屏幕)
                    float left = -1;
                    float right = -1;
                    float top = -1;
                    float bottom = -1;

                    // 解析path节点
                    List<PathItem> list = new ArrayList<>();
                    for (int i = 0; i < items.getLength(); ++i) {
                        Element element = (Element) items.item(i);
                        // 获取pathData内容
                        String pathData = element.getAttribute("android:pathData");
                        String name = element.getAttribute("android:name");
                        // 将pathData转换为path
                        Path path = PathParser.createPathFromPathData(pathData);
                        // 封装成PathItem对象
                        PathItem pathItem = new PathItem(path,getName(name));
                        pathItem.setDrawColor(mColorArray[getColor(name)]);
                        RectF rectF = new RectF();
                        // 计算当前path区域的矩形边界
                        path.computeBounds(rectF, true);
                        // 判断边界，最终获得的就是整个地图的最大矩形边界
                        left = left < 0 ? rectF.left : Math.min(left, rectF.left);
                        right = Math.max(right, rectF.right);
                        top = top < 0 ? rectF.top : Math.min(top, rectF.top);
                        bottom = Math.max(bottom, rectF.bottom);

                        list.add(pathItem);
                    }
                    // 解析完成，保存节点列表和最大边界
                    mItemList = list;
                    mMaxRect = new RectF(left, top, right, bottom);
                    // 通知重新布局和绘制
                    post(new Runnable() {
                        @Override
                        public void run() {
                            requestLayout();
                            invalidate();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mThreadPool != null) {
            // 释放线程池
            mThreadPool.shutdown();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mMaxRect != null) {
            // 获取缩放比例
            double mapWidth = mMaxRect.width();
            mScale = (float) (width / mapWidth);
        }

        // 应用测量数据
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mItemList != null) {
            // 使地图从画布左上角开始绘制（图片本身可能存在边距）
            canvas.translate(-mMaxRect.left, -mMaxRect.top);
            // 设置画布缩放，以(mMaxRect.left, mMaxRect.top)为基准进行缩放
            // 因为当前该点对应屏幕左上(0, 0)点
            canvas.scale(mScale, mScale, mMaxRect.left, mMaxRect.top);
            // 绘制所有省份区域，并设置是否选中状态
            for (PathItem pathItem : mItemList) {
                pathItem.drawItem(canvas, mPaint, mSelectItem == pathItem );
            }
            //现有确诊范围 1-9,10-99,100-999,1000-9999,10000
            rectPaint.setColor(mColorArray[1]);
            canvas.drawRect(20,MyApplication.dp2px(180),30,MyApplication.dp2px(180)+10,rectPaint);
            rectPaint.setColor(mColorArray[2]);
            canvas.drawRect(20, MyApplication.dp2px(190),30,MyApplication.dp2px(190)+10,rectPaint);
            rectPaint.setColor(mColorArray[3]);
            canvas.drawRect(20, MyApplication.dp2px(200),30,MyApplication.dp2px(200)+10,rectPaint);
            rectPaint.setColor(mColorArray[4]);
            canvas.drawRect(20, MyApplication.dp2px(210),30,MyApplication.dp2px(210)+10,rectPaint);
            rectPaint.setColor(mColorArray[5]);
            canvas.drawRect(20, MyApplication.dp2px(220),30,MyApplication.dp2px(220)+10,rectPaint);
            rectPaint.setColor(Color.BLACK);
            rectPaint.setTextSize(20f);
            canvas.drawText("1-9",35,MyApplication.dp2px(180)+10,rectPaint);
            canvas.drawText("10-99",35,MyApplication.dp2px(190)+10,rectPaint);
            canvas.drawText("100-999",35,MyApplication.dp2px(200)+10,rectPaint);
            canvas.drawText("1000-9999",35,MyApplication.dp2px(210)+10,rectPaint);
            canvas.drawText(">=10000",35,MyApplication.dp2px(220)+10,rectPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将事件分发给所有的区块，如果事件未被消费，则调用View的onTouchEvent，这里会默认范围false
        if (handleTouch((int) (event.getX() / mScale + mMaxRect.left), (int) (event.getY() / mScale + mMaxRect.top), event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 派发触摸事件
     */
    private boolean handleTouch(int x, int y, MotionEvent event) {
        if (mItemList == null) {
            return false;
        }

        boolean isTouch = false;

        PathItem selectItem = null;
        for (PathItem pathItem : mItemList) {
            // 依次派发事件
            if (pathItem.isTouch(x, y, event)) {
                // 选中省份区块
                selectItem = pathItem;
                isTouch = true;
                break;
            }
        }

        if (selectItem != null && selectItem != mSelectItem) {
            mSelectItem = selectItem;

            // 通知重绘
            postInvalidate();
        }
        return isTouch;
    }

    private int getColor(String name){
        int num = 0;
        String[] arr = name.split("-");
        //中国省份
        if (arr.length == 2){
            Children children = RetrofitUtil.getInstance().getProvinceDetail(provinceId.get(arr[1]));
            if (children == null)
                return 0;
            num = children.getTotal().getConfirm() - children.getTotal().getDead() - children.getTotal().getHeal();
        }
        //国家
        else {
            AreaTree areaTree = RetrofitUtil.getInstance().getCountryDetail(countryId.get(name));
            if (areaTree == null)
                return 0;
            num = areaTree.getTotal().getConfirm() - areaTree.getTotal().getDead() - areaTree.getTotal().getHeal();
        }
        if (num == 0)
            return 0;
        if (num > 0 && num < 10)
            return 1;
        if (num >= 10 && num < 100)
            return 2;
        if (num >= 100 && num < 1000)
            return 3;
        if (num >= 1000 && num < 10000)
            return 4;
        if (num >= 10000)
            return 5;
        return 0;
    }

    private String getName(String name){
        String[] arr = name.split("-");
        if (arr.length == 2){
            Children children = RetrofitUtil.getInstance().getProvinceDetail(provinceId.get(arr[1]));
            if (children != null)
                return children.getName();
        }
        //国家
        else {
            AreaTree areaTree = RetrofitUtil.getInstance().getCountryDetail(countryId.get(name));
            if (areaTree != null)
                return areaTree.getName();
        }
        return "";
    }
}
