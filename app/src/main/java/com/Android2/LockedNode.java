package com.Android2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cirkus on 24.07.2017.
 */

public class LockedNode extends Node implements Parcelable {
    public String qrCode;

    public LockedNode() {
    }

    protected LockedNode(Parcel in) {
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

    public static final Creator<LockedNode> CREATOR = new Creator<LockedNode>() {
        @Override
        public LockedNode createFromParcel(Parcel in) {
            return new LockedNode(in);
        }

        @Override
        public LockedNode[] newArray(int size) {
            return new LockedNode[size];
        }
    };
}
