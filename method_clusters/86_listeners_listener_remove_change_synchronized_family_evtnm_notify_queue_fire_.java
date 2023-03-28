public boolean addEventListener(int priority, String evtnm, EventListener listener)
	{
		boolean b = super.addEventListener(evtnm, listener);
		if (b)
		{
			final EventListenerInfo listenerInfo = new EventListenerInfo(priority, listener);
			List<EventListenerInfo> list = listeners.get(evtnm);
			if (list != null)
			{
				for (Iterator<EventListenerInfo> it = list.iterator(); it.hasNext();)
				{
					final EventListenerInfo li = it.next();
					if (li.listener.equals(listener))
					{
						if (li.priority == priority)
							return false; // nothing to do
						it.remove(); // re-added later
						break;
					}
				}

				list.add(listenerInfo);
			}
			else
			{
				listeners.put(evtnm, list = new LinkedList<EventListenerInfo>());
				list.add(listenerInfo);
			}
		}
		return b;
	}
--------------------

public void addHostsListener(IHostsListener listener) {
		synchronized(_hostsListeners){
			if (!_hostsListeners.contains(listener))
				_hostsListeners.add(listener);
		}
	}
--------------------

public void removeQueueUsersListener(QueueUsersListener listener) {
        synchronized (queueUsersListeners) {
            queueUsersListeners.remove(listener);
        }
    }
--------------------

