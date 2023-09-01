package element;

import visitor.ShoppingCartVisitor;

public interface ItemElement {
    int accept(ShoppingCartVisitor visitor);
}
