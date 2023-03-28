public void addI64(long v) {
        align(8);
        mData[mPos++] = (byte)(v & 0xff);
        mData[mPos++] = (byte)((v >> 8) & 0xff);
        mData[mPos++] = (byte)((v >> 16) & 0xff);
        mData[mPos++] = (byte)((v >> 24) & 0xff);
        mData[mPos++] = (byte)((v >> 32) & 0xff);
        mData[mPos++] = (byte)((v >> 40) & 0xff);
        mData[mPos++] = (byte)((v >> 48) & 0xff);
        mData[mPos++] = (byte)((v >> 56) & 0xff);
    }
--------------------

String getMatchDescription() {
            if (targets != null) {
                return "leading: " + targets.toString();
            }
            if (sources != null) {
                return "trailing: " + sources.toString();
            }
            return "--";
        }
--------------------

protected String convertCell(String s)
	{
		SB sb = new SB(s.length() + 32);
		for(int i=0; i<s.length(); i++)
		{
			char c = s.charAt(i);
			if((quoteChar != null) && (c == quoteChar))
			{
				sb.append(quoteChar).append(quoteChar);
			}
			else if((escapeChar != null) && (c == escapeChar))
			{
				sb.append(escapeChar).append(c);
			}
			else
			{
				sb.append(c);
			}
		}

		return sb.toString();
	}
--------------------

