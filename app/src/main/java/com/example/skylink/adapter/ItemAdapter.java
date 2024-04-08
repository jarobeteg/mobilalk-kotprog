package com.example.skylink.adapter;

import com.example.skylink.ui.activity.SettingsActivity;

public class ItemAdapter {
    private String name;
    private Runnable action;

    public ItemAdapter(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return this.name;
    }

    public Runnable getAction() {
        return this.action;
    }
}
