@Override
    public <U, R> SeqT<W,R> zipWithStream(final Stream<? extends U> other, final BiFunction<? super T, ? super U, ? extends R> zipper) {

        return (SeqT<W,R>) FoldableTransformerSeq.super.zipWithStream(other, zipper);
    }
--------------------

@Override
    public <U> ReactiveSeq<Tuple2<T, U>> zipWithStream(Stream<? extends U> other) {

        return zipWithStream(other,Tuple::tuple);
    }
--------------------

@Override
    default FutureStream<U> appendAll(final U... values) {
        return fromStream(ReactiveSeq.oneShotStream(stream())
                                     .appendAll(values));
    }
--------------------

