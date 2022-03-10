package com.coldnight.book;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.coldnight.book.controls.FlipperLayout;
import com.coldnight.book.utils.PageUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FlipperLayout.TouchListener, PageUtils.Calculated {
    String content = "第一章 呵呵\n一望无垠的大漠，空旷而高远，壮阔而雄浑，当红日西坠，地平线尽头一片殷红，磅礴中亦有种苍凉感。\n" +
            "\n" +
            "    上古的烽烟早已在岁月中逝去，黄河古道虽然几经变迁，但依旧在。\n" +
            "\n" +
            "    楚风一个人在旅行，很疲惫，他躺在黄沙上，看着血色的夕阳，不知道还要多久才能离开这片大漠。\n" +
            "\n" +
            "    数日前他毕业了，同时也跟校园中的女神说再见，或许见不到了吧，毕竟他曾被委婉的告知，从此天各一方，该分手了。\n" +
            "\n" +
            "    离开学院后，他便出来旅行。\n" +
            "\n" +
            "    落日很红，挂在大漠的尽头，在空旷中有一种宁静的美。\n" +
            "\n" +
            "    楚风坐起来喝了一些水，感觉精力恢复了不少，他的身体属于修长强健那一类型，体质非常好，疲惫渐消退。\n" +
            "\n" +
            "    站起来眺望，他觉得快要离开大漠了，再走一段路程或许就会见到牧民的帐篷，他决定继续前行。\n" +
            "\n" +
            "    第二回 哈哈\n一路西进，他在大漠中留下一串很长、很远的脚印。\n" +
            "\n" +
            "    无声无息，竟起雾了，这在沙漠中非常罕见。\n" +
            "\n" +
            "    楚风惊讶，而这雾竟然是蓝色的，在这深秋季节给人一种凉意。\n" +
            "\n" +
            "    不知不觉间，雾霭渐重，蓝色缭绕，朦朦胧胧，笼罩了这片沙漠。\n" +
            "\n" +
            "    大漠尽头，落日都显得有些诡异了，渐渐化成一轮蓝日，有种魔性的美，而火云也被染成了蓝色。\n" +
            "\n" +
            "    楚风皱眉，虽然他知道，沙漠的天气最是多变，但眼前实在不太正常。\n" +
            "\n" +
            "    一片寂静，他停下脚步。\n" +
            "\n" +
            "    在进大漠前，他曾听当地的老牧民讲过，一个人走在沙漠中，有时会听到一些古怪的声音，会见到一些奇异的东西，要格外谨慎。\n" +
            "\n" +
            "    当时他并未在意。\n" +
            "\n" +
            "    第三节 哈哈\n依旧宁静，沙漠中除却多了一层朦胧的蓝雾，并没有其他变故发生，楚风加快脚步，他想尽快离开这里。\n" +
            "\n" +
            "    大漠的尽头，落日蓝的妖异，染蓝了西部的天空，不过它终究快要消失在地平线上了。\n" +
            "\n" +
            "    楚风的速度越来越快，开始奔跑，他不想呆在这种诡异、充满不确定性的地方。\n" +
            "\n" +
            "    在沙漠中，海市蜃楼那样的奇景多发生在烈日当空下，眼下不相符，这不像是什么蜃景。\n" +
            "\n" +
            "    突然，前面传来轻响，像是有什么东西破沙而出，而且声音很密集，此起彼伏。\n" +
            "\n";

    private List<String> curr_pages;    //当前页列表
    private TextView tv;                //当前页文本控件
    private TextView tv_right;          //下一页文本控件
    private FlipperLayout fl;           //滑动控件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
         * 1.实例化对象   succeed
         * 2.获取并设置布局     succeed
         * 3.设置小说内容,自动分章     succeed
         * 4.加载章节  判断是否大于三章，大于如何，小于如何？      大于等于则载入三章内容，小于则有多少载入多少章节
         * 5.自动分页  判断是否大于三页，大于如何，小于如何？
         * 6.共有当前章节页数+2页
         * 7.点击上一页，如果是非第一章第一页，加载上一章内容
         *             如果是其他则不执行
         * 8.点击下一页，如果是非最后一章最后一页，加载下一章内容
         *             如果是其他则不执行
         * */
        // FIXME: 2021/11/6 1.滑动过快无法滑动
//        BookPage bp = new BookPage(this);

//        setContentView(bp.getView());
//
//        bp.setTxt(content);

        init();

    }

    private void init() {
        fl = findViewById(R.id.flip);

        @SuppressLint("InflateParams") View view1 = LayoutInflater.from(this).inflate(R.layout.activity_read, null);
        @SuppressLint("InflateParams") View view2 = LayoutInflater.from(this).inflate(R.layout.activity_read, null);
        @SuppressLint("InflateParams") View view3 = LayoutInflater.from(this).inflate(R.layout.activity_read, null);

        fl.initFlipperViews(MainActivity.this, view1, view2, view3);

        tv = view2.findViewById(R.id.textView);
        tv_right = view1.findViewById(R.id.textView);

        curr_pages = new ArrayList<>();
        PageUtils pu = new PageUtils(findViewById(R.id.textView_hide));
        pu.getChapters(content);
        pu.getPages(this, content, MainActivity.this);
    }

    /**
     * 创建一个承载Text的View
     *
     * @param direction 滑动方向
     * @param index     新建页的索引
     * @return 返回新建的上（下）一页的视图
     */
    @SuppressLint("InflateParams")
    @Override
    public View createView(int direction, int index) {
        View newView;
        if (direction == FlipperLayout.TouchListener.MOVE_TO_LEFT) { //下一页
            newView = LayoutInflater.from(this).inflate(R.layout.activity_read, null);
            final TextView tv = newView.findViewById(R.id.textView);
            if (index < curr_pages.size()) {
                tv.setText(curr_pages.get(index));
            }
        } else {
            newView = LayoutInflater.from(this).inflate(R.layout.activity_read, null);
            final TextView textView = newView.findViewById(R.id.textView);
            textView.setText(curr_pages.get(index - 1));
        }
        return newView;
    }

    /***
     * 当前页是否是最后一页
     *
     * @return 返回是否为最后一页
     */
    @Override
    public boolean currentIsLastPage() {
        return curr_pages.size() == fl.getIndex();
    }

    /***
     * 当前页是否有下一页（用来判断可滑动性）
     *
     * @return 返回是否有下一页
     */
    @Override
    public boolean whetherHasNextPage(int index) {
        return curr_pages.size() > index;
    }

    @Override
    public void calculated(List<String> mList) {
        curr_pages = mList;
        tv.setText(mList.get(0));
        tv_right.setText(mList.get(1));

    }
}