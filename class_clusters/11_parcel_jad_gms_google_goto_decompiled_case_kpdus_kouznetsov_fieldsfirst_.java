// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.internal;


// Referenced classes of package com.google.android.gms.internal:
//            fg, fy, fv

abstract class  extends com.google.android.gms.common.api.
{

    protected volatile void a(com.google.android.gms.common.api. )
    {
        a((fy));
    }

    protected abstract void a(fv fv);

    protected final void a(fy fy1)
    {
        a(fy1.dM());
    }

    public .c()
    {
        super(fg.xF);
    }
}

--------------------

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.appyet.metadata;


public class MetadataModuleFeedSql
{

    public String Guid;
    public int Id;
    public int ModuleId;
    public String Query;

    public MetadataModuleFeedSql()
    {
    }
}

--------------------

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package android.support.v4.app;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

class NoSaveStateFrameLayout extends FrameLayout
{

    public NoSaveStateFrameLayout(Context context)
    {
        super(context);
    }

    static ViewGroup wrap(View view)
    {
        NoSaveStateFrameLayout nosavestateframelayout = new NoSaveStateFrameLayout(view.getContext());
        android.view.ViewGroup.LayoutParams layoutparams = view.getLayoutParams();
        if (layoutparams != null)
        {
            nosavestateframelayout.setLayoutParams(layoutparams);
        }
        view.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
        nosavestateframelayout.addView(view);
        return nosavestateframelayout;
    }

    protected void dispatchRestoreInstanceState(SparseArray sparsearray)
    {
        dispatchThawSelfOnly(sparsearray);
    }

    protected void dispatchSaveInstanceState(SparseArray sparsearray)
    {
        dispatchFreezeSelfOnly(sparsearray);
    }
}

--------------------

