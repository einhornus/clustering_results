@Override
	public void afterTextChanged(Editable editable)
	{
		if (mConversation == null)
		{
			return;
		}

		/* only update the chat metadata if this is an SMS chat */
		if (!mConversation.isOnhike())
		{
			updateChatMetadata();
		}
		if (hashWatcher != null)
		{
			hashWatcher.afterTextChanged(editable);
		}
	}
--------------------

@Override
	protected Conversation[] doInBackground(Conversation... convs)
	{
		ArrayList<Uri> uris = new ArrayList<Uri>();
		String chatLabel = "";
		for (int k = 0; k < convs.length; k++)
		{
			HikeConversationsDatabase db = null;
			String msisdn = convs[k].getMsisdn();
			StringBuilder sBuilder = new StringBuilder();
			Map<String, PairModified<GroupParticipant, String>> participantMap = null;

			db = HikeConversationsDatabase.getInstance();
			Conversation conv = db.getConversation(msisdn, -1);
			boolean isGroup = Utils.isGroupConversation(msisdn);
			chatLabel = conv.getLabel();

			if (isGroup)
			{
				sBuilder.append(R.string.group_name_email);
				GroupConversation gConv = ((GroupConversation) convs[k]);
				if (null == gConv.getGroupParticipantList())
				{
					gConv.setGroupParticipantList(ContactManager.getInstance().getGroupParticipants(gConv.getMsisdn(), false, false));
				}
				participantMap = gConv.getGroupParticipantList();
			}
			// initialize with a label
			sBuilder.append(activity.getResources().getString(R.string.chat_with_prefix) + chatLabel + "\n");

			String chatFileName = activity.getResources().getString(R.string.chat_backup_) + System.currentTimeMillis() + ".txt";
			File chatFile = getChatFile(chatFileName);
			if(chatFile == null)
			{
				return null;
			}
			// iterate through the messages and construct a meaningful
			// payload
			List<ConvMessage> cList = conv.getMessages();
			for (int i = 0; i < cList.size(); i++)
			{
				ConvMessage cMessage = cList.get(i);
				String messageMask = cMessage.getMessage().toString();
				String fromString = null;
				// find if this message was sent or received
				// also find out the sender number, this is needed for the
				// chat
				// file backup
				MessageMetadata cMetadata = cMessage.getMetadata();
				boolean isSent = cMessage.isSent();
				if (cMessage.isGroupChat()) // gc naming logic
				{
					GroupParticipant gPart = null;
					PairModified<GroupParticipant, String> groupParticipantPair = participantMap.get(cMessage.getGroupParticipantMsisdn());
					if(null != groupParticipantPair)
						gPart = groupParticipantPair.getFirst();

					if (gPart != null)
					{
						fromString = (isSent == true) ? activity.getResources().getString(R.string.me_key) : groupParticipantPair.getSecond();
					}
					else
					{
						fromString = (isSent == true) ? activity.getResources().getString(R.string.me_key) : "";
					}
				}
				else
					fromString = (isSent == true) ? activity.getResources().getString(R.string.me_key) : chatLabel; // 1:1
																													// message
																													// logic

				if (cMessage.isFileTransferMessage())
				{
					// TODO: can make this generic and add support for
					// multiple
					// files.
					HikeFile hikeFile = cMetadata.getHikeFiles().get(0);
					HikeFileType fileType = hikeFile.getHikeFileType();
					if ((fileType == (HikeFileType.IMAGE) || fileType == (HikeFileType.AUDIO) || fileType == (HikeFileType.AUDIO_RECORDING) || fileType == (HikeFileType.VIDEO))
							&& !TextUtils.isEmpty(hikeFile.getFilePath()))
					{
						listValues.add(hikeFile.getFilePath());
					}
					// tweak the message here based on the file
					messageMask = activity.getResources().getString(R.string.file_transfer_of_type) + " " + fileType;

				}

				// finally construct the backup string here
				sBuilder.append(Utils.getFormattedDateTimeFromTimestamp(cMessage.getTimestamp(), activity.getResources().getConfiguration().locale) + ":" + fromString + "- "
						+ messageMask + "\n");

				if(sBuilder.length() > 10000)
				{
					writeToChatTextFile(sBuilder.toString(), chatFile);
					sBuilder.delete(0, sBuilder.length());
				}
				// TODO: add location and contact handling here.
			}
			chatLabel = (Utils.isFilenameValid(chatLabel)) ? chatLabel : "_";
			writeToChatTextFile(sBuilder.toString(), chatFile);
			uris.add(Uri.fromFile(chatFile));
		}
		// append the attachments in hike messages in form of URI's. Dodo
		// android needs uris duh!
		for (String file : listValues)
		{
			File tFile = new File(file);
			Uri u = Uri.fromFile(tFile);
			uris.add(u);
		}

		// TODO: change chatlabel if more than one chats

		// create an email intent to attach the text file and other chat
		// attachments
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL, "");
		intent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.backup_of_conversation_with_prefix) + chatLabel);
		intent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.attached_is_the_conversation_backup_string));
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

		// give the hike user a choice of intents
		activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R.string.email_your_conversation)));

		// TODO: Delete this temp file, although it might be useful for the
		// user to have local chat backups ? Also we need to see

		return null;
	}
--------------------

private void handleIncomingPin(final ConvMessage convMsg)
	{
		if(convMsg.getMessageType() == HikeConstants.MESSAGE_TYPE.TEXT_PIN)
		{
			String msisdn = convMsg.getMsisdn();
			
			if(msisdn != null && msisdn.equals(this.msisdn))
			{
				
				if(pinAdapter != null)
				{
					runOnUiThread(new Runnable() 
					{						
						@Override
						public void run() 
						{
							pinAdapter.addPinMessage(convMsg);
							pinAdapter.notifyDataSetChanged();							
						}
					});
				}
			}
			Utils.resetPinUnreadCount(mConversation);
		}
	}
--------------------

