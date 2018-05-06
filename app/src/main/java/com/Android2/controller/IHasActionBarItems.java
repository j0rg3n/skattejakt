package com.Android2.controller;

/**
 * Created by cirkus on 03.08.2017.
 */

public interface IHasActionBarItems {
    void setOnActionBarItemEnabledEnabledChanged(OnActionBarItemEnabledChangedListener listener);
    boolean isActionBarItemEnabled(int itemId);
    int[] getOwnedActionBarItemIds();
    boolean onActionBarItemSelected(int itemId);

    interface OnActionBarItemEnabledChangedListener {
        void onActionBarItemEnabledChanged();
        void onHasActionBarItemsDetach();
    }
}
