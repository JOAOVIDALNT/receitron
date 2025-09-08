package com.joaovidal.receitron.domain.port.out;

import java.util.List;

public interface MealdbApiPort {
    List<String> listCultures();
    List<String> listCategories();
}
