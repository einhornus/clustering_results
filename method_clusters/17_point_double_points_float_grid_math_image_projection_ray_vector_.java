boolean withinBounds(GLBaseShape shape, GLBaseShape center_shape, float radius){
		boolean visible = true;
		
		
		float[] shapecoords = shape.positionData;
		float[] centercoords = center_shape.positionData;
		
		for(int i =0; i < 3; i++){
			
			float component_distance = shapecoords[i] - centercoords[i];
			if(component_distance < 0) component_distance *= -1;
			
			if(component_distance > radius) {
				visible = false;
				break;
			}
			
			
			
		}
		
		return visible;
	}
--------------------

public double fittingFunction(double x, double[] a, double[] dyda, int ma) {
		// handles fitting of a straight line
		// for a more useful equation overwrite this function
		dyda[0] = x;
		dyda[1] = 1;
		return a[0]*x+a[1];
		}
--------------------

private void calculateNormTransformation()
	{
        // todo: Method is rather identical to VsSimpleScene#calculateNormTransformation 
		// - maybe this should be refactored in the future...

        VgEnvelope envXY = this.envelope();
		double xMinGeo = envXY.getXMin();
		double xMaxGeo = envXY.getXMax();
		double yMinGeo = envXY.getYMin();
		double yMaxGeo = envXY.getYMax();

        double dx = xMaxGeo - xMinGeo;
        double dy = yMaxGeo - yMinGeo;
        mAspect = dy/dx;

        if (Math.abs(dx) > Math.abs(dy)) {
        	mScale = 2./dx;
        	mOffset.setX(-(xMinGeo + xMaxGeo)/ dx);
        	mOffset.setY(-(yMinGeo + yMaxGeo)/ dx);
        }
        else {
        	mScale = 2./dy;
        	mOffset.setX(-(xMinGeo + xMaxGeo)/ dy);
        	mOffset.setY(-(yMinGeo + yMaxGeo)/ dy);
        }
	}
--------------------

