@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_banner, null);
			}
			ImageView item_banner_imageview = (ImageView) convertView
					.findViewById(R.id.item_banner_imageview);
			String imageUrl = getItem(position);
			imageUrl = TextUtils.isEmpty(imageUrl) ? "" : imageUrl;
			ImageLoad_Uils.setImage(item_banner_imageview, imageUrl, true,
					ScaleType.CENTER_CROP, R.drawable.ic_launcher,
					getActivity());
			return convertView;
		}
--------------------

@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
	}
--------------------

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_maps);

        // setTheme(R.style.Theme_Holo_Light);
        setContentView(R.layout.fragment_blank);
        setUpMapIfNeeded();
        txtCTime=(TextView)findViewById(R.id.textLatLong);

        addLocationListener();
    }
--------------------

