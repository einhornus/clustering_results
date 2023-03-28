@Override
        public void run() {
            /* if we have 'waitOnThese' futures, we'll wait on them before proceeding */
            if (waitOnThese != null) {
                for (Future<?> f : waitOnThese) {
                    try {
                        f.get();
                    } catch (Throwable e) {
                        System.err.println("Error while waiting on future in CompletionThread");
                    }
                }
            }

            /* send the event */
            if (event == TestConcurrencyobserverEvent.onError) {
                observer.onError(new RuntimeException("mocked exception"));
            } else if (event == TestConcurrencyobserverEvent.onComplete) {
                observer.onComplete();

            } else {
                throw new IllegalArgumentException("Expecting either onError or onComplete()");
            }
        }
--------------------

@Test(timeout = 2000)
    public void testRepeatAndTake() {
        @SuppressWarnings("unchecked")
                Observer<Object> o = mock(Observer.class);
        
        Observable.just(1).repeat().take(10).subscribe(o);
        
        verify(o, times(10)).onNext(1);
        verify(o).onComplete();
        verify(o, never()).onError(any(Throwable.class));
    }
--------------------

@Test
    public void concatVeryLongObservableOfObservablesTakeHalf() {
        final int n = 10000;
        Observable<Observable<Integer>> source = Observable.create(new OnSubscribe<Observable<Integer>>() {
            @Override
            public void call(Subscriber<? super Observable<Integer>> s) {
                for (int i = 0; i < n; i++) {
                    if (s.isUnsubscribed()) {
                        return;
                    }
                    s.onNext(Observable.just(i));
                }
                s.onComplete();
            }
        });
        
        Observable<List<Integer>> result = Observable.concat(source).take(n / 2).toList();
        
        @SuppressWarnings("unchecked")
        Observer<List<Integer>> o = mock(Observer.class);
        InOrder inOrder = inOrder(o);
        
        result.subscribe(o);

        List<Integer> list = new ArrayList<Integer>(n);
        for (int i = 0; i < n / 2; i++) {
            list.add(i);
        }
        inOrder.verify(o).onNext(list);
        inOrder.verify(o).onComplete();
        verify(o, never()).onError(any(Throwable.class));
    }
--------------------

