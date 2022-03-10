package com.coldnight.book.old;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.coldnight.book.beans.Chapter;
import com.coldnight.book.controls.ReadView;
import com.coldnight.book.old.BookPageAdapter;
import com.coldnight.book.old.BookViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookPage {
    private static final String TAG = "BookPage";
    private final Context mContext;
    private FrameLayout root;
    private BookPageAdapter bookPageAdapter;
    private BookViewPager bookViewPager;    //ViewPager
    private TextView textView;              //用于计算字数的文本框

    private final List<View> mItems = new ArrayList<>();                    //Viewpager视图
    private final List<Chapter> novel = new ArrayList<>();             //存放整本小说
    private final List<String> directory = new ArrayList<>();          //小说目录

    private final List<String> mTxtList = new ArrayList<>();           //存放本章小说
    private final List<String> mLastTxtList = new ArrayList<>();       //存放本章小说
    private final List<String> mNextTxtList = new ArrayList<>();       //存放本章小说

    private int chapter = 1;            //当前章节
    private int page = 1;               //当前页数
    private int item = 0;               //ViewPager位置 0 1 2

    private boolean switching_next = false;
    private boolean switching_last = false;


    //文本框样式
    private String txtColor;
    private String bgColor;

    public BookPage(Context context) {
        this.mContext = context;
    }

    public BookPage(Context context, String txt) {
        this(context);
        setTxt(txt);
    }

    public View getView() {
        if (root != null)
            return root;
        root = new FrameLayout(mContext);
        textView = new TextView(mContext);
        textView.setVisibility(View.INVISIBLE);
        textView.setTextSize(30);
        textView.setPadding(40, 40, 40, 40);
        textView.setLineSpacing(0, 2f);
        textView.setLetterSpacing(0.05f);

        bookPageAdapter = new BookPageAdapter(mContext, this, mItems);
        bookViewPager = new BookViewPager(mContext);
        bookViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        bookViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                /**/
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    item = bookViewPager.getCurrentItem();
                }
                if (state == 0) {
                    /*
                    页面滚动完成，有三种状态 当前页面未改变 上一页 下一页
                        先判断滚动完成的状态
                        当未改变时，不做动作
                        当上一页时，加载上一页
                        当下一页时，加载下一页

                     */
                    int position = bookViewPager.getCurrentItem();
                    if (item < position) {              //下一页
                        nextPage(position);
                    } else if (item > position) {      //上一页
                        lastPage(position);
                    }
                }
            }
        });

        root.addView(bookViewPager);
        root.addView(textView);
        return root;
    }

    /**
     * 滑动完成时加载上一页内容到ViewPager视图中
     *
     * @param position 当前视图位置
     */
    // FIXME: 2021/10/17 滑动过快时，加载较慢，无法继续滑动
    private void lastPage(int position) {
        if (page > 1)
            page--;

        if (switching_next)
            switching_next = false;

        if (chapter == getChapters() && page == getPages() - 1)     //倒数第二页不执行
            return;

        TextView tv0 = (TextView) mItems.get(mItems.size() - 1);
        if (page == 1 && !switching_last) {              //第一页
            if (mLastTxtList.size() == 0)               //第一章第一页，拒绝执行
                return;
            switching_last = true;                       //中间章节第一页，切换标记设置为真
            tv0.setText(mLastTxtList.get(mLastTxtList.size() - 1));
        } else if (switching_last) {                     //切换到上一章最后一页
            page = mLastTxtList.size();
            loadChapter(getChapter() - 1);
            tv0.setText(mTxtList.get(page - 2));
            switching_last = false;
        } else {                                    //中间页数
            tv0.setText(mTxtList.get(page - 2));
        }

        List<View> cache = new ArrayList<>(mItems);
        mItems.clear();
        mItems.add(tv0);
        mItems.addAll(cache);
        mItems.remove(mItems.size() - 1);
        bookPageAdapter.notifyDataSetChanged();
        bookViewPager.setCurrentItem(bookViewPager.getCurrentItem() + 1, false);
        System.out.println("上一页\t页数：" + getPage() + "/" + getPages() + "\t位置:" + (bookViewPager.getCurrentItem() + 1) + "/" + bookPageAdapter.getCount());

    }

    /**
     * 滑动完成时加载下一页内容到ViewPager视图中
     *
     * @param position 当前视图位置
     */
    private void nextPage(int position) {
        if (page < mTxtList.size())
            page++;

        if (switching_last)
            switching_last = false;

        if ((chapter == 1 && page == 2 && position == 1) || (chapter == getChapters() && page == getPages()))         //首尾页，拒绝执行
            return;

        TextView tv0 = (TextView) mItems.get(0);
        if (page == mTxtList.size() && !switching_next) {              //最后一页
            if (mNextTxtList.size() == 0)          //最后一章最后一页，拒绝执行
                return;
            switching_next = true;                       //中间章节最后一页，切换标记设置为真
            tv0.setText(mNextTxtList.get(0));
        } else if (switching_next) {                     //切换到下一章第一页
            page = 1;
            loadChapter(getChapter() + 1);
            tv0.setText(mTxtList.get(page));
            switching_next = false;
        } else {                                    //中间页数
            tv0.setText(mTxtList.get(page));
        }
        mItems.remove(0);
        mItems.add(tv0);
        bookPageAdapter.notifyDataSetChanged();
        bookViewPager.setCurrentItem(bookViewPager.getCurrentItem() - 1, false);
        System.out.println("下一页\t页数：" + getPage() + "/" + getPages() + "\t位置:" + (bookViewPager.getCurrentItem() + 1) + "/" + bookPageAdapter.getCount());
    }

    // FIXME: 2021/10/12 此为默认分章方式，后期优化
    public void setTxt(String txt) {
        setTxt(txt, "([第][\\d零一二三四五六七八九十百千万]{1,10}[章节回][ ]?[\\u4e00-\\u9fa5]{0,20}\n)");
    }

    public void setTxt(String txt, String regex) {
        setTxt(txt, regex, chapter);
    }

    public void setTxt(String txt, String regex, int chapter) {
        if ("".equals(txt.trim()))
            throw new RuntimeException("The text content must not be empty");
        List<Integer> positions = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(txt);
        while (m.find()) {
            directory.add(m.group().substring(0, (m.group().length() - 1)));
            positions.add(m.start());
            positions.add(m.end());
        }
        positions.add(txt.length());
        for (int i = 0; i < directory.size(); i++) {
            String endStr = txt.substring(positions.get(i * 2 + 1), positions.get(i * 2 + 2));
            novel.add(new Chapter(i + 1, directory.get(i), endStr));
        }

        //找不到标题分章，采用字数分章
        if (directory.size() == 0 || !txt.startsWith(directory.get(0))) {
            novel.clear();
            directory.clear();
            if (txt.length() < 5000) {
                directory.add("第一部分");
                novel.add(new Chapter(1, "第一部分", txt));
            } else {
                int num = (int) Math.ceil(txt.length() / 5000f);
                for (int i = 0; i <= num; i++) {
                    String title = "第" + (i + 1) + "部分";
                    directory.add(title);
                    if ((i + 1) * 5000 <= txt.length()) {
                        novel.add(new Chapter(i + 1, title, txt.substring(i * 5000, (i + 1) * 5000)));
                    } else {
                        novel.add(new Chapter(i + 1, title, txt.substring(i * 5000)));
                    }
                }
            }
        }
    }


    // FIXME: 2021/11/6 优化加载章节模式
    private void loadChapter(int num) {
        loadChapter(num, null);
    }

    /**
     * @param num 需要加载的章节数
     */
    private void loadChapter(int num, Calculated calculated) {
        if (num <= 0 || num > getChapters())
            throw new RuntimeException("章节号错误");
        num = num - 1;

        //计算章节内容
        if (mTxtList.size() == 0) {     //本章无内容，直接计算
            mLastTxtList.clear();
            mNextTxtList.clear();
            Chapter c_this = novel.get(num);
            calc(c_this.getContent(), mTxtList, calculated);        //本章无内容，直接计算出本章的内容
            if (getChapters() == 1) {                   //只有一章内容，已载入，拒绝执行
                chapter = 1;
                return;
            }
            if (num > 0 && chapter < getChapters() - 1) {   //中间章节，加载前后章节
                Chapter c_last = novel.get(num - 1);
                calc(c_last.getContent(), mLastTxtList);
                Chapter c_next = novel.get(num + 1);
                calc(c_next.getContent(), mNextTxtList);
                chapter = num + 1;
            } else if (num == 0) {                          //第一章，加载下一章
                chapter = 1;
                Chapter c_next = novel.get(num + 1);
                calc(c_next.getContent(), mNextTxtList);
            } else if (num == getChapters() - 1) {          //最后一章，加载上一章
                chapter = getChapters();
                Chapter c_last = novel.get(num - 1);
                calc(c_last.getContent(), mLastTxtList);
                mNextTxtList.clear();
            }
        } else {        //本章有内容   是否为相邻章节，是则赋值销毁，否则全部重新计算       最后一章，拒绝执行
            if ((num + 1) == chapter) {     //本章内容，不执行
                return;
            } else if (chapter == 1 && num == 0) {      //只有一章，不执行
                return;
            }
            if (num + 2 == chapter) {        //上一章
                chapter = num + 1;
                mNextTxtList.clear();
                mNextTxtList.addAll(mTxtList);

                mTxtList.clear();
                mTxtList.addAll(mLastTxtList);

                mLastTxtList.clear();
                if (num - 1 >= 0) {
                    Chapter c_last = novel.get(num - 1);
                    calc(c_last.getContent(), mLastTxtList);
                }
            } else if (num == chapter) {        //下一章
                chapter = num + 1;
                mLastTxtList.clear();
                mLastTxtList.addAll(mTxtList);

                mTxtList.clear();
                mTxtList.addAll(mNextTxtList);

                mNextTxtList.clear();
                if (novel.size() > num + 1) {
                    Chapter c_next = novel.get(num + 1);
                    calc(c_next.getContent(), mNextTxtList);
                }
            } else {
                chapter = num;
                mLastTxtList.clear();
                mTxtList.clear();
                mNextTxtList.clear();
                Chapter c_last = novel.get(num - 1);
                calc(c_last.getContent(), mLastTxtList);
                Chapter c_this = novel.get(num);
                calc(c_this.getContent(), mTxtList, calculated);
                Chapter c_next = novel.get(num + 1);
                calc(c_next.getContent(), mNextTxtList);
            }
        }

    }

    public void addChapter(String title, String content) {
        novel.add(new Chapter(novel.size(), title, content));
    }

    public int getChapters() {
        return novel.size();
    }

    public int getChapter() {
        return chapter;
    }

    public int getPages() {
        return mTxtList.size();
    }

    public int getPage() {
        return page;
    }


    public List<View> getmItems() {
        return mItems;
    }

    /**
     * 计算字数
     *
     * @param content 章节内容，用于计算字数的内容
     * @return mList中存放每页的内容
     */
    private void calc(String content, List<String> mlist) {
         calc(content, mlist,  null);
    }

    // FIXME: 2021/10/12 计算每页内容，后期优化代码
    private void calc(String content, List<String > mlst, Calculated callBackCalculate) {
        List<String> mList = new ArrayList<>();
        textView.setText(content);
        final boolean[] flag = {false};              //标识onGlobalLayout执行次数
        ViewTreeObserver observer = textView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            if (!flag[0]) {
                mList.clear();
                int cursor = 0;
                int length = content.length();
                while (cursor < length) {
                    Layout layout = textView.getLayout();
                    int topOfLastLine = textView.getHeight() - textView.getPaddingTop() - textView.getPaddingBottom() - textView.getLineHeight();
                    int line = layout.getLineForVertical(topOfLastLine);       //行号
                    int num = textView.getLayout().getLineEnd(line);                 //字数
                    CharSequence newContent;
                    int cursor_cache = cursor + num;
                    if (cursor_cache <= length) {
                        newContent = content.subSequence(cursor, cursor_cache);
                        cursor = cursor_cache;
                    } else {
                        newContent = content.subSequence(cursor, length);
                        cursor += cursor;
                    }
                    mList.add(newContent.toString().trim());
                    if (cursor < length) {

                    }
                }
                //计算完成 设置Items的文本内容
                if (callBackCalculate != null)
                    callBackCalculate.calculated();
                flag[0] = true;
            }
        });
    }

    /**
     * 如果mTxtList列表重新计算了文本，则重新需设置其内容
     *
     * @param mList 传入list列表，如果为mTxtList对象，则重新计算
     * @param page  传入page页，重新计算前的页数，重新计算和设置
     */
    private void setItemText(List<String> mList, int page) {

    }


    interface Calculated {
        void calculated();
    }
}
