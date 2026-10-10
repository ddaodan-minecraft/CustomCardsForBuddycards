package com.ddaodan.buddycardsexpansion.core;

public class BinderSettings {
    private int color = 0x555555; // 默认深灰
    private int pages = 1;

    public BinderSettings() {}

    public BinderSettings(int color, int pages) {
        this.color = color;
        this.pages = Math.max(1, pages);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = Math.max(1, pages);
    }
}
