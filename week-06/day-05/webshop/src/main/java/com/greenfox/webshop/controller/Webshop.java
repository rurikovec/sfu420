package com.greenfox.webshop.controller;

import com.greenfox.webshop.model.Database;
import com.greenfox.webshop.model.ProductCategory;
import com.greenfox.webshop.model.ShopItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Webshop {

  protected Database database = new Database();
  protected String currency = "€";
  protected double rateHUF = 354.60;

  @GetMapping("/webshop")
  public String listAllItems(Model model) {
    model.addAttribute("products", database.getProducts());
    model.addAttribute("currency", currency);
    return "index";
  }

  @GetMapping("/only-available")
  public String onlyAvailable(Model model) {
    List<ShopItem> onlyAvailable = database.getProducts().stream()
        .filter(product -> product.getQtyOfStock() > 0)
        .collect(Collectors.toList());

    model.addAttribute("products", onlyAvailable);
    model.addAttribute("currency", currency);
    return "index";
  }

  @GetMapping("/cheapest-first")
  public String cheapestFirst(Model model) {
    List<ShopItem> cheapestFirst = database.getProducts().stream()
        .sorted(Comparator.comparing(ShopItem::getPrice))
        .collect(Collectors.toList());

    model.addAttribute("products", cheapestFirst);
    model.addAttribute("currency", currency);
    return "index";
  }

  @GetMapping("/contains-nike")
  public String containsNike(Model model) {
    List<ShopItem> containsNike = database.getProducts().stream()
        .filter(product -> product.getName().toLowerCase().contains("nike") ||
            product.getDescription().toLowerCase().contains("nike"))
        .collect(Collectors.toList());

    model.addAttribute("products", containsNike);
    model.addAttribute("currency", currency);
    return "index";
  }

  @GetMapping("/most-expensive")
  public String mostExpensive(Model model) {
    Optional<ShopItem> mostExpensive = database.getProducts().stream()
        .filter(product -> product.getQtyOfStock() > 0)
        .max(Comparator.comparing(ShopItem::getPrice));

    ShopItem product = null;
    if (mostExpensive.isPresent()) {
      product = mostExpensive.get();
    }

    model.addAttribute("products", product);
    model.addAttribute("currency", currency);
    return "index";
  }

  @GetMapping("/average-stock")
  public String averageStock(Model model) {
    OptionalDouble averageStock = database.getProducts().stream()
        .mapToDouble(ShopItem::getQtyOfStock)
        .average();   //TODO what if is null?
    double average = 0.0;

    if (averageStock.isPresent()) {
      average = averageStock.getAsDouble();
    }

    model.addAttribute("average", average);
    return "average";
  }

  @PostMapping("/search")
  public String searchProduct(@RequestParam String searchBox, Model model) {
    String searchingFor = searchBox.toLowerCase().trim();

    List<ShopItem> result = database.getProducts().stream()
        .filter(product -> product.getName().toLowerCase().contains(searchingFor) ||
            product.getDescription().toLowerCase().contains(searchingFor))
        .collect(Collectors.toList());

    model.addAttribute("products", result);
    model.addAttribute("currency", currency);
    return "index";
  }

  @GetMapping("/more-filters")
  public String moreFilters(Model model) {
    model.addAttribute("products", database.getProducts());
    model.addAttribute("currency", currency);
    return "more";
  }

  @GetMapping("/filter-by-type/{type}")
  public String filterByType(@PathVariable String type, Model model) {
    ProductCategory filter = null;
    List<ShopItem> filteredByType;
    switch (type) {
      case "clothes-and-shoes":
        filter = ProductCategory.CLOTHES_SHOES;
        break;
      case "electronics":
        filter = ProductCategory.ELECTRONICS;
        break;
      case "beverages-and-snacks":
        filter = ProductCategory.BEVERAGES_SNACKS;
        break;
    }

    if (filter == null) {
      filteredByType = database.getProducts();    // It provide the full list, if user try manipulate the URL
    } else {
      ProductCategory finalFilter = filter;
      filteredByType = database.getProducts().stream()
          .filter(product -> product.getType() == finalFilter)
          .collect(Collectors.toList());
    }

    model.addAttribute("products", filteredByType);
    model.addAttribute("currency", currency);
    return "more";
  }

  @GetMapping("/huf")
  public String displayHUF(Model model) {
    List<Double> pricesInHUF = new ArrayList<>();

    database.getProducts()
        .forEach(product -> pricesInHUF.add(product.getPrice() * rateHUF));

    model.addAttribute("products", database.getProducts());
    model.addAttribute("convertedPrice", pricesInHUF);
    model.addAttribute("currency", "HUF");
    return "more";
  }
}
