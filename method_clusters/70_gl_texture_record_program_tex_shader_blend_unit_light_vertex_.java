private static int getGLType(Type type) {
        switch (type) {
            case TwoDimensional:
                return GL11.GL_TEXTURE_2D;
            case OneDimensional:
                return GL11.GL_TEXTURE_1D;
            case ThreeDimensional:
                return GL12.GL_TEXTURE_3D;
            case CubeMap:
                return ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_ARB;
        }
        throw new IllegalArgumentException("invalid texture type: " + type);
    }
--------------------

public void initRS() {
        SceneManager sceneManager = SceneManager.getInstance();
        mRS = SceneManager.getRS();
        mRes = SceneManager.getRes();
        long start = System.currentTimeMillis();
        mTransformRSData = mRootTransforms.getRSData();
        long end = System.currentTimeMillis();
        Log.v(TIMER_TAG, "Transform init time: " + (end - start));

        start = System.currentTimeMillis();

        sceneManager.mRenderLoop.bind_gRootNode(mTransformRSData);
        end = System.currentTimeMillis();
        Log.v(TIMER_TAG, "Script init time: " + (end - start));

        start = System.currentTimeMillis();
        addDrawables(mRS, mRes, sceneManager);
        end = System.currentTimeMillis();
        Log.v(TIMER_TAG, "Renderable init time: " + (end - start));

        addShaders(mRS, mRes, sceneManager);

        Allocation opaqueBuffer = null;
        if (mRenderables.size() > 0) {
            opaqueBuffer = Allocation.createSized(mRS, Element.U32(mRS), mRenderables.size());
        }
        Allocation transparentBuffer = null;
        if (mRenderables.size() > 0) {
            transparentBuffer = Allocation.createSized(mRS, Element.U32(mRS), mRenderables.size());
        }

        sceneManager.mRenderLoop.bind_gFrontToBack(opaqueBuffer);
        sceneManager.mRenderLoop.bind_gBackToFront(transparentBuffer);

        if (mCameras.size() > 0) {
            Allocation cameraData;
            cameraData = Allocation.createSized(mRS, Element.ALLOCATION(mRS), mCameras.size());
            Allocation[] cameraAllocs = new Allocation[mCameras.size()];
            for (int i = 0; i < mCameras.size(); i ++) {
                cameraAllocs[i] = mCameras.get(i).getRSData().getAllocation();
            }
            cameraData.copyFrom(cameraAllocs);
            sceneManager.mRenderLoop.set_gCameras(cameraData);
        }

        if (mLights.size() > 0) {
            Allocation lightData = Allocation.createSized(mRS,
                                                          Element.ALLOCATION(mRS),
                                                          mLights.size());
            Allocation[] lightAllocs = new Allocation[mLights.size()];
            for (int i = 0; i < mLights.size(); i ++) {
                lightAllocs[i] = mLights.get(i).getRSData().getAllocation();
            }
            lightData.copyFrom(lightAllocs);
            sceneManager.mRenderLoop.set_gLights(lightData);
        }
    }
--------------------

private void setDiffuse(int index, LightStateRecord record, float red,
            float green, float blue, float alpha, LightRecord lr) {
        if (!record.isValid() || lr.diffuse.r != red || lr.diffuse.g != green
                || lr.diffuse.b != blue || lr.diffuse.a != alpha) {
            record.lightBuffer.clear();
            record.lightBuffer.put(red);
            record.lightBuffer.put(green);
            record.lightBuffer.put(blue);
            record.lightBuffer.put(alpha);
            record.lightBuffer.flip();
            GL11.glLight(GL11.GL_LIGHT0 + index, GL11.GL_DIFFUSE,
                    record.lightBuffer);
            lr.diffuse.set(red, green, blue, alpha);
        }
    }
--------------------

