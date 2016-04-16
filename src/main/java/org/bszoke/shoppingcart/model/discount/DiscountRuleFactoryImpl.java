package org.bszoke.shoppingcart.model.discount;

public class DiscountRuleFactoryImpl implements DiscountRuleFactory {

    public Discount createRuleChain() {
        Discount rootDiscount = new TwoForOneDiscount();
        Discount threeForTwoDiscount = new ThreeForTwoDiscount();
        rootDiscount.setSuccessor(threeForTwoDiscount);
        return rootDiscount;
    }
}
