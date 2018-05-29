package kr.co.sketchlab.gtcall.shlib.ui.comp;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.shlib.ui.ViewUtil;


/**
 * Created by shp on 2018. 3. 21..
 */

public class PagerDotView {
    Activity activity;
    FrameLayout holder;
    int sizeInPx = 4;
    int marginInPx = 8;

    float curPage = 0;
    int pageCnt = 0;

    View dotSel;

    public PagerDotView(Activity activity, FrameLayout holder, int sizeInDp, int marginInDp) {
        this.activity = activity;
        this.holder = holder;
        this.sizeInPx = ViewUtil.dp2px(activity, sizeInDp);
        this.marginInPx = ViewUtil.dp2px(activity, marginInDp);
    }

    public void setPageCnt(int pageCnt) {
        this.pageCnt = pageCnt;

        holder.removeAllViews();
        if(pageCnt > 0) {
            // 페이지 수만큼 선택안된 dot 추가
            for(int i = 0; i < pageCnt; i++) {
                ImageView dot = new ImageView(activity);
                dot.setImageResource(R.drawable.ic_pager_dot_nor);
                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(sizeInPx, sizeInPx);

                dot.setLayoutParams(layoutParams);

                holder.addView(dot);

                int leftMargin = (marginInPx+sizeInPx) * i;
                ViewUtil.setViewMarginLeft(dot, leftMargin, false);
            }

            // 선택 아이콘 더하기
            ImageView dot = new ImageView(activity);
            dot.setImageResource(R.drawable.ic_pager_dot_sel);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(sizeInPx, sizeInPx);
            dot.setLayoutParams(layoutParams);

            holder.addView(dot);

            // 선택된 dot
            dotSel = dot;
        }
    }

    public void setCurPage(float curPage) {
        this.curPage = curPage;

        int selDotMarginLeft = (int)(curPage * (marginInPx+sizeInPx));
        ViewUtil.setViewMarginLeft(dotSel, selDotMarginLeft, true);
    }
}
