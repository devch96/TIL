package factory;

import product.Chair;
import product.Table;
import product.VictorianChair;
import product.VictorianTable;

public class VictorianFurnitureFactory implements FurnitureFactory{
    @Override
    public Chair createChair() {
        return new VictorianChair();
    }

    @Override
    public Table createTable() {
        return new VictorianTable();
    }
}
