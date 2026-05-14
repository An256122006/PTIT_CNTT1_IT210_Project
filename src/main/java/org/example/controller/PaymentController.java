package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Payments;
import org.example.service.IPaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hospital/patient")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @GetMapping("/payment/{id}")
    public String qrPayment(
            @PathVariable("id") Long id,
            Model model
    ) {

        Payments payments =
                paymentService.findById(id);

        String bank = "MB";
        String accountNo = "0343580389";
        String accountName = "DINH TRONG AN";

        String content =
                "PAYMENT" + payments.getId();

        String qrUrl =
                "https://img.vietqr.io/image/"
                        + bank + "-"
                        + accountNo
                        + "-compact2.png"
                        + "?amount=" + payments.getAmount()
                        + "&addInfo=" + content
                        + "&accountName="
                        + accountName.replace(" ", "%20");

        model.addAttribute("qrUrl", qrUrl);
        model.addAttribute("payment", payments);

        return "patient/patient-payment-pr";
    }
   @PostMapping("/payment")
   public String webhook(
           @RequestBody Map<String,Object> body
   ){
       String content =
               body.get("content").toString();

       Double amount =
               Double.valueOf(
                       body.get("transferAmount").toString()
               );

       List<Payments> payments =
               paymentService.findByStatus("PENDING");

       for(Payments payment : payments){

           String code =
                   "PAYMENT" + payment.getId();

           if(content.contains(code)
                   && amount.compareTo(payment.getAmount()) == 0){

               payment.setStatus("PAID");

               paymentService.save(payment);

               return "redirect:/hospital/patient/payment/" + payment.getId();
           }
       }
       return "redirect:/hospital/patient/payment";
   }
}
