package com.example.lehoanghan.widgets;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.HashSet;
import java.util.List;

/**
 * Created by LTr on 3/27/2017.
 */

public class DayViewDecoratorDotSpan implements DayViewDecorator {
    private final HashSet<CalendarDay> dates;
    private final int color;
    private Context context;


    public DayViewDecoratorDotSpan(int color, List<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
    }
}
