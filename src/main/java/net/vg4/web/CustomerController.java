package net.vg4.web;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.val;

import net.vg4.domain.Customer;
import net.vg4.service.CustomerService;
import net.vg4.service.LoginUserDetails;

@Controller
@RequestMapping("customers")
@val
public class CustomerController {
    final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {this.customerService = customerService;}

    @ModelAttribute
    CustomerForm setUpForm() {
        return new CustomerForm();
    }

    @GetMapping
    String list(Model model) {
        val customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    @PostMapping("create")
    String create(@Validated CustomerForm form, BindingResult result, Model model,
                  @AuthenticationPrincipal LoginUserDetails userDetails) {
        if (result.hasErrors()) {
            return this.list(model);
        }
        val customer = new Customer();
        BeanUtils.copyProperties(form, customer);
        customerService.create(customer, userDetails.getUser());
        return "redirect:/customers";
    }

    @GetMapping(value = "edit", params = "form")
    String editForm(@RequestParam Integer id, CustomerForm form) {
        val customer = customerService.findOne(id);
        BeanUtils.copyProperties(customer, form);
        return "customers/edit";
    }

    @PostMapping("edit")
    String edit(@RequestParam Integer id, @Validated CustomerForm form, BindingResult result,
                @AuthenticationPrincipal LoginUserDetails userDetails) {
        if (result.hasErrors()) {
            return editForm(id, form);
        }
        val customer = new Customer();
        BeanUtils.copyProperties(form, customer);
        customer.setId(id);
        customerService.update(customer, userDetails.getUser());
        return "redirect:/customers";
    }

    @PostMapping(value = "edit", params = "goToTop")
    String goToTop() {
        return "redirect:/customers";
    }

    @PostMapping(value = "delete")
    String delete(@RequestParam Integer id) {
        customerService.delete(id);
        return "redirect:/customers";
    }
}
