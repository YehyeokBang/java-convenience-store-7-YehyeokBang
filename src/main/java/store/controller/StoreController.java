package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.DisplayProduct;
import store.dto.ProductInfo;
import store.dto.ReceiptData;
import store.model.Customer;
import store.model.YesOrNoParser;
import store.model.order.Order;
import store.model.order.parser.OrderParser;
import store.model.store.Membership;
import store.model.store.Product;
import store.model.store.Promotion;
import store.model.store.Shelf;
import store.model.store.Store;
import store.model.store.StoreStaff;
import store.util.RetryHandler;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final OrderParser<String> orderParser;

    public StoreController(InputView inputView, OutputView outputView, OrderParser<String> orderParser) {
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

    private void showProducts(List<DisplayProduct> products) {
        outputView.printCurrentProductsMessage();
        outputView.printDisplayProducts(products);
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
            int changeCount = storeStaff.checkPromotion(takenProducts);
            change(shelf, takenProducts, order.getName(), changeCount);
            products.addAll(takenProducts);
        }
        return products;
    }

    private void change(Shelf shelf, List<Product> takenProducts, String productName, int changeCount) {
        if (changeCount == 1) {
            shelf.takeOut(productName, 1);
            return;
        }
        for (int i = changeCount; i < 0; i++) {
            shelf.add(takenProducts.removeLast());
        }
    }

    private Boolean askMembership() {
        return RetryHandler.retryIfError(this::membership);
    }

    private boolean membership() {
        String rawInputMembership = inputView.requestMembership();
        return new YesOrNoParser().parse(rawInputMembership);
    }

    private void printReceipt(List<Product> products, boolean isMembership) {
        ReceiptData receiptData = calculateProducts(products, isMembership);
        printReceipt(receiptData);
    }

    private ReceiptData calculateProducts(List<Product> products, boolean isMembership) {
        Map<String, List<Product>> groupedProducts = products.stream()
                .collect(Collectors.groupingBy(Product::getName));

        List<ProductInfo> productInfos = new ArrayList<>();
        List<ProductInfo> promotionProducts = new ArrayList<>();
        int totalPrice = 0;
        int totalQuantity = 0;
        int promotionDiscount = 0;

        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String productName = entry.getKey();
            List<Product> productList = entry.getValue();
            List<Product> promotionProducts2 = productList.stream()
                    .filter(Product::hasPromotion)
                    .toList();
            Product product = productList.getFirst();
            int quantity = productList.size();
            int quantity2 = promotionProducts2.size();
            int price = product.getPrice();

            int promotionQuantity = 0;
            if (product.hasPromotion()) {
                Promotion promotion = product.getPromotion();
                if (promotion.isApplicableToday()) {
                    int buyQuantity = promotion.getBuyQuantity();
                    int freeQuantity = promotion.getFreeQuantity();
                    promotionQuantity = quantity2 / (buyQuantity + freeQuantity);
                }
            }
            int productTotalPrice = price * quantity;
            totalPrice += productTotalPrice;

            productInfos.add(new ProductInfo(productName, price, quantity));
            if (promotionQuantity > 0) {
                promotionDiscount = price * promotionQuantity;
                promotionProducts.add(new ProductInfo(productName, price, promotionQuantity));
            }

            totalQuantity += quantity;
        }

        int membershipDiscountPrice = getMembershipDiscountPrice(isMembership, totalPrice);

        int finalPrice = totalPrice - membershipDiscountPrice - promotionDiscount;
        return new ReceiptData(
                productInfos,
                promotionProducts,
                totalQuantity,
                totalPrice,
                promotionDiscount,
                membershipDiscountPrice,
                finalPrice
        );
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
