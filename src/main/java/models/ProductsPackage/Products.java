package models.ProductsPackage;

import java.util.HashMap;
import java.util.Map;

public interface Products {
      static String name = "";

       Map<Double, String> quality = new HashMap<>();

      int price = 0;

    //TODO:set name
     static void setName(String name) {
        name = name;
    }

     static String getName() {
        return name;
    }
    static void setPrice(int price) {}

}
