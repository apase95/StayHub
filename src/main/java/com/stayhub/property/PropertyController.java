package com.stayhub.property;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/properties/{id}")
    public String showProperty(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyService.getPublicProperty(id));
        return "property/property-detail";
    }
}
