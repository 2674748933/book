package com.coldnight.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
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
            "\n" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*
        * 1.实例化对象   succeed
        * 2.获取并设置布局     succeed
        * 3.设置小说内容,自动分章     succeed
        * 4.加载章节 判断章节是否大于三章，大于执行，小于如何？ 小于是根据位置设置清空其他章节缓存
        * 5.自动分页  计算完每页内容后自动视图内容 分页时判断是否大于三页，大于如何，小于如何？
        * 6.固定三页视图，默认第一页，永远第一页
        * 7.点击下一页 如果不是最后一页，执行，如果是最后一页
        * */
        // FIXME: 2021/11/6 1.滑动过快无法滑动
        BookPage bp = new BookPage(this);

        setContentView(bp.getView());

        bp.setTxt(content);


    }
}