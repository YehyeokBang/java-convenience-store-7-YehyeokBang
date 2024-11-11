package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import store.dto.ProductDetails;
import store.dto.ProductInfo;
import store.dto.ReceiptData;
import store.model.Customer;
import store.model.parser.YesOrNoParser;
import store.model.order.Order;
import store.model.parser.Parser;
import store.model.event.Membership;
import store.model.store.Product;
import store.model.event.Promotion;
import store.model.event.PromotionResult;
import store.model.event.PromotionResultType;
import store.model.store.Shelf;
import store.model.store.Store;
import store.model.store.StoreStaff;
import store.util.RetryHandler;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final Parser<String, List<Order>> orderParser;

    public StoreController(InputView inputView, OutputView outputView, Parser<String, List<Order>> orderParser) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.orderParser = orderParser;
    }

    public void start() {
        Store store = Store.open();
        boolean isRepurchase;
        do {
            Customer customer = enterStore(store);
            List<Product> products = orderProducts(store, customer);
            boolean isMembership = askMembership();
            printReceipt(products, isMembership);
            isRepurchase = askRepurchase();
        } while (isRepurchase);
    }

    private Customer enterStore(Store store) {
        Customer customer = new Customer();
        printWelcomeMessage();
        showProducts(store.getInfo());
        return customer;
    }

    private void printWelcomeMessage() {
        outputView.printWelcomeMessage();
    }

    private void showProducts(List<ProductDetails> products) {
        outputView.printCurrentProductsMessage();
        outputView.printProductDetails(products);
    }

    private List<Product> orderProducts(Store store, Customer customer) {
        return RetryHandler.retryIfError(() -> order(store, customer));
    }

    private List<Product> order(Store store, Customer customer) {
        List<Order> orders = getOrdersFromInput();
        StoreStaff storeStaff = store.getStoreStaff();
        Shelf shelf = store.getShelf();
        return getProducts(customer, orders, shelf, storeStaff);
    }

    private List<Order> getOrdersFromInput() {
        String rawInputOrder = inputView.requestOrder();
        return orderParser.parse(rawInputOrder);
    }

    private List<Product> getProducts(Customer customer, List<Order> orders, Shelf shelf, StoreStaff storeStaff) {
        List<Product> products = new ArrayList<>();
        for (Order order : orders) {
            customer.addProductsInShoppingCart(shelf, order);
            List<Product> takenProducts = customer.unloadShoppingCart();
            PromotionResult promotionResult = storeStaff.evaluatePromotion(takenProducts, shelf);
            processPromotionChange(shelf, takenProducts, promotionResult);
            products.addAll(takenProducts);
        }
        return products;
    }

    private void processPromotionChange(Shelf shelf, List<Product> takenProducts, PromotionResult promotionResult) {
        if (isNotChange(promotionResult)) {
            return;
        }
        if (promotionResult.checkType(PromotionResultType.FREE_PRODUCT_AVAILABLE)) {
            offerFreeProductIfAccepted(shelf, takenProducts, promotionResult);
            return;
        }
        if (promotionResult.checkType(PromotionResultType.PROMOTION_INSUFFICIENT_STOCK)) {
            confirmPurchaseOrDiscard(shelf, takenProducts, promotionResult);
        }
    }

    private void offerFreeProductIfAccepted(Shelf shelf, List<Product> takenProducts, PromotionResult promotionResult) {
        if (requestFreeGet(promotionResult.getProductName(), promotionResult.getChangeCount())) {
            takenProducts.addAll(shelf.takeOut(promotionResult.getProductName(), promotionResult.getChangeCount()));
        }
    }

    private boolean requestFreeGet(String productName, int additionalQuantityNeeded) {
        return RetryHandler.retryIfError(() -> {
            String rawInputNoPromotion = inputView.requestFreePromotion(productName, additionalQuantityNeeded);
            return new YesOrNoParser().parse(rawInputNoPromotion);
        });
    }

    private void confirmPurchaseOrDiscard(Shelf shelf, List<Product> takenProducts, PromotionResult promotionResult) {
        if (!requestNoPromotionProduct(promotionResult.getProductName(), promotionResult.getChangeCount())) {
            for (int i = 0; i < promotionResult.getChangeCount(); i++) {
                shelf.add(takenProducts.removeLast());
            }
        }
    }

    private boolean requestNoPromotionProduct(String productName, int quantityNotApplied) {
        return RetryHandler.retryIfError(() -> {
            String rawInputNoPromotion = new InputView().requestNoPromotion(productName, quantityNotApplied);
            return new YesOrNoParser().parse(rawInputNoPromotion);
        });
    }

    private boolean isNotChange(PromotionResult promotionResult) {
        return promotionResult.checkType(PromotionResultType.NO_CHANGE)
                || promotionResult.checkType(PromotionResultType.NO_CHANGE);
    }

    private Boolean askMembership() {
        return RetryHandler.retryIfError(this::membership);
    }

    private boolean membership() {
        String rawInputMembership = inputView.requestMembership();
        return new YesOrNoParser().parse(rawInputMembership);
    }

    private void printReceipt(List<Product> products, boolean isMembership) {
        ReceiptData receiptData = createReceiptData(products, isMembership);
        printReceipt(receiptData);
    }

    private ReceiptData createReceiptData(List<Product> products, boolean isMembership) {
        Map<String, List<Product>> groupedProducts = getProductsGroupingByName(products);
        return calculatePayments(isMembership, groupedProducts);
    }

    private ReceiptData calculatePayments(boolean isMembership, Map<String, List<Product>> groupedProducts) {
        int totalPrice = getTotalPrice(groupedProducts);
        int totalQuantity = getTotalQuantity(groupedProducts);
        int promotionDiscount = getPromotionDiscount(groupedProducts);
        int promotionAppliedAmount = getPromotionAppliedAmount(groupedProducts);
        int membershipDiscountPrice = getMembershipDiscountPrice(isMembership, totalPrice - promotionAppliedAmount);
        int finalPrice = totalPrice - membershipDiscountPrice - promotionDiscount;
        List<ProductInfo> productInfos = getProductInfos(groupedProducts);
        List<ProductInfo> promotionProductInfos = getPromotionProductInfos(groupedProducts);
        return new ReceiptData(productInfos, promotionProductInfos, totalQuantity, totalPrice, promotionDiscount, membershipDiscountPrice, finalPrice);
    }

    private List<ProductInfo> getProductInfos(Map<String, List<Product>> groupedProducts) {
        List<ProductInfo> productInfos = new ArrayList<>();
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String productName = entry.getKey();
            List<Product> productList = entry.getValue();
            Product product = productList.getFirst();
            int quantity = productList.size();
            productInfos.add(new ProductInfo(productName, product.getPrice() * quantity, quantity));
        }
        return productInfos;
    }

    private List<ProductInfo> getPromotionProductInfos(Map<String, List<Product>> groupedProducts) {
        List<ProductInfo> promotionProductInfos = new ArrayList<>();
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            List<Product> productList = entry.getValue();
            Product product = productList.getFirst();
            int promotionQuantity = getPromotionProductCount(productList);
            int promotionGetQuantity;
            if (product.hasPromotion()) {
                Promotion promotion = product.getPromotion();
                if (promotion.isApplicableToday()) {
                    promotionGetQuantity = promotionQuantity / (promotion.getBuyQuantity() + promotion.getFreeQuantity());
                    promotionProductInfos.add(new ProductInfo(product.getName(), product.getPrice(), promotionGetQuantity));
                }
            }
        }
        return promotionProductInfos;
    }

    private int getPromotionDiscount(Map<String, List<Product>> groupedProducts) {
        int promotionDiscount = 0;
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            List<Product> productList = entry.getValue();
            Product product = productList.getFirst();
            int promotionQuantity = getPromotionProductCount(productList);
            int promotionGetQuantity;
            if (product.hasPromotion()) {
                Promotion promotion = product.getPromotion();
                if (promotion.isApplicableToday()) {
                    promotionGetQuantity = promotionQuantity / (promotion.getBuyQuantity() + promotion.getFreeQuantity());
                    promotionDiscount += product.getPrice() * promotionGetQuantity;
                }
            }
        }
        return promotionDiscount;
    }

    private int getPromotionAppliedAmount(Map<String, List<Product>> groupedProducts) {
        int promotionAppliedAmount = 0;
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            List<Product> productList = entry.getValue();
            Product product = productList.getFirst();
            int promotionQuantity = getPromotionProductCount(productList);
            int price = product.getPrice();
            if (product.hasPromotion()) {
                Promotion promotion = product.getPromotion();
                if (promotion.isApplicableToday()) {
                    int promotionGetQuantity = promotionQuantity / (promotion.getBuyQuantity() + promotion.getFreeQuantity());
                    promotionAppliedAmount += promotionGetQuantity * (promotion.getBuyQuantity() + promotion.getFreeQuantity()) * price;
                }
            }
        }
        return promotionAppliedAmount;
    }

    private int getTotalPrice(Map<String, List<Product>> groupedProducts) {
        return groupedProducts.values()
                .stream()
                .mapToInt(products -> products.getFirst().getPrice() * products.size())
                .sum();
    }

    private int getTotalQuantity(Map<String, List<Product>> groupedProducts) {
        return groupedProducts.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    private LinkedHashMap<String, List<Product>> getProductsGroupingByName(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getName, LinkedHashMap::new, Collectors.toList()));
    }

    private int getPromotionProductCount(List<Product> productList) {
        return (int) productList.stream()
                .filter(Product::hasPromotion)
                .count();
    }

    private int getMembershipDiscountPrice(boolean isMembership, int totalPrice) {
        return new Membership().getMembershipDiscountPrice(isMembership, totalPrice);
    }

    private void printReceipt(ReceiptData receiptData) {
        outputView.printReceipt(receiptData);
    }

    private Boolean askRepurchase() {
        return RetryHandler.retryIfError(this::repurchase);
    }

    private boolean repurchase() {
        String rawInputRepurchase = inputView.requestRepurchase();
        return new YesOrNoParser().parse(rawInputRepurchase);
    }
}
