import interpreterExpression.AndExpression;
import interpreterExpression.Expression;
import interpreterExpression.OrExpression;
import interpreterExpression.TerminalExpression;

public class InterpreterDemo {
    public static void main(String[] args) {
        Expression person1 = new TerminalExpression("hoon");
        Expression person2 = new TerminalExpression("dev");
        Expression isSingle = new OrExpression(person1, person2);

        Expression vikram = new TerminalExpression("Vikram");
        Expression committed = new TerminalExpression("Committed");
        Expression isCommitted = new AndExpression(vikram, committed);

        System.out.println(isSingle.interpreter("hoon"));
        System.out.println(isSingle.interpreter("dev"));
        System.out.println(isSingle.interpreter("qwe"));

        System.out.println(isCommitted.interpreter("Committed, Vikram"));
        System.out.println(isCommitted.interpreter("Single, Vikram"));

    }
}
