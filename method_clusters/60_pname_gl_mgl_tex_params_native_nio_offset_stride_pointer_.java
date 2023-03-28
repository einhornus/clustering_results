public static native void glGetActiveUniformBlockiv(
        int program,
        int uniformBlockIndex,
        int pname,
        int[] params,
        int offset
    );
--------------------

public void glTexCoordPointer(int size, int type, int stride, int offset) {
        begin("glTexCoordPointer");
        arg("size", size);
        arg("type", type);
        arg("stride", stride);
        arg("offset", offset);
        end();
        mgl11.glTexCoordPointer(size, type, stride, offset);
    }
--------------------

public void glPixelStorei(int pname, int param) {
        begin("glPixelStorei");
        arg("pname", pname);
        arg("param", param);
        end();

        mgl.glPixelStorei(pname, param);
        checkError();
    }
--------------------

