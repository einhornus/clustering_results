public Frame makeNewSubroutineStartFrame(int subLabel, int callerLabel) {
        IntList newSubroutines = subroutines.mutableCopy();
        newSubroutines.add(subLabel);
        Frame newFrame = new Frame(locals.getPrimary(), stack,
                IntList.makeImmutable(subLabel));
        return newFrame.mergeWithSubroutineCaller(this, subLabel, callerLabel);
    }
--------------------

public DalvInsn hrVersion() {
        RegisterSpecList regs =
            registers.withSequentialRegisters(0, hasResult());
        return withRegisters(regs);
    }
--------------------

public RopMethod withRegisterOffset(int delta) {
        RopMethod result = new RopMethod(blocks.withRegisterOffset(delta),
                                         firstLabel);

        if (exitPredecessors != null) {
            /*
             * The predecessors have been calculated. It's safe to
             * inject these into the new instance, since the
             * transformation being applied doesn't affect the
             * predecessors.
             */
            result.exitPredecessors = exitPredecessors;
            result.predecessors = predecessors;
        }

        return result;
    }
--------------------

