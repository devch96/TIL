package visitor;

import element.Fruit;
import element.Meat;

public interface ShoppingCartVisitor {
    int visit(Meat meat);

    int visit(Fruit fruit);
}
