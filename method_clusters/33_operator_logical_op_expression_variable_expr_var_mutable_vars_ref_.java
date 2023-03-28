private Mutable<ILogicalOperator> findJoinOp(Mutable<ILogicalOperator> opRef) {
        AbstractLogicalOperator op = (AbstractLogicalOperator) opRef.getValue();
        switch (op.getOperatorTag()) {
            case INNERJOIN:
            case LEFTOUTERJOIN: {
                return opRef;
            }
            // Bail on these operators.
            case GROUP:
            case AGGREGATE:
            case DISTINCT:
            case UNNEST_MAP: {
                return null;
            }
            // Traverse children.
            default: {
                for (Mutable<ILogicalOperator> childOpRef : op.getInputs()) {
                    return findJoinOp(childOpRef);
                }
            }
        }
        return null;
    }
--------------------

public static List<PlanStage> getStages(ILogicalPlan plan) throws AlgebricksException {
        // There could be only one root operator for a top-level query plan.
        final ILogicalOperator rootOp = plan.getRoots().get(0).getValue();
        final PlanStagesGenerator stagesGenerator = new PlanStagesGenerator();
        rootOp.accept(stagesGenerator, null);
        return stagesGenerator.getStages();
    }
--------------------

@Override
    public Void visitScriptOperator(ScriptOperator op, Void arg) throws AlgebricksException {
        List<LogicalVariable> usedVariables = new ArrayList<LogicalVariable>();
        VariableUtilities.getUsedVariables(op, usedVariables);
        for (LogicalVariable v : op.getOutputVariables()) {
            if (!usedVariables.contains(v)) {
                producedVariables.add(v);
            }
        }
        return null;
    }
--------------------

