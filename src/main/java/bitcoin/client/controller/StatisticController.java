package bitcoin.client.controller;

import bitcoin.client.entry.Wallet;
import bitcoin.client.service.WalletService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/statistic")
public class StatisticController {
    String DEFAULT_TAG = "exchanges";
    String DEFAULT_ADD = "13yA1dwP63TXsaB6cpxdv6XoRNYpWSAN4v";
    String DEFAULT_WEBNAME = "13yA1dwP63TXsaB6cpxdv6XoRNYpWSAN4v";
    String DEFAULT_GRAPH = "/static/graph/AV_13yA1dwP63TXsaB6cpxdv6XoRNYpWSAN4v.png";
    Integer DEFAULT_SIMI = 0;

    @GetMapping("/index")
    public String index(ModelMap modelMap){
        modelMap.put("tagOD",DEFAULT_TAG);
        modelMap.put("webNameOD",DEFAULT_WEBNAME);
        modelMap.put("similarityOD",DEFAULT_SIMI);
        modelMap.put("addressOD",DEFAULT_ADD);
        modelMap.put("graphPathOD", DEFAULT_GRAPH);
        return "statisticWallet";
    }

    @PostMapping("/getWallet")
    @ResponseBody
    public Wallet getWallet(HttpServletRequest request){
        String tag = request.getParameter("filterTag");
        String jsonStr = WalletService.readFileFromJar();
        Wallet walletObj = WalletService.getWallet(jsonStr,tag);
        System.out.println(walletObj.getAddressOD());
        return walletObj;
    }

    @GetMapping("/getWalletByAddress/{address}")
    public String getWalletByAddress(@PathVariable("address") String address,ModelMap modelMap){
        String jsonStr = WalletService.readFileFromJar();
        Wallet walletObj = WalletService.getWalletByAddress(jsonStr,address);
        modelMap.put("tagOD",walletObj.getTagOD());
        modelMap.put("webNameOD",walletObj.getWebNameOD());
        modelMap.put("similarityOD",walletObj.getSimilarityOD());
        modelMap.put("addressOD",walletObj.getAddressOD());
        modelMap.put("graphPathOD", walletObj.getGraphPathOD());
        return "statisticWallet";
    }



}
