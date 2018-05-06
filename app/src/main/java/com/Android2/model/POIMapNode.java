package com.Android2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cirkus on 24.07.2017.
 *
 * General point-of-interest. Can have visibility criteria, action unlock criteria,
 * unrestricted and restricted actions etc.
 */

public class POIMapNode extends MapNode implements Parcelable {
    public String qrCode; //< TODO: Migrate into QRCodeLockRule.

    int id;
    String name; //< Optional. TODO: Make localizable.

    public POIMapNode() {
    }

    protected POIMapNode(Parcel in) {
        super(in);
        qrCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(qrCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMapNode> CREATOR = new Creator<POIMapNode>() {
        @Override
        public POIMapNode createFromParcel(Parcel in) {
            return new POIMapNode(in);
        }

        @Override
        public POIMapNode[] newArray(int size) {
            return new POIMapNode[size];
        }
    };
}
