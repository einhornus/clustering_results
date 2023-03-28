public static String randomStringOfNumbers(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        final char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = sNumbers[sRandGen.nextInt(sNumbers.length - 1)];
        }
        return new String(randBuffer);
    }
--------------------

private void generateOrderArray(int totalCount) {
            if (mOrder.length != totalCount) {
                mOrder = new int[totalCount];
                for (int i = 0; i < totalCount; ++i) {
                    mOrder[i] = i;
                }
            }
            for (int i = totalCount - 1; i > 0; --i) {
                Utils.swap(mOrder, i, mRandom.nextInt(i + 1));
            }
            if (mOrder[0] == mLastIndex && totalCount > 1) {
                Utils.swap(mOrder, 0, mRandom.nextInt(totalCount - 1) + 1);
            }
        }
--------------------

public static float nextFloat(Random random, float minimum, float lessThan) {
		if(lessThan <= minimum) throw new IllegalArgumentException("lessThan must greater than minimum");
		float result = (random.nextFloat() * (lessThan - minimum)) + minimum;
		if(result >= lessThan) result = Math.nextDown(lessThan);
		return result;
//		return (random.nextFloat() * lessThan) + minimum;
	}
--------------------

