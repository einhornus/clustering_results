@Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(mUri, i);
            parcel.writeParcelable(mContentValues, i);
        }
--------------------

@Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(app, 0);
        dest.writeInt(newBuild);
        dest.writeString(newBuildUrl);
    }
--------------------

@Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMessageClass);
        dest.writeInt(mCategory);
        dest.writeInt(mResponseType);
        dest.writeInt(mSeverity);
        dest.writeInt(mUrgency);
        dest.writeInt(mCertainty);
    }
--------------------

