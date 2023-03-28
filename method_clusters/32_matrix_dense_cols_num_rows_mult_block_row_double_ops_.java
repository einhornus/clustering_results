@Test
    public void testIdentity() {
        SimpleMatrix A = SimpleMatrix.identity(5);

        BidiagonalDecomposition<DenseMatrix64F> decomp = createQRDecomposition();

        assertTrue(decomp.decompose(A.getMatrix().copy()));

        checkGeneric(A.getMatrix(), decomp);
    }
--------------------

@Test
    public void testDecomposeR() {
        if( !canR ) return;

        DenseMatrix64F A = new DenseMatrix64F(3,3, true, 1, 2, 4, 2, 13, 23, 4, 23, 90);

        DenseMatrix64F R = new DenseMatrix64F(3,3, true, 1, 2, 4, 0, 3, 5, 0, 0, 7);

        CholeskyDecomposition<DenseMatrix64F> cholesky = create(false);
        assertTrue(cholesky.decompose(A));

        DenseMatrix64F foundR = cholesky.getT(null);

        EjmlUnitTests.assertEquals(R,foundR,1e-8);
    }
--------------------

@Test
    public void upper() {
        checkDecompose(5, false);
        checkNotPositiveDefinite(5,true);
    }
--------------------

