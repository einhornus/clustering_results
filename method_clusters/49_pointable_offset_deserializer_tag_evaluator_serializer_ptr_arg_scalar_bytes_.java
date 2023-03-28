@Override
    public IScalarEvaluatorFactory createEvaluatorFactory(final IScalarEvaluatorFactory[] args) {
        return new IScalarEvaluatorFactory() {
            private static final long serialVersionUID = 1L;

            @Override
            public IScalarEvaluator createScalarEvaluator(IEvaluatorContext ctx) throws HyracksDataException {
                return new IScalarEvaluator() {

                    private ArrayBackedValueStorage resultStorage = new ArrayBackedValueStorage();
                    private DataOutput out = resultStorage.getDataOutput();
                    private IPointable inputArg0 = new VoidPointable();
                    private IPointable inputArg1 = new VoidPointable();
                    private IScalarEvaluator eval0 = args[0].createScalarEvaluator(ctx);
                    private IScalarEvaluator eval1 = args[1].createScalarEvaluator(ctx);
                    private AMutablePoint aPoint = new AMutablePoint(0, 0);
                    private AMutableCircle aCircle = new AMutableCircle(null, 0);
                    @SuppressWarnings("unchecked")
                    private ISerializerDeserializer<ACircle> circleSerde =
                            SerializerDeserializerProvider.INSTANCE.getSerializerDeserializer(BuiltinType.ACIRCLE);

                    @Override
                    public void evaluate(IFrameTupleReference tuple, IPointable result) throws HyracksDataException {
                        resultStorage.reset();
                        eval0.evaluate(tuple, inputArg0);
                        eval1.evaluate(tuple, inputArg1);

                        if (PointableHelper.checkAndSetMissingOrNull(result, inputArg0, inputArg1)) {
                            return;
                        }

                        byte[] bytes0 = inputArg0.getByteArray();
                        int offset0 = inputArg0.getStartOffset();
                        byte[] bytes1 = inputArg1.getByteArray();
                        int offset1 = inputArg1.getStartOffset();

                        // Type check: (point, double)
                        if (bytes0[offset0] != ATypeTag.SERIALIZED_POINT_TYPE_TAG) {
                            throw new TypeMismatchException(sourceLoc, getIdentifier(), 0, bytes0[offset0],
                                    ATypeTag.SERIALIZED_POINT_TYPE_TAG);
                        }
                        if (bytes1[offset1] != ATypeTag.SERIALIZED_DOUBLE_TYPE_TAG) {
                            throw new TypeMismatchException(sourceLoc, getIdentifier(), 1, bytes1[offset1],
                                    ATypeTag.SERIALIZED_DOUBLE_TYPE_TAG);
                        }

                        try {
                            aPoint.setValue(
                                    ADoubleSerializerDeserializer.getDouble(bytes0,
                                            offset0 + 1
                                                    + APointSerializerDeserializer.getCoordinateOffset(Coordinate.X)),
                                    ADoubleSerializerDeserializer.getDouble(bytes0, offset0 + 1
                                            + APointSerializerDeserializer.getCoordinateOffset(Coordinate.Y)));
                            aCircle.setValue(aPoint, ADoubleSerializerDeserializer.getDouble(bytes1, offset1 + 1));
                            circleSerde.serialize(aCircle, out);
                        } catch (IOException e1) {
                            throw HyracksDataException.create(e1);
                        }
                        result.set(resultStorage);
                    }
                };
            }
        };
    }
--------------------

@Override
                    public void evaluate(IFrameTupleReference tuple, IPointable result) throws HyracksDataException {
                        resultStorage.reset();
                        eval.evaluate(tuple, argPtr);

                        if (PointableHelper.checkAndSetMissingOrNull(result, argPtr)) {
                            return;
                        }

                        byte[] bytes = argPtr.getByteArray();
                        int offset = argPtr.getStartOffset();
                        if (bytes[offset] == ATypeTag.SERIALIZED_BOOLEAN_TYPE_TAG) {
                            boolean argRes = ABooleanSerializerDeserializer.getBoolean(bytes, offset + 1);
                            ABoolean aResult = argRes ? ABoolean.FALSE : ABoolean.TRUE;
                            booleanSerde.serialize(aResult, out);
                        } else {
                            throw new TypeMismatchException(sourceLoc, getIdentifier(), 0, bytes[offset],
                                    ATypeTag.SERIALIZED_BOOLEAN_TYPE_TAG);
                        }
                        result.set(resultStorage);
                    }
--------------------

@Override
            public IScalarEvaluator createScalarEvaluator(final IEvaluatorContext ctx) throws HyracksDataException {

                return new IScalarEvaluator() {

                    private final ArrayBackedValueStorage resultStorage = new ArrayBackedValueStorage();
                    private final DataOutput out = resultStorage.getDataOutput();
                    private final IPointable inputVal = new VoidPointable();
                    private final IScalarEvaluator evalLen = args[0].createScalarEvaluator(ctx);
                    private final IScalarEvaluator evalSimilarity = args[1].createScalarEvaluator(ctx);
                    private final IScalarEvaluator evalThreshold = args[2].createScalarEvaluator(ctx);

                    private final SimilarityFiltersCache similarityFiltersCache = new SimilarityFiltersCache();

                    // result
                    private final AMutableInt32 res = new AMutableInt32(0);
                    @SuppressWarnings("unchecked")
                    private final ISerializerDeserializer<AInt32> int32Serde =
                            SerializerDeserializerProvider.INSTANCE.getSerializerDeserializer(BuiltinType.AINT32);

                    @Override
                    public void evaluate(IFrameTupleReference tuple, IPointable result) throws HyracksDataException {
                        resultStorage.reset();
                        // length
                        evalLen.evaluate(tuple, inputVal);
                        byte[] data = inputVal.getByteArray();
                        int offset = inputVal.getStartOffset();
                        if (data[offset] != ATypeTag.SERIALIZED_INT32_TYPE_TAG) {
                            throw new TypeMismatchException(sourceLoc, getIdentifier(), 0, data[offset],
                                    ATypeTag.SERIALIZED_INT32_TYPE_TAG);
                        }
                        int length = IntegerPointable.getInteger(data, offset + 1);

                        // similarity threshold
                        evalThreshold.evaluate(tuple, inputVal);
                        data = inputVal.getByteArray();
                        offset = inputVal.getStartOffset();
                        if (data[offset] != ATypeTag.SERIALIZED_DOUBLE_TYPE_TAG) {
                            throw new TypeMismatchException(sourceLoc, getIdentifier(), 1, data[offset],
                                    ATypeTag.SERIALIZED_DOUBLE_TYPE_TAG);
                        }
                        float similarityThreshold = (float) ADoubleSerializerDeserializer.getDouble(data, offset + 1);

                        // similarity name
                        evalSimilarity.evaluate(tuple, inputVal);
                        data = inputVal.getByteArray();
                        offset = inputVal.getStartOffset();
                        int len = inputVal.getLength();
                        if (data[offset] != ATypeTag.SERIALIZED_STRING_TYPE_TAG) {
                            throw new TypeMismatchException(sourceLoc, getIdentifier(), 2, data[offset],
                                    ATypeTag.SERIALIZED_STRING_TYPE_TAG);
                        }
                        SimilarityFilters similarityFilters =
                                similarityFiltersCache.get(similarityThreshold, data, offset, len);

                        int prefixLength = similarityFilters.getPrefixLength(length);
                        res.setValue(prefixLength);

                        try {
                            int32Serde.serialize(res, out);
                        } catch (IOException e) {
                            throw HyracksDataException.create(e);
                        }
                        result.set(resultStorage);
                    }
                };
            }
--------------------

