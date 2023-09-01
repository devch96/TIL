package interpreterExpression;

public class TerminalExpression implements Expression{
    String data;

    public TerminalExpression(String data) {
        this.data = data;
    }

    @Override
    public boolean interpreter(String context) {
        return context.contains(data);
    }
}
