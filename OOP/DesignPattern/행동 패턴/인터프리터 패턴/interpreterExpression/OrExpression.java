package interpreterExpression;

public class OrExpression implements Expression{
    Expression exp1;
    Expression exp2;

    public OrExpression(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public boolean interpreter(String context) {
        return exp1.interpreter(context) || exp2.interpreter(context);
    }
}
