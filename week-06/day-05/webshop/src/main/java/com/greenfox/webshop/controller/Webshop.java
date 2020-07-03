package com.greenfox.webshop.controller;

import com.greenfox.webshop.model.Database;
import com.greenfox.webshop.model.ShopItem;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Webshop {

  protected Database database = new Database();
  protected String currency = "€";

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
        .average();
    double average = 0.0;

    if(averageStock.isPresent()) {
      average = averageStock.getAsDouble();
    }

    model.addAttribute("average", average);
    return "average";
  }

}
