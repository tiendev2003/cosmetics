package datn.com.cosmetics.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import datn.com.cosmetics.bean.request.MomoPaymentRequest;
import datn.com.cosmetics.config.MoMoConfig;
import datn.com.cosmetics.services.IOrderService;
import datn.com.cosmetics.services.MoMoPaymentService;

@RestController
@RequestMapping("/api/momo")
public class MomoController {
    @Autowired
    private MoMoConfig moMoConfig;

    @Autowired
    private MoMoPaymentService moMoPaymentService;

    @Autowired
    private IOrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> paymentWithMomo(@RequestBody MomoPaymentRequest request) {
        try {
            String payUrl = moMoPaymentService.paymentWithMomo(request.getOrderId(), request.getTotal());
            return ResponseEntity.ok(payUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/callback")
    public String callback(@RequestParam String orderId, @RequestParam String amount,
            @RequestParam(value = "resultCode") String resultCode, @RequestParam(value = "message") String message) {
        Long id = Long.parseLong(orderId);
        if (resultCode.equals("0")) {
            // convert id string to long
            orderService.changeStatusOrder(id, "PROCESSING");
        } else {
            orderService.changeStatusOrder(id, "PENDING");
        }
        return "";
    }

    @GetMapping("/ipn")
    public String ipn(@RequestParam String orderId, @RequestParam String amount) {
        return "redirect:" + moMoConfig.getRedirectUrl() + "?orderId=" + orderId + "&amount=" + amount;
    }

}
