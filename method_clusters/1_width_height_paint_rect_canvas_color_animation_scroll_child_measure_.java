public boolean mouseExit(Event e, int x, int y) {

        if (bDrag) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            bDrag = false;
        }

        return true;
    }
--------------------

private void put(double height, double rORh, double gORs, double bORv) {
		if (tHeights==null)
			tHeights = new double[cArrayInitSize];
		if (tNumHeights >= tHeights.length-1) {
			double[] temp = new double[tHeights.length*2];
			System.arraycopy(tHeights, 0, temp, 0, tHeights.length);
			tHeights = temp;
		}
		if (mMinElevation>height) mMinElevation = height;
		if (mMaxElevation<height) mMaxElevation = height;
		tHeights[tNumHeights] = height;
		if (mColorSpace.equalsIgnoreCase("RGB")) {
			mColors.add(	new T3dColor("RGB", 
							(float)rORh/255, 
							(float)gORs/255, 
							(float)bORv/255));
		}
		else {
			mColors.add(	new T3dColor("HSV", 
							(float)rORh, 
							(float)gORs, 
							(float)bORv));
		}
		
		tNumHeights++;
	}
--------------------

private long calculateStatusBarTransitionStartTime(Animation openAnimation,
            Animation closeAnimation) {
        if (openAnimation != null && closeAnimation != null) {
            TranslateAnimation openTranslateAnimation = findTranslateAnimation(openAnimation);
            TranslateAnimation closeTranslateAnimation = findTranslateAnimation(closeAnimation);
            if (openTranslateAnimation != null) {

                // Some interpolators are extremely quickly mostly finished, but not completely. For
                // our purposes, we need to find the fraction for which ther interpolator is mostly
                // there, and use that value for the calculation.
                float t = findAlmostThereFraction(openTranslateAnimation.getInterpolator());
                return SystemClock.uptimeMillis()
                        + openTranslateAnimation.getStartOffset()
                        + (long)(openTranslateAnimation.getDuration()*t) - TRANSITION_DURATION;
            } else if (closeTranslateAnimation != null) {
                return SystemClock.uptimeMillis();
            } else {
                return SystemClock.uptimeMillis();
            }
        } else {
            return SystemClock.uptimeMillis();
        }
    }
--------------------

