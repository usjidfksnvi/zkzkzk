package com.example.demo.controller;

import com.ylsoft.framework.core.encrypt.Coder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/try")
public class TryController {
//    BASE64加密
    @GetMapping("/encrypt")
    public String encrypt(@RequestParam String input){
        try {
            byte[] inputBytes = input.getBytes("UTF-8");
            return Coder.encryptBASE64(inputBytes);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
//    BASE64解密
    @GetMapping("/decrypt")
    public String decrypt(@RequestParam String input) {
        try {
            byte[] outputBytes = Coder.decryptBASE64(input);
            return new String(outputBytes, "UTF-8");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
